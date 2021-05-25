package com.Client.Table

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Client.Table.data.LoginRepository
import com.Client.Table.network.*
import com.Client.Table.ui.bio.BioViewModel
import com.Client.Table.ui.login.LoggedInUserView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
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
        mockWebServer.start(8190)
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(StringBuilder("http://" + mockWebServer.hostName + ":" + mockWebServer.port.toString()).toString())
            .build()

        if (bioViewModel == null) {
            LoginRepository.user = LoggedInUserView("Mario", "1")
            val response = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                "{\"id\": 1, \"user_name\": \"Mario\", \"age\": 20," +
                        " \"city\": \"Graz\", \"card_games\": true, \"board_games\": false, \"ttrpg\": false, \"wargames\": true}"
            )
            mockWebServer.enqueue(response)
            bioViewModel = BioViewModel()
        }
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
        var res: Boolean = bioViewModel!!.setData("mario", 21, "Graz",null, true, false, true, false)

        assert(res)
    }

    @Test
    fun testGetBioApiCall(){
        assert(bioViewModel!!.bio_id.value == 1)
        assert(bioViewModel!!.bio_username.value == "Mario")
        assert(bioViewModel!!.bio_age.value == 20)
        assert(bioViewModel!!.bio_city.value == "Graz")
        assert(bioViewModel!!.bio_card_games.value == true)
        assert(bioViewModel!!.bio_board_games.value == false)
        assert(bioViewModel!!.bio_ttrpg.value == false)
        assert(bioViewModel!!.bio_wargames.value == true)
    }
}