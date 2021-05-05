package com.Client.Table

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Client.Table.data.model.RegistrationResponse
import com.Client.Table.network.*
import com.Client.Table.ui.registration.RegistrationModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.mockito.Mock
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.StringBuilder
import java.net.HttpURLConnection

class RegistrationBackendTest {

    lateinit var mockWebServer: MockWebServer

    @Mock
    private var model: RegistrationModel? = null

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
        model = RegistrationModel()
    }
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testAPI()
    {
        val response = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody("{\"id\":1}")
        mockWebServer.enqueue(response)
        println(mockWebServer.hostName + " " + mockWebServer.port + " " + mockWebServer.requestCount)
        val actual_response: RegistrationResponse?
        runBlocking {
            actual_response = model?.register("test", "test@test.at", "pw")
        }
        println("count " + mockWebServer.requestCount)
        assert(actual_response != null && actual_response.id == 1)
    }
}