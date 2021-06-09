package com.Table.Server.DatabaseObjects

import com.Table.Server.DatabaseObjects.Users.autoIncrement
import com.Table.Server.DatabaseObjects.Users.nullable
import com.Table.Server.DatabaseObjects.Users.references
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

data class GroupToUserRelation(val id: Int?=null, val group_id:Int, val user_id:Int)

object GroupsToUsers: Table() {
    val relation_id: Column<Int> = GroupsToUsers.integer("group_id").autoIncrement()
    val group_id = (integer("grp_id") references Groups.group_id)
    val user_id = (integer("usr_id") references Users.id)

    override val primaryKey = PrimaryKey(relation_id, name = "Rel_ID")

    fun toGroupToUser(row: ResultRow): GroupToUserRelation = GroupToUserRelation(
        id = row[GroupsToUsers.relation_id],
        group_id = row[GroupsToUsers.group_id],
        user_id = row[GroupsToUsers.user_id]
        )

}