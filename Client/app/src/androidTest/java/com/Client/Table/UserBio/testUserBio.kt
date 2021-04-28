package com.Client.Table.UserBio

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import com.Client.Table.ui.bio.EditBioFragment
import org.junit.Test

class testUserBio {

    @Test
    fun testInputFields()
    {
        val scenario = launchFragmentInContainer<EditBioFragment>()
        onView(withId(R.id.bio_username_input)).perform(typeText("tarik"))
        onView(withId(R.id.bio_age_input)).perform(typeText("16"))
        onView(withId(R.id.bio_city_input)).perform(typeText("Vienna"))

        onView(withId(R.id.edit_bio_checkbox_1)).perform(click()).check(matches(isChecked()))
        onView(withId(R.id.edit_bio_checkbox_2)).perform(click()).check(matches(isChecked()))
    }

    @Test
    fun testPicture()
    {

    }

}