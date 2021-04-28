package com.Table.Server.DatabaseObjects
import org.jetbrains.exposed.sql.*
import java.util.regex.Pattern

data class User(val id: Int?=null, val username: String, val email:String, val password:String)
data class UserCredentials(val username: String, val password:String)
object Users: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val username: Column<String> = varchar("username", 255)
    val email: Column<String> = varchar("email", 255)
    val passwordHash: Column<String> = varchar("passwordHash", 255)
    val salt: Column<String> = varchar("salt", 255)
    override val primaryKey = PrimaryKey(id, name = "User_ID")

    init {
        index(true, username)
        index(true, email)
    }

    fun toUser(row: ResultRow): User = User(
        id = row[id],
        username = row[username],
        email = row[email],
        password = ""
    )
    //start
    //Taken from https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    fun emailValidator (email:String) = EMAIL_ADDRESS_PATTERN.matcher(email).matches()

    //end

}
