package com.Client.Table.data.model

data class User(val username: String, val email: String, val password: String)
data class RegistrationResponse(val id: Int)
data class Credentials(val username: String, val password: String)
data class LoginResponse(val jwtToken: String)
data class Response(val response: String)
data class Bio(val id: Int?=null, val user_name:String?=null, val age:Int?=null, val city:String?=null, val card_games:Boolean?=false , val board_games:Boolean?=false, val ttrpg:Boolean?=false, val wargames:Boolean?=false)
data class GroupCredentials(val username:String?=null, val groupname:String?=null, val groupid:Int?=null)
data class GroupListResponse(val list: Map<String,String>)
data class UserListResponse(val list: List<String>)