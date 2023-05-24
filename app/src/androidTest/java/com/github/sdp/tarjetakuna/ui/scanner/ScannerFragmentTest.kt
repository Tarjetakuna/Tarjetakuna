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

    @Test(timeout = 5000)
    fun test_0_grantPermissions() {
        assert(true)
    }

    @Test(timeout = 5000)
    fun test_1_defaultTextValues() {
        // check that the texts are the default ones
        waitForText(R.id.scanner_description_text, R.string.scanner_description, 100)
        waitForText(R.id.scanner_textInImage_text, R.string.scanner_no_text_detected_yet, 100)
        waitForText(R.id.scanner_save_button, R.string.scanner_save_button, 100)
    }


    //    @Ignore("This test case is not working on cirrus-ci, but it works locally.")
    @Test(timeout = 5000)
    fun test_2_clickSave() {
        waitForText(R.id.scanner_description_text, R.string.scanner_description, 100)

        onView(withId(R.id.scanner_save_button)).perform(click())
        waitForText(R.string.scanner_photo_saved, 1000)
    }
}
