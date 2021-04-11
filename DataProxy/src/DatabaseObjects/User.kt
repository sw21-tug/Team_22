package com.Table.Server.DatabaseObjects
import org.jetbrains.exposed.sql.*

data class User(val id: Int?=null, val username: String, val age: Int)
object Users: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val username: Column<String> = varchar("username", 255)
    val age: Column<Int> = integer("age")

    override val primaryKey = PrimaryKey(id, name = "User_ID")

    fun toUser(row: ResultRow): User = User(
        id = row[id],
        username = row[username],
        age = row[age]
    )
}
