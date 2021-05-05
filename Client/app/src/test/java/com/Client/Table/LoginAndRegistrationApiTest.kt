package com.Client.Table

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Client.Table.data.LoginDataSource
import com.Client.Table.data.LoginRepository
import com.Client.Table.data.model.RegistrationResponse
import com.Client.Table.network.*
import com.Client.Table.ui.login.LoginViewModel
import com.Client.Table.ui.registration.RegistrationModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.mockito.Mock
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.StringBuilder
import java.net.HttpURLConnection

class LoginAndRegistrationApiTest {

    lateinit var mockWebServer: MockWebServer

    @Mock
    private var registrationModel: RegistrationModel? = null
    @Mock
    private var loginModel: LoginViewModel? = null

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
        registrationModel = RegistrationModel()
        loginModel = LoginViewModel(LoginRepository(LoginDataSource()))
    }
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testValidRegistration()
    {
        val response = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody("{\"id\":1}")
        mockWebServer.enqueue(response)
        val actual_response: RegistrationResponse?
        runBlocking {
            actual_response = registrationModel?.register("test", "test@test.at", "pw")
        }
        assert(actual_response != null && actual_response.id == 1)
    }

    @Test
    fun testInvalidLogin()
    {
        val response = MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND).setBody("")
        mockWebServer.enqueue(response)
        runBlocking {
            loginModel?.login("username_invalid", "password_invalid")
        }
        assert(loginModel?.loginResult?.value?.error != null && loginModel?.loginResult?.value?.success == null)
    }

    @Test
    fun testValidLogin()
    {
        val response = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody("{\"jwtToken\":\"my_test_token\"}")
        mockWebServer.enqueue(response)
        runBlocking {
            loginModel?.login("username", "password")
        }
        assert(loginModel?.loginResult?.value?.error == null && loginModel?.loginResult?.value?.success != null)
    }
}