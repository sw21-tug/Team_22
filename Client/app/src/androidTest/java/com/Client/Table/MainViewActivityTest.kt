package com.Client.Table

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class MainViewActivityTest {

    @Test
    fun does_main_view_activity_launch(){
        val activityScenario = ActivityScenario.launch(MainViewActivity::class.java)
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }
    @Test
    fun is_home_fragment_visible_on_start(){
        val activityScenario = ActivityScenario.launch(MainViewActivity::class.java)
        onView(withId(R.id.text_home)).check(matches(isDisplayed()))


        //more general with possibility to check for invisible or gone
        //onView(withId(R.id.text_home)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        //compare text | Maybe useful for testing value passes to activity
        //onView(withId(R.id.text_home)).check(matches(withText("String to Compare")))
    }


}