package com.Table.Server
import com.Table.Server.DatabaseObjects.*
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

    fun reset() {
        transaction(db) {
            if (Users.exists())
                Users.deleteAll()
            if (Bios.exists())
                Bios.deleteAll()
        }
    }

}