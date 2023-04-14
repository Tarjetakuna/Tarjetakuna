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
    fun test_getCards() {
        test_getCards_withRetry(5)
    }

    private fun test_getCards_withRetry(retry: Int) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("magic_webapi_cards_response.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fCards = WebApi.getCards()
        fCards.whenComplete { cards, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsByName_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", cards, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("cards.cards not null", cards?.cards, `is`(notNullValue()))
                assertThat("cards size bigger than 4", cards?.cards?.size!!, `is`(greaterThan(4)))

                assertThat(
                    "first card imageUrl not empty",
                    cards.cards[0].imageUrl,
                    `is`(not(String()))
                )
                assertThat(
                    "first card name is Ancestor's Chosen",
                    cards.cards[0].name,
                    `is`("Ancestor's Chosen")
                )
            }
        }.get()
    }

    @Test
    fun test_getCardsByName() {
        test_getCardsByName_withRetry(5)
    }

    private fun test_getCardsByName_withRetry(retry: Int) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("magic_webapi_cardByName_Ancester'sChosen_response.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fCards = WebApi.getCardsByName("Ancestor's Chosen")
        fCards.whenComplete { cards, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsByName_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", cards, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("cards.cards not null", cards?.cards, `is`(notNullValue()))
                assertThat("cards size bigger than 1", cards?.cards?.size!!, greaterThan(1))

                assertThat(
                    "first card imageUrl not empty",
                    cards.cards[0].imageUrl,
                    `is`(not(String()))
                )
                assertThat(
                    "first card name is Ancestor's Chosen",
                    cards.cards[0].name,
                    `is`("Ancestor's Chosen")
                )
            }
        }.get()
    }

    @Test
    fun test_getCardById() {
        test_getCardById_withRetry(5)
    }

    private fun test_getCardById_withRetry(retry: Int) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("magic_webapi_cardById_386616_response.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fCard = WebApi.getCardById("386616")
        fCard.whenComplete { card, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardById_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", card, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))

                assertThat("imageUrl not empty", card.card.imageUrl, `is`(not(String())))
                assertThat("name is", card.card.name, `is`("Narset, Enlightened Master"))
                assertThat("id is", card.card.multiverseid, `is`("386616"))
            }
        }.get()
    }

    @Test
    fun test_getCardsBySet() {
        test_getCardsBySet_withRetry(5)
    }

    private fun test_getCardsBySet_withRetry(retry: Int) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("magic_webapi_cardBySet_KTK_response.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fCards = WebApi.getCardsBySet("KTK")
        fCards.whenComplete { cards, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsBySet_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", cards, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("cards.cards not null", cards?.cards, `is`(notNullValue()))
                assertThat("cards size bigger than 1", cards?.cards?.size!!, greaterThan(1))

                for (card in cards.cards) {
                    assertThat("card set is KTK", card.set, `is`("KTK"))
                    assertThat("card imageUrl not empty", card.imageUrl, `is`(not(String())))
                }
            }
        }.get()
    }

    @Test
    fun test_getSets() {
        test_getSets_withRetry(5)
    }

    private fun test_getSets_withRetry(retry: Int) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("magic_webapi_sets_response.json"))
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
                assertThat("sets.sets not null", sets?.sets, `is`(notNullValue()))
                assertThat("sets size bigger than 1", sets?.sets?.size!!, greaterThan(1))

                for (set in sets.sets) {
                    assertThat("set code not empty", set.code, `is`(not(String())))
                    assertThat("set name not empty", set.name, `is`(not(String())))
                }
            }
        }.get()
    }

    @Test
    fun test_getSetByCode() {
        test_getSetByCode_withRetry(5)
    }

    private fun test_getSetByCode_withRetry(retry: Int) {
        // setup the mock webserver to return a delayed response with the cards
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("magic_webapi_setBySet_KTK_response.json"))
                    .setBodyDelay(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            }
        }

        // call the api
        val fSet = WebApi.getSetByCode("KTK")
        fSet.whenComplete { set, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsBySet_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("set is not null", set, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))

                assertThat("set code is KTK", set.set.code, `is`("KTK"))
                assertThat("set name is Khans of Tarkir", set.set.name, `is`("Khans of Tarkir"))
            }
        }.get()
    }
}
