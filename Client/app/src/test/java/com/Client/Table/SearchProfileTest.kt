package com.Client.Table

import android.view.KeyEvent
import android.view.View
import androidx.core.content.MimeTypeFilter.matches
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.ui.UiController
import com.google.android.material.slider.Slider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.Description
import java.util.regex.Matcher


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


    // Adapted from
    // https://stackoverflow.com/questions/65390086/androidx-how-to-test-slider-in-ui-tests-espresso
    // Adaptation start:
    @Test
    fun testSlider() {
        onView(withId(R.id.sliderRange)).check(assert(withValue(25)))

        onView(withId(R.id.sliderRange)).perform(setValue(30))

        onView(withId(R.id.sliderRange)).check(assert(withValue(20)))
    }

    fun withValue(expectedValue: Int): ViewMatchers {
        return object : BoundedMatcher<View?, Slider>(Slider::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("expected: $expectedValue")
            }

            override fun matchesSafely(slider: Slider?): Boolean {
                return slider?.value?.toInt() == expectedValue
            }
        }
    }

    fun setValue(value: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Slider value to $value"
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(Slider::class.java)
            }

            override fun perform(uiController: UiController?, view: View) {
                val ageSlider = view as Slider
                ageSlider.value = value.toFloat()
            }
        }
    }
    // Adaptation End

}