package com.Client.Table.UserBio

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import com.Client.Table.ui.registration.RegistrationActivity
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Test


class testUserBio {

    @Test
    fun testInputFields()
    {
        val activityScenario = ActivityScenario.launch(RegistrationActivity::class.java)
        onView(withId(R.id.name)).perform(typeText("tarik"))
        onView(withId(R.id.age)).perform(typeText("16"))
        onView(withId(R.id.city)).perform(typeText("Vienna"))

        onView(withId(R.id.checkbox_one)).perform(click()).check(matches(isChecked()))
        onView(withId(R.id.checkbox_two)).perform(click()).check(matches(isChecked()))

    }

    @Test
    fun testPicture()
    {

    }

}