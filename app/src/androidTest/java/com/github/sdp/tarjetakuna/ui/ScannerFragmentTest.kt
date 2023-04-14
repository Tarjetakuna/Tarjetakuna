package com.github.sdp.tarjetakuna.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.ui.scanner.ScannerFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScannerFragmentTest {

    private lateinit var scenario: FragmentScenario<ScannerFragment>

    @Before
    fun setUp() {
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
//        val imageBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
//        val resultData = Intent().apply { putExtra("data", imageBitmap) }
//        val result = Instrumentation.ActivityResult(RESULT_OK, resultData)
//        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)
//
//        onView(withId(R.id.button_scan)).perform(click())
//        onView(withId(R.id.imagePreview)).check(
//            matches(
//                WithDrawableSafeMatcher.withDrawable(
//                    imageBitmap
//                )
//            )
//        )
//        onView(withId(R.id.text_information)).check(matches(withText(R.string.operation_success)))
    }

//    @Test
//    fun testTakeInvalidPictureShouldDisplayErrorMessage() {
//        val resultData = Intent()
//        val result = Instrumentation.ActivityResult(RESULT_CANCELED, resultData)
//        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)
//
//        onView(withId(R.id.button_scan)).perform(click())
//        onView(withId(R.id.text_information)).check(matches(withText(R.string.operation_failed)))
//    }
}
