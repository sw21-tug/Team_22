package com.Client.Table.ui.preferenceSearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import com.Client.Table.data.LoginRepository
import com.Client.Table.ui.login.LoggedInUserView
import com.google.android.material.slider.RangeSlider
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test


class SearchPreferenceActivityTest {

    /**
     * Helper function for setting the value range of a slider
     */
    private fun setValueRangeSlider(): ViewAction? {
        return object : ViewAction {
            override fun perform(uiController: UiController?, view: View) {
                val slider = view as RangeSlider
                slider.setValues(30.0f, 50.0f)
            }

            override fun getDescription(): String {
                return "Sets the range slider's value to sample values"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(RangeSlider::class.java)
            }
        }
    }

    private fun isRecyclerViewEmpty(): Matcher<View?>? {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("is empty?")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                return recyclerView.childCount == 0
            }
        }
    }

    @Before
    fun setUp() {
        LoginRepository.user = LoggedInUserView("Mario", "1")
    }

    @Test
    fun testInputFields() {
        ActivityScenario.launch(SearchPreferenceActivity::class.java)

        onView(withId(R.id.search_profile_card_games)).perform(click(), closeSoftKeyboard()).check(
            matches(
                isChecked()
            )
        )
        onView(withId(R.id.search_profile_board_games)).perform(click(), closeSoftKeyboard()).check(
            matches(
                isChecked()
            )
        )

        onView((withId(R.id.search_profile_age_slider))).perform(setValueRangeSlider(), closeSoftKeyboard())

        onView(withId(R.id.search_profile_city_input)).perform(typeText("Graz"), closeSoftKeyboard()).check(
            matches(
                withText("Graz")
            )
        )
    }
    @Test
    fun selectedNoCheckboxes() {
        ActivityScenario.launch(SearchPreferenceActivity::class.java)

        onView((withId(R.id.search_profile_age_slider))).perform(setValueRangeSlider(), closeSoftKeyboard())

        onView(withId(R.id.search_profile_city_input)).perform(typeText("Graz"), closeSoftKeyboard()).check(
                matches(
                        withText("Graz")
                )
        )
        onView((withId(R.id.search_profile_submit_btn))).perform(click())

        onView(withId(R.id.search_profile_error_message)).check(matches(isDisplayed()))
    }

    @Test
    fun listNotShownUntilClicked() {
        ActivityScenario.launch(SearchPreferenceActivity::class.java)
        onView(withId(R.id.search_preference_list))
            .check(matches(isRecyclerViewEmpty()))
    }

    @Test
    fun listEmptyAfterSubmitClicked() {
        ActivityScenario.launch(SearchPreferenceActivity::class.java)

        onView(withId(R.id.search_profile_board_games)).perform(click(), closeSoftKeyboard()).check(
            matches(
                isChecked()
            )
        )

        onView((withId(R.id.search_profile_age_slider))).perform(setValueRangeSlider(), closeSoftKeyboard())

        onView(withId(R.id.search_profile_city_input)).perform(typeText("Graz"), closeSoftKeyboard()).check(
            matches(
                withText("Graz")
            )
        )

        onView((withId(R.id.search_profile_submit_btn))).perform(click())

        onView(withId(R.id.search_preference_list))
            .check(matches((isRecyclerViewEmpty())))
    }
    @Test
    fun testRemainingInputFields() {
        ActivityScenario.launch(SearchPreferenceActivity::class.java)

        onView(withId(R.id.search_profile_war_games)).perform(click(), closeSoftKeyboard()).check(
                matches(
                        isChecked()
                )
        )
        onView(withId(R.id.search_profile_ttrpgs)).perform(click(), closeSoftKeyboard()).check(
                matches(
                        isChecked()
                )
        )
        onView(withId(R.id.search_profile_city_input)).perform(typeText("Vienna"), closeSoftKeyboard()).check(
                matches(
                        withText("Vienna")
                )
        )
    }

}
