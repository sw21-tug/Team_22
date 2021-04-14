package com.Table.Server
import com.Table.Server.DatabaseObjects.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager


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

    fun insertUser(user: User):Int{
        transaction(db) {
            Users.insert {
                it[Users.username] = user.username
                it[Users.email] = user.email
                it[Users.password] = user.password
            }
        }
        return 0
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

    fun updateUsers(id:Int, name:String?=null, age:Int?=null):Int{
        transaction(db){
            Users.update({Users.id eq id}){
                if(name!=null){ it[Users.username] = name }
            }
        }
        return 0
    }

}