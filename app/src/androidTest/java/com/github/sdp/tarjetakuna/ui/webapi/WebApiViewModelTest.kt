package com.github.sdp.tarjetakuna.ui.webapi

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.FileReader
import com.github.sdp.tarjetakuna.utils.OkHttp3IdlingResource
import com.github.sdp.tarjetakuna.utils.OkHttpProvider
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
    private lateinit var scenario: FragmentScenario<WebApiFragment>

    private val mockWebServer = MockWebServer()
    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Before
    fun setUp() {

        scenario = launchFragmentInContainer()

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

    @Test
    fun test_getCards_success() {
        // check initial values
        val apiResults = viewModel.apiResults
        assert(apiResults.value == null)

        // setup the mock webserver to return a delayed response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("magic_webapi_cards_response.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            apiResults.observeForever { }
        }

        // call the api to get the cards
        viewModel.getCards()

        // get the text displayed during the waiting time
        val waitResult =
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.api_waiting_results)

        // wait for the response
        while (apiResults.value == null) {
            Thread.sleep(100)
        }
        while (apiResults.value == waitResult) {
            Thread.sleep(100)
        }

        // check that the text change to the response
        assertThat(
            apiResults.value!!.toString(),
            containsString("DataCard(name='Ancestor's Chosen', manaCost='{5}{W}{W}'")
        )
        assertThat(
            apiResults.value!!.toString(),
            containsString("DataCard(name='Angel of Mercy', manaCost='{4}{W}', colors=W")
        )
        assertThat(
            apiResults.value!!.toString(),
            containsString("DataCard(name='Angelic Blessing', manaCost='{2}{W}', colors=W")
        )
    }

    @Test
    fun test_getSets_success() {
        val apiResults = viewModel.apiResults
        assert(apiResults.value == null)

        // setup the mock webserver to return a delayed response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("magic_webapi_sets_response.json"))
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
        while (apiResults.value == null) {
            Thread.sleep(100)
        }
        while (apiResults.value == waitResult) {
            Thread.sleep(100)
        }

        // check that the text change to the response
        assertThat(
            apiResults.value!!.toString(),
            containsString("DataSet(code='2ED', name='Unlimited Edition', type='core', releaseDate=1993-12-01")
        )
        assertThat(
            apiResults.value!!.toString(),
            containsString("DataSet(code='2X2', name='Double Masters 2022', type='masters', releaseDate=2022-07-08")
        )
        assertThat(
            apiResults.value!!.toString(),
            containsString("DataSet(code='2XM', name='Double Masters', type='masters', releaseDate=2020-08-07")
        )
        assertThat(
            apiResults.value!!.toString(),
            containsString("DataSet(code='3ED', name='Revised Edition', type='core', releaseDate=1994-04-01")
        )
        assertThat(
            apiResults.value!!.toString(),
            containsString("DataSet(code='40K', name='Warhammer 40,000', type='commander', releaseDate=2022-08-12")
        )
    }

    @Test
    fun test_getCards_failure() {
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
        viewModel.getCards()

        // wait for the response
        while (apiError.value == null) {
            Thread.sleep(100)
        }

        // check that the text change to the response
        assertThat(
            apiError.value!!.toString(), containsString("error, file not found")
        )
    }

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
        while (apiError.value == null) {
            Thread.sleep(100)
        }

        // check that the text change to the response
        assertThat(
            apiError.value!!.toString(), containsString("error, file not found")
        )
    }

    @Test
    fun test_getCards_noResponse() {
        val apiError = viewModel.apiError
        assert(apiError.value == null)

        // no response from the server

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            apiError.observeForever { }
        }

        // call the api
        viewModel.getCards()

        // wait for the response
        while (apiError.value == null) {
            Thread.sleep(100)
        }

        // check that the text change to the response
        assertThat(
            apiError.value!!.toString(), containsString("timeout")
        )
    }

    @Test
    fun test_getSets_noResponse() {
        val apiError = viewModel.apiError
        assert(apiError.value == null)

        // no response from the server

        // use global scope to observe the live data
        GlobalScope.launch(Dispatchers.Main) {
            apiError.observeForever { }
        }

        // call the api
        viewModel.getSets()

        // wait for the response
        while (apiError.value == null) {
            Thread.sleep(100)
        }

        // check that the text change to the response
        assertThat(
            apiError.value!!.toString(), containsString("timeout")
        )
    }
}
