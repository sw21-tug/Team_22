package com.Client.Table.data

import com.Client.Table.data.model.FetchedUser
import com.Client.Table.data.model.Group
import java.io.IOException

class GroupDataSource {
    fun getGroup(groupname:String):Result<Group> {
        try {
            val list: MutableList<FetchedUser> = ArrayList()
            if (groupname == "TestGroup") {
                val user1: FetchedUser = FetchedUser("User1")
                val user2: FetchedUser = FetchedUser("User2")
                val user3: FetchedUser = FetchedUser("User3")
                val user4: FetchedUser = FetchedUser("User4")
                list.add(user1)
                list.add(user2)
                list.add(user3)
                list.add(user4)
            }
            return Result.Success(Group(groupname,list))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error finding group", e))
        }
    }
}