package com.github.sdp.tarjetakuna.ui

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
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
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.ui.scanner.ScannerFragment
import com.github.sdp.tarjetakuna.utils.Utils
import com.github.sdp.tarjetakuna.utils.WithDrawableSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScannerFragmentTest {

    private lateinit var scenario: FragmentScenario<ScannerFragment>

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()

        Intents.init()
        scenario = launchFragmentInContainer()
        Thread.sleep(100) // Wait for the fragment to be created (otherwise the test fails because there is no view)
        //TODO : Find a better way to wait for the activity to be created
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

        onView(withId(R.id.scanner_scan_button)).perform(click())
        onView(withId(R.id.scanner_image)).check(
            matches(
                WithDrawableSafeMatcher.withDrawable(
                    imageBitmap
                )
            )
        )
        onView(withId(R.id.scanner_information_text)).check(matches(withText(R.string.operation_success)))
    }

    @Test
    fun testTakeInvalidPictureShouldDisplayErrorMessage() {
        val resultData = Intent()
        val result = Instrumentation.ActivityResult(RESULT_CANCELED, resultData)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

        onView(withId(R.id.scanner_scan_button)).perform(click())
        onView(withId(R.id.scanner_information_text)).check(matches(withText(R.string.operation_failed)))
    }
}
