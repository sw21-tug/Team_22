package com.Client.Table.data.model

data class Group (
    val GroupName: String,
    val fetchedUserList : MutableList<FetchedUser>
)