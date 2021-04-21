package com.Table.Server
import com.Table.Server.DatabaseObjects.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transactionManager


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
        return transaction(db) {
            Users.insert {
                it[username] = user.username
                it[email] = user.email
                it[password] = user.password
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