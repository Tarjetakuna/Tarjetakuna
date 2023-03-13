package com.github.bjolidon.bootcamp.ui

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.bjolidon.bootcamp.ui.scanner.ScannerFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.github.bjolidon.bootcamp.R
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After

@RunWith(AndroidJUnit4::class)
class ScannerFragmentTest {

    private lateinit var scenario: FragmentScenario<ScannerFragment>

    @Before
    fun setUp() {
        Intents.init()
        scenario = launchFragmentInContainer()
    }

    @After
    fun after() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun testTakeValidPictureShouldDisplaySuccessMessageAndImage() {
        val imageBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val resultData = Intent().apply { putExtra("data", imageBitmap) }
        val result = Instrumentation.ActivityResult(RESULT_OK, resultData)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

        onView(withId(R.id.button_scan)).perform(click())
        onView(withId(R.id.image_card)).check(matches(withDrawable(imageBitmap)))
        onView(withId(R.id.text_information)).check(matches(withText(R.string.operation_success)))
    }

    @Test
    fun testTakeInvalidPictureShouldDisplayErrorMessage() {
        val resultData = Intent()
        val result = Instrumentation.ActivityResult(RESULT_CANCELED, resultData)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

        onView(withId(R.id.button_scan)).perform(click())
        onView(withId(R.id.text_information)).check(matches(withText(R.string.operation_failed)))
    }

    private fun withDrawable(bitmap: Bitmap) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with drawable same as a bitmap")
        }

        override fun matchesSafely(view: View): Boolean {
            return view is ImageView && view.drawable.toBitmap().sameAs(bitmap)
        }
    }
}
