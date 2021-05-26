package com.Client.Table.ui.preferenceSearch

import android.view.View
import android.widget.SeekBar
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import com.Client.Table.data.LoginRepository
import com.Client.Table.ui.login.LoggedInUserView
import com.google.android.material.slider.RangeSlider
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import java.io.*


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

        onView((withId(R.id.search_profile_age_slider))).perform(setValueRangeSlider())

        onView(withId(R.id.search_city_input)).perform(typeText("Graz"), closeSoftKeyboard()).check(
            matches(
                withText("Graz")
            )
        )
    }
}