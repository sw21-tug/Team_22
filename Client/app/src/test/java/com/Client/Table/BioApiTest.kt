package com.Client.Table

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Client.Table.data.LoginDataSource
import com.Client.Table.data.LoginRepository
import com.Client.Table.data.model.RegistrationResponse
import com.Client.Table.network.retrofit
import com.Client.Table.ui.bio.BioViewModel
import com.Client.Table.ui.login.LoggedInUserView
import com.Client.Table.ui.login.LoginViewModel
import com.Client.Table.ui.registration.RegistrationModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.StringBuilder
import java.net.HttpURLConnection

class BioApiTest {
    lateinit var mockWebServer: MockWebServer

    @Mock
    private var bioViewModel: BioViewModel? = null

    // JSON converter used for the retrofit
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Before
    fun setUp() {
        // start the mocked web server and inject the edited retrofit server to the mocked one
        mockWebServer = MockWebServer()
        mockWebServer.start()
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(StringBuilder("http://" + mockWebServer.hostName + ":" + mockWebServer.port.toString()).toString())
            .build()
        bioViewModel = BioViewModel()
        LoginRepository.user = LoggedInUserView("Mario","1234567")
    }
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testUpdateBioApiCall()
    {
        val response = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody("{\"response\":\"success\"}")
        mockWebServer.enqueue(response)
        var res: Boolean = bioViewModel!!.setData("mario", 21, "Graz", true, false, true, false)

        assert(res)
    }

}