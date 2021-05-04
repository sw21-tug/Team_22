package com.Client.Table.ui.bio

import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import com.Client.Table.R
import org.hamcrest.Matchers.*
import org.junit.Test
import java.io.*
import java.lang.Exception

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

    /**
     * Tests whether the user can choose a profile picture from his gallery and whether this image
     *   then gets displayed as a preview.
     * The profile picture gets mocked using a local drawable resource using mockGalleryImage()
     * The image view is supposed to be not shown unless an image has been chosen from the gallery
     */
    @Test
    fun testUserPictureCanBeChosen()
    {
        Intents.init()
        val scenario = launchFragmentInContainer<EditBioFragment>()
        // in the beginning, no image is added to the image view
        onView(withId(R.id.profile_picture)).check(matches(not(withTagValue(equalTo(EditBioFragment.TAG_IMAGE_ADDED)))))
        val mockRes = mockGalleryImage()
        // stub the intend accordingly
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(mockRes)
        onView(withId(R.id.edit_picture_button)).perform(click())
        // now the image should have been set and tag accordingly changed
        onView(withId(R.id.profile_picture)).check(matches(withTagValue(equalTo(EditBioFragment.TAG_IMAGE_ADDED))))
        Intents.release()
    }

    /**
     * Mocks an intent of choosing a picture from the image gallery.
     * Reads in the bitmap of a local drawable resource an writes it to a temp file in order to
     *   to be able to then get the Uri from the file and put it into the bundle for the intent
     */
    private fun mockGalleryImage(): Instrumentation.ActivityResult {
        val intent = Intent()

        // get a local drawable resource file and retrieve the bitmap
        val context = ApplicationProvider.getApplicationContext<Application>()
        val res = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_menu_edit_bio, null)
        val bitmap = res?.toBitmap()
        assert(bitmap != null)

        // create temp file and write bitmap into it
        val file = File.createTempFile("profile_picture_l4g_app_tmp", ".png")
        try {
            val outStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 80, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            assert(false)
        }

        // set the data Uri of the intent to the Uri of the created temp file
        intent.data = Uri.fromFile(file)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
    }
    @Test
    fun checkEditedFields()
    {
        launchFragmentInContainer<EditBioFragment>()
        val context = ApplicationProvider.getApplicationContext<Application>()

        onView(withId(R.id.bio_username_input)).perform(typeText(" "), closeSoftKeyboard())
        onView(withId(R.id.bio_age_input)).perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.bio_city_input)).perform(typeText("Sarajevo"), closeSoftKeyboard())

        onView(withId(R.id.edit_bio_submit)).perform(scrollTo(), click())

        onView(withId(R.id.bio_age_input)).check(matches(hasErrorText(context.resources.getString(R.string.bio_error_message_age_range))))
        onView(withId(R.id.bio_username_input)).check(matches(hasErrorText(context.resources.getString(R.string.bio_error_message_empty_username))))


    }
}