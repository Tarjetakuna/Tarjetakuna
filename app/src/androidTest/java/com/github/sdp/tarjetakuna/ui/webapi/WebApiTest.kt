package com.github.sdp.tarjetakuna.ui.webapi

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import com.github.sdp.tarjetakuna.utils.FileReader
import com.github.sdp.tarjetakuna.utils.OkHttp3IdlingResource
import com.github.sdp.tarjetakuna.utils.OkHttpProvider
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test for WebApi - mostly done in the androidTest as need UI
 */
class WebApiTest {

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
    fun test_getRandomCard() {
        test_getRandomCard_withRetry()
    }

    private fun test_getRandomCard_withRetry(retry: Int = 5) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_cards_random.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fCard = WebApi.getRandomCard()
        fCard.whenComplete { card, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsByName_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("card is not null", card, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))

                assertThat(
                    "set not empty",
                    card.set,
                    `is`(not(String()))
                )
                assertThat(
                    "card name is Marsh Goblins",
                    card.name,
                    `is`("Marsh Goblins")
                )
            }
        }.get()
    }

    @Test
    fun test_getCardsByName() {
        test_getCardsByName_withRetry()
    }

    private fun test_getCardsByName_withRetry(retry: Int = 5) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_cards_search_name_ajani.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fCards = WebApi.getCardsByName("Ajani")
        fCards.whenComplete { cards, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsByName_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", cards, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("cards.cards not null", cards?.data, `is`(notNullValue()))
                assertThat("cards size bigger than 1", cards?.data?.size!!, greaterThan(1))
                assertThat(
                    "first card name is Ajani's Aid",
                    cards.data[0].name,
                    `is`("Ajani's Aid")
                )
            }
        }.get()
    }

    @Test
    fun test_getCardsBySet() {
        test_getCardsBySet_withRetry()
    }

    private fun test_getCardsBySet_withRetry(retry: Int = 5) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_cards_search_set_m15.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fCards = WebApi.getCardsBySet("m15")
        fCards.whenComplete { cards, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsBySet_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", cards, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("cards.cards not null", cards?.data, `is`(notNullValue()))
                assertThat("cards size bigger than 1", cards?.data?.size!!, greaterThan(1))

                for (card in cards.data) {
                    assertThat("card set is m15", card.set, `is`("m15"))
                }
            }
        }.get()
    }

    @Test
    fun test_getSets() {
        test_getSets_withRetry()
    }

    private fun test_getSets_withRetry(retry: Int = 5) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_sets.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fSets = WebApi.getSets()
        fSets.whenComplete { sets, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsBySet_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("sets is not null", sets, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("sets.sets not null", sets?.data, `is`(notNullValue()))
                assertThat("sets size bigger than 1", sets?.data?.size!!, greaterThan(1))

                for (set in sets.data) {
                    assertThat("set code not empty", set.code, `is`(not(String())))
                    assertThat("set name not empty", set.name, `is`(not(String())))
                }
            }
        }.get()
    }

    @Test
    fun test_getSetByCode() {
        test_getSetByCode_withRetry()
    }

    private fun test_getSetByCode_withRetry(retry: Int = 5) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("scryfall_api_sets_m15.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fSet = WebApi.getSetByCode("m15")
        fSet.whenComplete { set, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsBySet_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("set is not null", set, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))

                assertThat("set code is m15", set.code, `is`("m15"))
                assertThat("set name is Magic 2015", set.name, `is`("Magic 2015"))
            }
        }.get()
    }
}
