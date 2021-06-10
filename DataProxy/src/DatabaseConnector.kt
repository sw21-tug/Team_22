package com.Table.Server
import com.Table.Server.DatabaseObjects.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.mindrot.jbcrypt.BCrypt


class DatabaseConnector {
    //inMemory keep alive between transactions
    var db = Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    fun connectToDatabase(){
        TransactionManager.defaultDatabase = db
        transaction(db) {
            //create Table
            SchemaUtils.create(Users)
            SchemaUtils.create(Bios)
            SchemaUtils.create(GroupsToUsers)
            SchemaUtils.create(Groups)

        }
        print("Created Database\n")
    }

    fun insertUser(user: User) : Int {
        val salt = BCrypt.gensalt()
        return transaction(db) {
            val bio_id = Bios.insert {
            }[Bios.bio_id]
            Users.insert {
                it[username] = user.username
                it[email] = user.email
                it[passwordHash] = BCrypt.hashpw(user.password, salt)
                it[Users.salt] = salt
                it[Users.bioId]=bio_id
            }[Users.id]
        }
    }

    fun updateBio(bio:Bio, username: String):Int{
        var user: User
        val users: List<User> = getUserByUsername(username)

        if (users.isEmpty())
            return -1
        else
            user = users[0]

        transaction(db){
            Bios.update({Bios.bio_id eq user.bio_id!!}){
                if(bio.user_name!=null){it[user_name]=bio.user_name }
                if(bio.age!=null){ it[age] = bio.age }
                if(bio.city!=null){ it[city] = bio.city }
                if(bio.card_games!=null){ it[card_games] = bio.card_games }
                if(bio.board_games!=null){ it[board_games] = bio.board_games }
                if(bio.ttrpg!=null){ it[ttrpg] = bio.ttrpg }
                if(bio.wargames!=null){ it[wargames] = bio.wargames }
            }
        }
        return 0
    }
    fun getBioByUsername(username: String):List<Bio>{
        val bio = transaction(db){
            val user: User = getUserByUsername(username)[0]
            Bios.select{Bios.bio_id eq user.bio_id!!}.map{ Bios.toBio(it)}
        }
        return bio
    }

    fun getUserById(id:Int):List<User>{
        val users = transaction(db){
            Users.select{Users.id eq id}.map{ Users.toUser(it)}
        }
        return users
    }

    fun getUserByUsername(username: String):List<User>{
        val users = transaction(db){
            Users.select{Users.username eq username}.map{ Users.toUser(it)}
        }
        return users
    }

    fun getAllUsers():List<User>{
        val user = transaction(db){
            Users.selectAll().map{ Users.toUser(it)}
        }
        return user
    }

    fun getUserForAuth(credentials: UserCredentials):List<UserCredentials>{
        val salt: String?
        val users = transaction(db){
            Users.select{Users.username eq credentials.username}.map{ Users.toUserCredentials(it)}
        }
        return users
    }

    fun updateUsers(id:Int, name:String?=null):Int{
        transaction(db){
            Users.update({Users.id eq id}){
                if(name!=null){ it[username] = name }
            }
        }
        return 0
    }

    fun createGroup(groupCredentials: GroupCredentials):Int {
        transaction {
            val group_id = Groups.insert {
                it[group_name] = groupCredentials.groupname
                it[group_admin] = groupCredentials.username
            }[Groups.group_id]
            val user = Users.select{Users.username eq groupCredentials.username}.map{Users.toUser(it)}[0]

            GroupsToUsers.insert {
                it[GroupsToUsers.group_id] = group_id
                it[GroupsToUsers.user_id] = user.id!!
            }
        }
        return 0
    }

    fun addMemberToGroup(groupCredentials: GroupCredentials):Int{
        val ret = transaction {
            val userList = Users.select{Users.username eq groupCredentials.username}.map{ Users.toUser(it)}
            if(userList.isEmpty())
            {
                return@transaction 1
            }
            var user = userList[0]

            val relationList = GroupsToUsers.select{
                (GroupsToUsers.group_id eq groupCredentials.groupid!!) and
                        (GroupsToUsers.user_id eq user.id!!)}.map{GroupsToUsers.toGroupToUser(it)}
            if(!relationList.isEmpty()){
                return@transaction 2
            }

            GroupsToUsers.insert {
                it[GroupsToUsers.group_id] = groupCredentials.groupid!!
                it[GroupsToUsers.user_id] = user.id!!
            }
            return@transaction 0
        }
        return ret
    }

    fun deleteMemberFromGroup(groupCredentials: GroupCredentials):Int{
        transaction {
            val user = Users.select{Users.username eq groupCredentials.username}.map{ Users.toUser(it)}[0]
            GroupsToUsers.deleteWhere{(GroupsToUsers.user_id eq user.id!!) and
                    (GroupsToUsers.group_id eq groupCredentials.groupid!!)}
        }
        return 0
    }

    fun getGroupNames(username:String):List<Pair<String,String>>{
        val ret = transaction {
            val user = Users.select{Users.username eq username}.map{ Users.toUser(it)}[0]
            val selected_group_ids = GroupsToUsers.select{(GroupsToUsers.user_id eq user.id!!)}.map{GroupsToUsers.toGroupToUser(it)}
            var list: MutableList<Pair<String, String>> = ArrayList()
            for(relation in selected_group_ids)
            {
                val selected_groups = Groups.select{(Groups.group_id eq relation.group_id)}.map{Groups.toGroup(it)}
                list.add(Pair(relation.group_id.toString(),selected_groups[0].group_name))
            }
            return@transaction list
        }
        return ret
    }

    fun getGroupUserNames(groupCredentials: GroupCredentials):List<String>
    {
        var user_list: MutableList<String> = ArrayList()
        val return_value = transaction {
            val selected_user_group_relations = GroupsToUsers.select{(GroupsToUsers.group_id eq
                    groupCredentials.groupid!!)}.map{GroupsToUsers.toGroupToUser(it)}
            var user_is_in_group = false

            for (identification in selected_user_group_relations)
            {
                val user = Users.select{Users.id eq identification.user_id}.map{ Users.toUser(it)}[0]
                if(user.username == groupCredentials.username)
                {
                    user_is_in_group = true
                }
                user_list.add(user.username)
            }
            if(user_is_in_group == true)
            {
                return@transaction user_list
            }
            else
            {
                return@transaction ArrayList()
            }
        }

        return return_value
    }

    fun reset() {
        transaction(db) {
            if (GroupsToUsers.exists())
                GroupsToUsers.deleteAll()
            if (Groups.exists())
                Groups.deleteAll()
            if (Users.exists())
                Users.deleteAll()
            if (Bios.exists())
                Bios.deleteAll()
        }
    }



}