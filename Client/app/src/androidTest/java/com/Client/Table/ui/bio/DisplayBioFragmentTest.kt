package com.Client.Table.ui.bio

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.Client.Table.R
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DisplayBioFragmentTest {
    @Test
    fun testDisplayingWhenModelEmpty()
    {
        launchFragmentInContainer<DisplayBioFragment>()
        // at least these 3 should be also displayed; checkboxes might be out of view and need scrolling
        onView(withId(R.id.bio_username_display)).check(matches(isDisplayed())).check(matches(withText(" ")))
        onView(withId(R.id.bio_age_display)).check(matches(isDisplayed())).check(matches(withText(" ")))
        onView(withId(R.id.bio_city_display)).check(matches(isDisplayed())).check(matches(withText(" ")))

        onView(withId(R.id.display_bio_preference_1)).check(matches(not(isDisplayed())))
        onView(withId(R.id.display_bio_preference_2)).check(matches(not(isDisplayed())))
        onView(withId(R.id.display_bio_preference_3)).check(matches(not(isDisplayed())))
        onView(withId(R.id.display_bio_preference_4)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testEditProfileButton()
    {
        // ** Start **
        // Adapted from https://developer.android.com/guide/navigation/navigation-testing
        val navController = TestNavHostController(
                ApplicationProvider.getApplicationContext())

        val scenario = launchFragmentInContainer<DisplayBioFragment> {
            DisplayBioFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.mobile_navigation)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                        navController.setCurrentDestination(R.id.nav_display_bio)
                    }
                }
            }
        }
        // ** End **
        onView(withId(R.id.display_bio_header)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_profile_button)).perform(scrollTo(), click())
        navController.currentDestination?.id?.equals(R.id.nav_edit_bio)?.let { assert(it) }

    }
}