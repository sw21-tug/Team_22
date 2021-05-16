package com.Client.Table

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
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

        val response = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody("{\"bio_id\": \"1\", \"bio_username\": \"Mario\", \"bio_age\": \"20\"," +
                " \"bio_city\": \"Graz\", \"bio_card_games\": \"true\", \"bio_board_games\": \"false\", \"bio_ttrpg\": \"false\", \"bio_wargames\": \"true\"}")
        mockWebServer.enqueue(response)
        bioViewModel = BioViewModel()
        LoginRepository.user = LoggedInUserView("Mario","1")
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

    @Test
    fun testGetBioApiCall(){
        assert(bioViewModel!!.bio_id.toString() == "1")
        assert(bioViewModel!!.bio_username.toString() == "Mario")
        assert(bioViewModel!!.bio_age.toString() == "20")
        assert(bioViewModel!!.bio_city.toString() == "Graz")
        assert(bioViewModel!!.bio_card_games.toString() == "true")
        assert(bioViewModel!!.bio_board_games.toString() == "false")
        assert(bioViewModel!!.bio_ttrpg.toString() == "false")
        assert(bioViewModel!!.bio_wargames.toString() == "true")
        
    }
}