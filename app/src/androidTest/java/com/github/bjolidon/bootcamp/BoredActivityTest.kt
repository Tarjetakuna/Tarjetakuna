package com.github.bjolidon.bootcamp

import android.content.Intent
import android.util.Log
import android.widget.TextView
import androidx.test.core.app.launchActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoredActivityTest {

    private val mockWebServer = MockWebServer()
    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Before
    fun setup() {
        okHttp3IdlingResource = OkHttp3IdlingResource.create(
            "okhttp",
            OkHttpProvider.getOkHttpClient()
        )
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
        mockWebServer.start(8080)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

    @Test
    fun testBoredActivity_startActivity_initText() {
        val intent = Intent(getApplicationContext(), BoredActivityTestApp::class.java)
        val activity = ActivityScenario.launch<BoredActivityTestApp>(intent)
        onView(withId(R.id.bored_txtview)).check(matches(withText(R.string.txt_click_to_get_bored)))
        activity.close()
    }

    @Test
    fun testBoredActivity_startActivity_onClick() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("success_response.json"))
            }
        }

        val intent = Intent(getApplicationContext(), BoredActivityTestApp::class.java)
        val activity = ActivityScenario.launch<BoredActivityTestApp>(intent)
        activity.onActivity {
            Log.d("testBoredActivity_startActivity_onClick", "txt1 :" + it.findViewById<TextView>(R.id.bored_txtview).text)
        }
        onView(withId(R.id.so_bored_btn)).perform(click())
//        onView(withId(R.id.bored_txtview)).check(matches(withText(R.string.txt_getting_bored)))
//        activity.onActivity {
//            Log.d("testBoredActivity_startActivity_onClick", "txt2 :" + it.findViewById<TextView>(R.id.bored_txtview).text)
//        }

//        while(activity.state != ActivityScenario.State.DESTROYED) {
//            Thread.sleep(100)
//        }

        val fakeSuccessValue = "you can do : Learn Morse code\nwith 1 people\nprice 0.0 out of [0, 1] zero being free\naccessibility : 0.0 out of [0.0-1.0] zero being the most accessible\ntype : education\nlink : https://en.wikipedia.org/wiki/Morse_code\nkey : 3646173"
        onView(withId(R.id.bored_txtview)).check(matches(withText(fakeSuccessValue)))
        activity.onActivity {
            Log.d("testBoredActivity_startActivity_onClick", "txt3 :" + it.findViewById<TextView>(R.id.bored_txtview).text)
        }

        activity.close()
    }

    @Test
    fun testSuccessfulResponse() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("success_response.json"))
            }
        }

        val intent = Intent(getApplicationContext(), BoredActivityTestApp::class.java)
        val activity = ActivityScenario.launch<BoredActivityTestApp>(intent)

//        onView(withId(R.id.progress_bar))
//            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
//        onView(withId(R.id.meme_recyclerview))
//            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.textview))
//            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        activity.close()
    }
}
