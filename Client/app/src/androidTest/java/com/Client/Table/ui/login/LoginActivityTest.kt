package com.Client.Table.ui.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.Client.Table.R
import org.junit.Test

class RegistrationActivityTest {
    @Test
    fun testRegisterButton() {
        val register = ActivityScenario.launch(LoginActivity::class.java)
        closeSoftKeyboard()
        onView(withId(R.id.register)).perform(click())
    }
}
