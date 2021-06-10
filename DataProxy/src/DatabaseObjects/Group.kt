package com.Table.Server.DatabaseObjects

import com.Table.Server.DatabaseObjects.Users.autoIncrement
import com.Table.Server.DatabaseObjects.Users.nullable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

data class Group(val id: Int?=null, val group_name:String, val group_admin: String)

object Groups: Table() {
    val group_id: Column<Int> = Groups.integer("group_id").autoIncrement()
    val group_name: Column<String> = Groups.varchar("group_name",255) // testcases for length > SET_MAXIMUM
    val group_admin: Column<String> = Groups.varchar("group_admin",255)

    override val primaryKey = PrimaryKey(group_id, name = "Group_ID")

    fun toGroup(row: ResultRow): Group = Group(
        id = row[Groups.group_id],
        group_name = row[Groups.group_name],
        group_admin = row[Groups.group_admin]
        )

}