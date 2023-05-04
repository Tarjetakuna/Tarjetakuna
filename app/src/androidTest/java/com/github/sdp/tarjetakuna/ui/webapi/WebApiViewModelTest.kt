package com.github.sdp.tarjetakuna.ui.webapi

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.FileReader
import com.github.sdp.tarjetakuna.utils.OkHttp3IdlingResource
import com.github.sdp.tarjetakuna.utils.OkHttpProvider
import com.github.sdp.tarjetakuna.utils.Utils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test the WebApiViewModel behaviour (need UI to mock the webserver) of apiResults and apiError
 */
class WebApiViewModelTest {
    private val viewModel = WebApiViewModelTester()
    private lateinit var scenario: ActivityScenario<MainActivity>

    private val mockWebServer = MockWebServer()
    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Before
    fun setUp() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        // setup mock webserver
        okHttp3IdlingResource = OkHttp3IdlingResource.create(
            "okhttp",
            OkHttpProvider.getOkHttpClient()
        )
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
        mockWebServer.start(8080)

        // setup the api to use the mock webserver
        WebApi.magicUrl = "http://127.0.0.1:8080"
    }

    @After
    fun after() {

        scenario.close()

        // teardown mock webserver
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }


    @Test
    fun test_initialValues() {
        assert(viewModel.apiResults.value == null)
        assert(viewModel.apiError.value == null)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun test_getSets_success() {
        val apiResults = viewModel.apiResults
        assert(apiResults.value == null)

        // setup the mock webserver to return a delayed response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_sets.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            apiResults.observeForever { }
        }

        // call the api
        viewModel.getSets()

        val waitResult =
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.api_waiting_results)

        // wait for the response
        Utils.waitWhileTrue(100, 10) { apiResults.value == null }
        Utils.waitWhileTrue(100, 10) { apiResults.value == waitResult }

        // check that the text change to the response
        assertThat(
            apiResults.value!!.toString(),
            containsString("Unlimited Edition")
        )
        assertThat(
            apiResults.value!!.toString(),
            containsString("Double Masters 2022")
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun test_getRandomCard_failure() {
        val apiError = viewModel.apiError
        assert(apiError.value == null)

        // setup the mock webserver to return a delayed error response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(404)
                    .setBody("error, file not found")
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            apiError.observeForever { }
        }

        // call the api
        viewModel.getRandomCard()

        // wait for the response
        Utils.waitWhileTrue(100, 10) { apiError.value == null }

        // check that the text change to the response
        assertThat(
            apiError.value!!.toString(), containsString("error, file not found")
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun test_getSets_failure() {
        val apiError = viewModel.apiError
        assert(apiError.value == null)

        // setup the mock webserver to return a delayed error response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(404)
                    .setBody("error, file not found")
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            apiError.observeForever { }
        }

        // call the api
        viewModel.getSets()

        // wait for the response
        Utils.waitWhileTrue(100, 10) { apiError.value == null }

        // check that the text change to the response
        assertThat(
            apiError.value!!.toString(), containsString("error, file not found")
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun test_getRandomCard_noResponse() {
        val apiError = viewModel.apiError
        assert(apiError.value == null)

        // no response from the server, set timeout to 1s
        WebApi.setReadTimeout(1L)

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            apiError.observeForever { }
        }

        // call the api
        viewModel.getRandomCard()

        // wait for the response - timeout of 1s, wait 3 times to be sure
        Utils.waitWhileTrue(1000, 3) { apiError.value == null }

        // check that the text change to the response
        assertThat(
            apiError.value!!.toString(), containsString("timeout")
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun test_getSets_noResponse() {
        val apiError = viewModel.apiError
        assert(apiError.value == null)

        // no response from the server, set timeout to 1s
        WebApi.setReadTimeout(1L)

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            apiError.observeForever { }
        }

        // call the api
        viewModel.getSets()

        // wait for the response - timeout of 1s, wait 3 times to be sure
        Utils.waitWhileTrue(1000, 3) { apiError.value == null }

        // check that the text change to the response
        assertThat(
            apiError.value!!.toString(), containsString("timeout")
        )
    }
}
