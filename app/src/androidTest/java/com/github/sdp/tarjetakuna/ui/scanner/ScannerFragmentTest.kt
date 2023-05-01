package com.github.sdp.tarjetakuna.ui.scanner

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.PermissionGranting.PermissionGranting.grantPermissions
import com.github.sdp.tarjetakuna.utils.getStringInTest
import com.github.sdp.tarjetakuna.utils.waitForMatcher
import com.github.sdp.tarjetakuna.utils.waitForText
import org.hamcrest.CoreMatchers.startsWith
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScannerFragmentTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        grantPermissions()
        activityScenarioRule.scenario.onActivity {
            it.changeFragment(R.id.nav_scanner)
        }
    }

    @After
    fun after() {
        Intents.release()
    }

    @Test
    fun test_defaultTextValues() {
        // check that the texts are the default ones
        waitForText(R.id.text_description, R.string.scanner_description, 100)
        waitForText(R.id.textInImage, R.string.scanner_no_text_detected_yet, 100)
        waitForText(R.id.objectInImage, R.string.scanner_no_object_detected_yet, 100)
        waitForText(R.id.button_scan, R.string.scanner_button, 100)
    }

    @Test
    fun test_clickScan() {
        // wait for view to be displayed
        waitForText(R.id.text_description, R.string.scanner_description, 100)

        // click on the button
        onView(withId(R.id.button_scan)).perform(click())

        // check that the image is saved
        val msg = getStringInTest(R.string.scanner_photo_saved, "")
        waitForMatcher(R.id.hidden_text, withText(startsWith(msg)), 1000)
    }
}
