package com.Client.Table.ui.registration

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import com.Client.Table.ui.login.LoginActivity
import org.hamcrest.CoreMatchers.anything
import org.junit.Test

class RegistrationActivityLanguageTest {
    @Test
    fun testInvalidEmail() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.languageSpinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.btnLanguage)).perform((click()))
        onView(withId(R.id.registerBtn)).perform(click())

        onView(withId(R.id.register_email)).perform(typeText("tarik"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.register_btn)).check(matches(withText(R.string.register_btn)))
        onView(withId(R.id.register_btn)).perform(click())

    }


    @Test
    fun testInvalidUsername() {

        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.languageSpinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.btnLanguage)).perform((click()))
        onView(withId(R.id.registerBtn)).perform(click())

        onView(withId(R.id.register_username)).perform(typeText("ta"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.register_btn)).check(matches(withText(R.string.register_btn)))
        onView(withId(R.id.register_btn)).perform(click())

    }

}






