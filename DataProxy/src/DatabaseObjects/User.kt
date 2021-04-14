package com.Table.Server.DatabaseObjects
import org.jetbrains.exposed.sql.*

data class User(val id: Int?=null, val username: String, val email:String,  val password:String?=null)
object Users: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val username: Column<String> = varchar("username", 255)
    val email: Column<String> = varchar("email", 255)
    val password: Column<String> = varchar("password", 255)
    override val primaryKey = PrimaryKey(id, name = "User_ID")

    fun toUser(row: ResultRow): User = User(
        id = row[id],
        username = row[username],
        email = row[email],
        password = row[password]
    )
}
