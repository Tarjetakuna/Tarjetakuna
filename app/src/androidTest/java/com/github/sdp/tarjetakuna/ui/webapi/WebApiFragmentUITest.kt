package com.github.sdp.tarjetakuna.ui.webapi

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.*
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
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
        onView(withId(R.id.api_sets_button)).check(matches(withText(R.string.api_sets)))
        onView(withId(R.id.api_results)).check(matches(withText(R.string.api_default_results)))
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

        // check that the text is the default one
        onView(withId(R.id.api_results)).perform(waitForText(R.string.api_default_results, 100))

        // click on the button
        onView(withId(R.id.api_random_card_button)).perform(waitForText(R.string.api_random_card, 100))
        onView(withId(R.id.api_random_card_button)).perform(click())

        // check that the text change to "waiting results" after the click
        onView(withId(R.id.api_results)).perform(waitForText(R.string.api_waiting_results, 200))
        onView(withId(R.id.api_results)).check(matches(withText(R.string.api_waiting_results)))

        // wait for the response - allowed because we manually set the delay on the mock response
        onView(withId(R.id.api_results)).perform(waitForTextDiff(R.string.api_waiting_results, 300))

        // check that the text change to the response
        onView(withId(R.id.api_results)).check(matches(withSubstring("Marsh Goblins")))
    }

    @Test
    fun test_clickOnSetsButtonWithMockWebServer() {
        // setup the mock webserver to return a delayed response
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_sets.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // check that the text is the default one
        onView(withId(R.id.api_results)).perform(waitForText(R.string.api_default_results, 100))

        // click on the button
        onView(withId(R.id.api_sets_button)).perform(waitForText(R.string.api_sets, 100))
        onView(withId(R.id.api_sets_button)).perform(click())

        // check that the text change to "waiting results" after the click
        onView(withId(R.id.api_results)).perform(waitForText(R.string.api_waiting_results, 200))
        onView(withId(R.id.api_results)).check(matches(withText(R.string.api_waiting_results)))

        // wait for the response - allowed because we manually set the delay on the mock response
        onView(withId(R.id.api_results)).perform(waitForTextDiff(R.string.api_waiting_results, 300))

        // check that the text change to the response
        onView(withId(R.id.api_results)).check(matches(withSubstring("Unlimited Edition")))
        onView(withId(R.id.api_results)).check(matches(withSubstring("Warhammer 40,000")))
    }
}
