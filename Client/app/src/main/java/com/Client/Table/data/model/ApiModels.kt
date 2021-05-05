package com.Client.Table.data.model

data class User(val username: String, val email: String, val password: String)
data class RegistrationResponse(val id: Int)
data class Credentials(val username: String, val password: String)
data class LoginResponse(val jwtToken: String)
data class TestAuthentication(val response: String)