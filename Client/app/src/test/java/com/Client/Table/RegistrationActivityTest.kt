package com.Client.Table

import com.Client.Table.ui.registration.RegistrationActivity
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
import org.junit.Test
import androidx.test.espresso.action.ViewActions.typeText


class RegistrationActivityTest {

    @Test
    fun testInvalidUsername() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.register_username)).perform(typeText("ba"))
        closeSoftKeyboard()
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_username)).check(matches(hasErrorText("Username Length must be more than 4 characters")))
    }

    @Test
    fun testInvalidPassword() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.register_password)).perform(typeText("39"))
        closeSoftKeyboard()
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_password)).check(matches(hasErrorText("Password Length must be more than 6 characters")))
    }

}

class RegistrationActivityTestTwo {
    @Test
    fun testEmptyForm() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_username)).check(matches(hasErrorText("Username Length must be more than 4 characters")))
        onView(withId(R.id.register_password)).check(matches(hasErrorText("Password Length must be more than 6 characters")))
    }
}