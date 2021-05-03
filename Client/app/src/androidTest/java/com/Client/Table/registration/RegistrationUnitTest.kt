package com.Client.Table.registration

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.ui.registration.RegistrationActivity
import com.Client.Table.R
import org.junit.Assert.*
import org.junit.Test

class RegistrationUnitTest {
    @Test
    fun testForRegistration() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.register_email)).perform(typeText("tarik"))
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_email)).check(matches(hasErrorText("Please Enter Valid Email")))

    }
}