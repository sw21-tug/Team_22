package com.Client.Table.ui.registration

import com.Client.Table.data.model.RegistrationResponse
import com.Client.Table.data.model.User
import com.Client.Table.network.BackendApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class RegistrationModel {
    suspend fun register(username: String, email: String, password: String): RegistrationResponse?
    {
        return try {
            BackendApi.retrofitService.register(User(username, email, password))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}