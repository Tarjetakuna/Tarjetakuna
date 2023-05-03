package com.github.sdp.tarjetakuna.ui.scanner

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.PermissionGranting.PermissionGranting.grantPermissions
import com.github.sdp.tarjetakuna.utils.waitForText
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        println("after done")
    }

    @Test
    fun test_0_grantPermissions() {
        assert(true)
    }

    @Test
    fun test_1_defaultTextValues() {
        // check that the texts are the default ones
        waitForText(R.id.scanner_description_text, R.string.scanner_description, 100)
        waitForText(R.id.scanner_textInImage_text, R.string.scanner_no_text_detected_yet, 100)
        waitForText(R.id.scanner_objectInImage_text, R.string.scanner_no_object_detected_yet, 100)
        waitForText(R.id.scanner_scan_button, R.string.scanner_button, 100)
    }

    @Test
    fun test_2_clickScan() {
        // wait for view to be displayed
        waitForText(R.id.scanner_description_text, R.string.scanner_description, 100)

        // click on the button
        onView(withId(R.id.scanner_scan_button)).perform(click())

        // find all snackbars and check that the text is the default one
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.scanner_photo_saved)))
    }
}
