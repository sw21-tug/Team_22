package com.Client.Table.ui.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.MainViewActivity
import com.Client.Table.R
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Test

class LoginActivityLanguageTest{
    @Test
    fun  main_activity_displayed_after_login(){

        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.spinner)).perform(click())
        Espresso.onData(CoreMatchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.btnLanguage)).perform((click()))

        onView(withId(R.id.login)).check(matches(withText("Войти")))

        onView(withId(R.id.username)).perform(typeText("username"))
        onView(withId(R.id.password)).perform(typeText("password"))
        onView(withId(R.id.login)).perform(click())
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))

    }
}
