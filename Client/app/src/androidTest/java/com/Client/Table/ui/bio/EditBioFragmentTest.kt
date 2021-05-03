package com.Client.Table.ui.bio

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import org.junit.Test

class EditBioFragmentTest {

    @Test
    fun testInputFields()
    {
        val scenario = launchFragmentInContainer<EditBioFragment>()
        onView(withId(R.id.bio_username_input)).perform(typeText("tarik"), closeSoftKeyboard())
        onView(withId(R.id.bio_age_input)).perform(typeText("16"), closeSoftKeyboard())
        onView(withId(R.id.bio_city_input)).perform(typeText("Vienna"), closeSoftKeyboard())

        onView(withId(R.id.edit_bio_checkbox_1)).perform(click(), closeSoftKeyboard()).check(matches(isChecked()))
        onView(withId(R.id.edit_bio_checkbox_2)).perform(click(), closeSoftKeyboard()).check(matches(isChecked()))
    }

    @Test
    fun testPicture()
    {

    }

}