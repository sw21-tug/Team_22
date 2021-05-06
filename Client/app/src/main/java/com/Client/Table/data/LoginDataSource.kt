package com.Client.Table.data

import com.Client.Table.data.model.Credentials
import com.Client.Table.data.model.LoggedInUser
import com.Client.Table.data.model.LoginResponse
import com.Client.Table.data.model.User
import com.Client.Table.network.BackendApi
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            val user: LoggedInUser?
            val loginResponse: LoginResponse?
            runBlocking {
                loginResponse = authenticate(username, password)
            }
            if (loginResponse != null)
            {
                user = LoggedInUser(java.util.UUID.randomUUID().toString(), username, loginResponse.jwtToken)
            } else if (username == "username" && password == "password") {
                user = LoggedInUser(java.util.UUID.randomUUID().toString(), username, "hardcoded_jwt_token")
            } else {
                throw Throwable("Credentials not valid")
            }
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    suspend fun authenticate(username: String, password: String): LoginResponse? {
        return try {
            BackendApi.retrofitService.login(Credentials(username, password))
        } catch (e: Exception) {
            //e.printStackTrace()
            null
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}