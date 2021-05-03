package com.Client.Table.ui.bio

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.Client.Table.R
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import java.io.*
import java.lang.Exception

class EditBioFragmentTest {
    @Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
        val scenario = launchFragmentInContainer<EditBioFragment>()
        onView(withId(R.id.bio_profile_picture_preview)).check(matches(not(isDisplayed())))
        val mockRes = mockGalleryImage()
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(mockRes)
        onView(withId(R.id.bio_upload_profile_picture_button)).perform(click())
        onView(withId(R.id.bio_profile_picture_preview)).check(matches(isDisplayed()))
    }

    /**
     * Mocks an intent of choosing a picture from the image gallery.
     * Reads in the bitmap of a local drawable resource an writes it to a temp file in order to
     *   to be able to then get the Uri from the file and put it into the bundle for the intent
     */
    private fun mockGalleryImage(): Instrumentation.ActivityResult {
        // prepare bundle for passing with the intent
        val bundle = Bundle()
        val parcels = mutableListOf<Parcelable>()
        val resultData = Intent()

        val bitmap = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().context.resources, R.drawable.ic_menu_edit_bio)
        assert(bitmap != null)

        val file = File.createTempFile("profile_picture_l4g_app_tmp", "png")
        try {
            val outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            assert(false)
        }

        val myParcelable = Uri.fromFile(file) as Parcelable
        parcels.add(myParcelable)
        bundle.putParcelableArrayList(Intent.EXTRA_STREAM, ArrayList(parcels))
        resultData.putExtras(bundle)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }
}