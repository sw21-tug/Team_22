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
        }
        print("Created Database\n")
    }

    fun insertUser(user: User) : Int {
        val salt = BCrypt.gensalt()
        return transaction(db) {
            Users.insert {
                it[username] = user.username
                it[email] = user.email
                it[passwordHash] = BCrypt.hashpw(user.password, salt)
                it[Users.salt] = salt
            }[Users.id]
        }
    }
    fun getUserById(id:Int):List<User>{
        val users = transaction(db){
            Users.select{Users.id eq id}.map{ Users.toUser(it)}
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
        }
    }

}