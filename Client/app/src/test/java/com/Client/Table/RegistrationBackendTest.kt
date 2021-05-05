package com.Client.Table



import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.Client.Table.R
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import java.net.HttpURLConnection


class RegistrationBackendTest {

    lateinit var mockWebServer: MockWebServer
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testAPI()
    {
        val response = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(" ")
        mockWebServer.enqueue(response)
        val actual_response = mockServerVariable.request(" ")
        assert(actual_response.code.matches(HttpURLConnection.HTTP_OK))

    }




}