package com.Client.Table.ui.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import org.junit.Test

class LoginActivityTest{
    @Test
    fun  main_activity_displayed_after_login(){
        //probably needs to be rewritten at some point
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.username)).perform(typeText("username"))
        onView(withId(R.id.password)).perform(typeText("password"))
        onView(withId(R.id.signinBtn)).perform(click())
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }
    
    @Test
    fun  register_activity_displayed_after_button(){
        //probably needs to be rewritten at some point
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.registerBtn)).perform(click())
        onView(withId(R.id.password_repeat)).check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.signinBtn)).check(matches(isDisplayed()))
    }
    @Test
    fun  login_then_logout(){
        //probably needs to be rewritten at some point
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.username)).perform(typeText("username"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(typeText("password"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signinBtn)).perform(click())
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.signinBtn)).check(matches(isDisplayed()))
    }
}