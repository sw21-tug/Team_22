package com.Client.Table

import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.Client.Table.ui.preferenceSearch.SearchPreferenceActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
class SearchProfileTest {

    @Test
    fun TestSearchBar() {
        val activityScenario = ActivityScenario.launch(SearchPreferenceActivity::class.java)
        onView(withId(R.id.bio_city_input)).perform(
            typeText("Graz")
        ).perform(pressKey(KeyEvent.KEYCODE_ENTER))
    }
}


