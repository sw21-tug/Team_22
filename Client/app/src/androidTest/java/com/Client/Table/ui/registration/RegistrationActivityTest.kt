package com.Client.Table.ui.registration

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

class RegistrationActivityTest {
    @Test
    fun testInvalidEmail() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.register_email)).perform(typeText("tarik"))
        closeSoftKeyboard()
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_email)).check(matches(hasErrorText("Please Enter Valid Email")))
    }

    @Test
    fun testInvalidUsername() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.register_username)).perform(typeText("ta"))
        closeSoftKeyboard()
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_username)).check(matches(hasErrorText("Username Length must be more than 4 characters")))
    }

    @Test
    fun testInvalidPassword() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.register_password)).perform(typeText("30"))
        closeSoftKeyboard()
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_password)).check(matches(hasErrorText("Password Length must be more than 6 characters")))
    }

    @Test
    fun testInvalidRepeatPassword() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.password_repeat)).perform(typeText("3006"))
        closeSoftKeyboard()
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.password_repeat)).check(matches(hasErrorText("Password does not match")))
    }
}

class RegistrationActivityTestTwo {
    @Test
    fun testEmptyForm() {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_email)).check(matches(hasErrorText("Please Enter Valid Email")))
        onView(withId(R.id.register_username)).check(matches(hasErrorText("Username Length must be more than 4 characters")))
        onView(withId(R.id.register_password)).check(matches(hasErrorText("Password Length must be more than 6 characters")))
        onView(withId(R.id.password_repeat)).check(matches(hasErrorText("Please Enter Repeat Password")))
    }
}



