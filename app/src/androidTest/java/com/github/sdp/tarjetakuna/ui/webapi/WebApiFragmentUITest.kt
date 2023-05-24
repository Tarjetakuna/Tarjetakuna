package com.github.sdp.tarjetakuna.ui.webapi

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.*
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Test the WebApiFragment UI reacting to UI events
 */
@RunWith(AndroidJUnit4::class)
class WebApiFragmentUITest {

    private lateinit var scenario: FragmentScenario<WebApiFragment>
    private val mockWebServer = MockWebServer()
    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    private val recyclerView = onView(withId(R.id.web_api_list_card))

    @Before
    fun setUp() {
        Intents.init()
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
        Intents.release()
        scenario.close()

        // teardown mock webserver
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

    @Test
    fun test_initialText() {
        onView(withId(R.id.api_random_card_button)).check(matches(withText(R.string.api_random_card)))
    }

    @Test
    fun test_clickOnRandomCardButtonWithMockWebServer() {
        // setup the mock webserver to return a delayed response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_cards_random.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // make the call to the api
        onView(withId(R.id.api_random_card_button)).perform(click())

        // wait for the response and for the recyclerView to be updated
        var result = false
        val timeoutMillis: Long = 2000
        val startTimeMillis = System.currentTimeMillis()

        while (System.currentTimeMillis() - startTimeMillis < timeoutMillis) {
            try {
                // check that the card displayed is the one from the response
                recyclerView.perform(scrollToPosition<RecyclerView.ViewHolder>(0))
                recyclerView.check(matches(hasDescendant(withText("Marsh Goblins"))))

                // assertion passed, break the loop
                result = true
                break
            } catch (e: AssertionError) {
                // Assertion failed, continue looping until timeout
                Espresso.onIdle()
            }
        }

        // check that the test passed
        assert(result)
    }

    @Test
    fun test_searchBySetCodeWithMockWebServer() {
        // setup the mock webserver to return a delayed response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_cards_search_set_m15.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        onView(withId(R.id.api_set_id_edittext)).perform(click())
        onView(withId(R.id.api_set_id_edittext)).perform(typeText("m15"))
        onView(withId(R.id.api_cards_by_set_button)).perform(click())

        // wait for the response and for the recyclerView to be updated
        var result = false
        val timeoutMillis: Long = 2000
        val startTimeMillis = System.currentTimeMillis()

        while (System.currentTimeMillis() - startTimeMillis < timeoutMillis) {
            try {
                // check that the cards displayed are the one from the response
                recyclerView.perform(scrollToPosition<RecyclerView.ViewHolder>(0))
                recyclerView.check(matches(hasDescendant(withText("Ajani Steadfast"))))
                recyclerView.perform(scrollToPosition<RecyclerView.ViewHolder>(3))
                recyclerView.check(matches(hasDescendant(withText("Battle Mastery"))))

                // assertion passed, break the loop
                result = true
                break
            } catch (e: AssertionError) {
                // Assertion failed, continue looping until timeout
                Espresso.onIdle()
            }
        }

        // check that the test passed
        assert(result)
    }

    @Test
    fun test_searchByNameWithMockWebServer() {
        // setup the mock webserver to return a delayed response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_cards_search_name_ajani.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        onView(withId(R.id.api_card_name_edittext)).perform(click())
        onView(withId(R.id.api_card_name_edittext)).perform(typeText("ajani"))
        onView(withId(R.id.api_cards_by_name_button)).perform(click())

        // wait for the response and for the recyclerView to be updated
        var result = false
        val timeoutMillis: Long = 2000
        val startTimeMillis = System.currentTimeMillis()

        while (System.currentTimeMillis() - startTimeMillis < timeoutMillis) {
            try {
                // check that the cards displayed are the one from the response
                recyclerView.perform(scrollToPosition<RecyclerView.ViewHolder>(0))
                recyclerView.check(matches(hasDescendant(withText("Ajani's Aid"))))
                recyclerView.perform(scrollToPosition<RecyclerView.ViewHolder>(1))
                recyclerView.check(matches(hasDescendant(withText("Ajani's Comrade"))))

                // assertion passed, break the loop
                result = true
                break
            } catch (e: AssertionError) {
                // Assertion failed, continue looping until timeout
                Espresso.onIdle()
            }
        }

        // check that the test passed
        assert(result)
    }

}
