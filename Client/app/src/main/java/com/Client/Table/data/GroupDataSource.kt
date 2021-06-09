package com.Client.Table.data

import com.Client.Table.data.model.*
import com.Client.Table.network.BackendApi
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Member

class GroupDataSource {
    fun getGroup(groupname:String, id: Int):Result<Group> {
        var group_credentials = GroupCredentials(LoginRepository.user!!.displayName, groupname, id)
        var result:Result<Group>
        runBlocking {
            try {
                val response: UserListResponse =
                    BackendApi.retrofitService.getUserList(LoginRepository.user!!.jwtToken, group_credentials)
                val ret = Group(groupname, ArrayList(response.list.map {FetchedUser(it)}))
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
                    result = Result.Success(Group(groupname,list))
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
                    result = Result.Success(Group(groupname,list))
                }
                result = Result.Success(ret)
            }
            catch (e: Exception) {
                result = Result.Error(IOException("Error getting group", e))
            }
        }
        return  result
    }
    fun addMemberToGroup(grpname:String, id:Int, member: String):Result<String>{
        var group_credentials = GroupCredentials(member, grpname, id)
        var result : Result<String> = Result.Success("Success")
        runBlocking {
            try {
                val response: Response =
                    BackendApi.retrofitService.addMemberToGroup(LoginRepository.user!!.jwtToken, group_credentials)
                result = Result.Success("Success")
            }
            catch (e: Exception) {
                result = Result.Error(IOException("Error adding Member to group", e))
            }
        }
        return result
    }


    fun deleteMemberFromGroup(grpname:String, id:Int, member: String):Result<String>{
        var group_credentials = GroupCredentials(member, grpname, id)
        var result : Result<String> = Result.Success("Success")
        runBlocking {
            try {
                val response: Response =
                    BackendApi.retrofitService.deleteMemberFromGroup(LoginRepository.user!!.jwtToken, group_credentials)
                result = Result.Success("Success")
            }
            catch (e: Exception) {
                result = Result.Error(IOException("Error adding Member to group", e))
            }
        }
        return result
    }

    fun fetchUserGroups(username:String):Result<MutableList<Pair<Int, String>>>{
            var demoList: MutableList<Pair<Int, String>> = ArrayList()
            var result: Result<MutableList<Pair<Int, String>>> = Result.Success(demoList)
            runBlocking {
                try {
                    val groups: MutableList<Pair<Int, String>> =
                        //Moshi adapter for pair or hashmaps?
                        ArrayList(BackendApi.retrofitService.getGroupList(LoginRepository.user!!.jwtToken).list.map { Pair(it.key.toInt(), it.value) })
                    result = Result.Success(groups)

                } catch (e: Exception) {
                    result = Result.Error(IOException("Error finding group", e))
                }
            }

            if (username == "username") {

                demoList.add(Pair(0,"TestGroup1"))
                demoList.add(Pair(0,"TestGroup2"))
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