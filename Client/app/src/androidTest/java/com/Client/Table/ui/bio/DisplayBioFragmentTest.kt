package com.Client.Table.ui.bio

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class DisplayBioFragmentTest {
    @Test
    fun testDisplayingWhenModelEmpty()
    {
        launchFragmentInContainer<DisplayBioFragment>()
        // at least these 3 should be also displayed; checkboxes might be out of view and need scrolling
        onView(withId(R.id.bio_username_display)).check(matches(isDisplayed())).check(matches(withText(" ")))
        onView(withId(R.id.bio_age_display)).check(matches(isDisplayed())).check(matches(withText(" ")))
        onView(withId(R.id.bio_city_display)).check(matches(isDisplayed())).check(matches(withText(" ")))

        onView(withId(R.id.display_bio_checkbox_1)).check(matches(not(isChecked())))
        onView(withId(R.id.display_bio_checkbox_2)).check(matches(not(isChecked())))
        onView(withId(R.id.display_bio_checkbox_3)).check(matches(not(isChecked())))
        onView(withId(R.id.display_bio_checkbox_4)).check(matches(not(isChecked())))
    }
}