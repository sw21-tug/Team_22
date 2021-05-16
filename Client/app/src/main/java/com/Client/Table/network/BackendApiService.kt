package com.Client.Table.network

import com.Client.Table.data.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val BASE_URL = HttpUrl.get("http://10.0.2.2:8080/")

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

// public var such that unit tests can inject their mocked webserver
public var retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()


interface BackendApiService {
    @POST("/user/register")
    suspend fun register(@Body user: User): RegistrationResponse

    @POST("/user/login")
    suspend fun login(@Body credentials: Credentials): LoginResponse

    @GET("/testauthentication/")
    suspend fun testauthentication(@Header("Authorization") authToken: String) : Response

    @POST("/user/updateBio")
    suspend fun updatebio(@Header("Authorization")authToken: String,@Body bio :Bio): Response

}

object BackendApi {
    val retrofitService: BackendApiService by lazy {
        println(retrofit.baseUrl())
        retrofit.create(BackendApiService::class.java)
    }
}