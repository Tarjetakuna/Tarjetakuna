package com.github.sdp.tarjetakuna.ui.scanner

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import com.github.sdp.tarjetakuna.utils.PermissionGranting.PermissionGranting.grantPermissions
import com.github.sdp.tarjetakuna.utils.Utils
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ScannerViewModelTest {

    private val viewModel = ScannerViewModelTester()
    private val textDetected = viewModel.textDetected
    private lateinit var scenario: FragmentScenario<ScannerFragment>

    @Before
    fun setUp() {
        grantPermissions()
        scenario = launchFragmentInContainer()

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            textDetected.observeForever { }
        }

    }

    @After
    fun after() {
        scenario.close()
    }


    @Test
    fun test_viewModelStart() {
        assert(true)
    }

    @Test
    fun test_initialValues() {
        assert(textDetected.value == null)
    }

    @Test
    fun test_detectTextSuccess() {
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
    fun test_detectTextError() {
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
