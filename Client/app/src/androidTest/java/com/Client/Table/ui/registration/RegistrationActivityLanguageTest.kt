package com.Client.Table.ui.registration

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import com.Client.Table.ui.login.LoginActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anything
import org.junit.Test
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard as testEspressoActionViewActionsCloseSoftKeyboard

class RegistrationActivityLanguageTest {
    @Test
    fun testInvalidEmail() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.btnLanguage)).perform((click()))
        onView(withId(R.id.register)).perform(click())

        onView(withId(R.id.register_email)).perform(typeText("tarik"))
        closeSoftKeyboard()
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_email)).check(matches(hasErrorText("Пожалуйста, введите действительный адрес электронной почты")))
    }

    @Test
    fun testInvalidUsername() {

        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.btnLanguage)).perform((click()))
        onView(withId(R.id.register)).perform(click())

        onView(withId(R.id.register_username)).perform(typeText("ta"))
        closeSoftKeyboard()
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_username)).check(matches(hasErrorText("Длина имени пользователя должна быть более 4 символов")))
    }

}





