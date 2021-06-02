package com.Client.Table.data

import com.Client.Table.data.model.*
import com.Client.Table.network.BackendApi
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Member

class GroupDataSource {
    fun getGroup(groupname:String):Result<Group> {
        try {
            val list: MutableList<FetchedUser> = ArrayList()
            if (groupname == "TestGroup1") {
                val user1: FetchedUser = FetchedUser("User1")
                val user2: FetchedUser = FetchedUser("User2")
                val user3: FetchedUser = FetchedUser("User3")
                val user4: FetchedUser = FetchedUser("User4")
                list.add(user1)
                list.add(user2)
                list.add(user3)
                list.add(user4)
            }
            if (groupname == "TestGroup2") {
                val user1: FetchedUser = FetchedUser("User5")
                val user2: FetchedUser = FetchedUser("User6")
                val user3: FetchedUser = FetchedUser("User7")
                val user4: FetchedUser = FetchedUser("User8")
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
    fun addMemberToGroup(groupname:String, member: String):Result<FetchedUser>{
        return Result.Success(FetchedUser(member))
    }
    fun fetchUserGroups(username:String):Result<MutableList<String>>{
            var demoList: MutableList<String> = ArrayList()
            var result: Result<MutableList<String>> = Result.Success(demoList)
            runBlocking {
                try {
                    val groups: MutableList<String> =
                        BackendApi.retrofitService.getGroupList(LoginRepository.user!!.jwtToken)
                    result = Result.Success(groups)

                } catch (e: Exception) {
                    result = Result.Error(IOException("Error finding group", e))
                }
            }

            if (username == "username") {

                demoList.add("TestGroup1")
                demoList.add("TestGroup2")
                result = Result.Success(demoList)

            }
            return result
        }

    fun createGroup(groupname: String, username: String) : Result<String> {
       var group_credentials = GroupCredentials(username, groupname, null)
        var result : Result<String> = Result.Success("Success")
        runBlocking {
            try {
                val response: Response =
                        BackendApi.retrofitService.createGroup(LoginRepository.user!!.jwtToken, group_credentials)
                result = Result.Success("Success")
            }
            catch (e: Exception) {
                result = Result.Error(IOException("Error creating group", e))
            }
        }

        return result
    }
}