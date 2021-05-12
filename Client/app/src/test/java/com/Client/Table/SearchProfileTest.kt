package com.Client.Table

import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test


class SearchProfileTest {

    @Test
    fun TestSearchBar()
    {
        val activityScenario = ActivityScenario.launch(SearchProfile::class.java)
        onView(withId(R.id.profile_search_view)).perform(click())
        onView(withId(R.id.profile_search_view)).perform(
            typeText("Dzemail2912")
        ).perform(pressKey(KeyEvent.KEYCODE_ENTER))
    }

    @Test
    fun TestEmptySearchBar()
    {
      /*  onView(withId(R.id.profile_search_view)).perform(click())
        onView(withId(R.id.profile_search_viwe)).perform(typeText(" ")
        )
        assertFalse(onView(withId(R.id.profile_search_view)).perform(pressKey(KeyEvent.KEYCODE_ENTER)))*/

    }
}