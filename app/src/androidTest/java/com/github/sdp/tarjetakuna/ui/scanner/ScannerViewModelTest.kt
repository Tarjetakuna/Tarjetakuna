package com.github.sdp.tarjetakuna.ui.scanner

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.PermissionGranting.PermissionGranting.grantPermissions
import com.github.sdp.tarjetakuna.utils.Utils
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

//@Ignore("This test suit is not working on cirrus-ci, but it works locally.")
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ScannerViewModelTest {

    private val viewModel = ScannerViewModel()
    private val textDetected = viewModel.textDetected

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    @Before
    fun setUp() {
        Intents.init()
        grantPermissions()
        activityScenarioRule.scenario.onActivity {
            it.changeFragment(R.id.nav_scanner)
        }

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            textDetected.observeForever { }
        }

        // TODO not the best way, but no other way for now
        // wait for camera to be ready
        Thread.sleep(1000);
    }

    @After
    fun after() {
        activityScenarioRule.scenario.onActivity {
            it.changeFragment(R.id.nav_home)
        }
        Intents.release()
    }


    @Test
    fun test_0_viewModelStart() {
        assert(true)
    }

    @Test
    fun test_1_initialValues() {
        assert(textDetected.value == null)
    }

    @Test
    fun test_2_detectTextSuccess() {
        val textSuccess = "Success_test"

        // call the callback
        viewModel.detectTextSuccess(Text(textSuccess, emptyList<Text.TextBlock>()))

        // wait for the data to change
        Utils.waitWhileTrue(100, 10) { textDetected.value == null }

        assertThat(
            "match success text",
            textDetected.value!!.text,
            containsString(textSuccess)
        )
    }

    @Test
    fun test_3_detectTextError() {
        val textError = "Error_test"
        val exception = Exception(textError)

        // call the callback
        viewModel.detectTextError(exception)

        // wait for the data to change
        Utils.waitWhileTrue(100, 10) { textDetected.value == null }

        // check the data
        assertThat(
            "match error text",
            textDetected.value!!.text,
            containsString(textError)
        )
    }
}
