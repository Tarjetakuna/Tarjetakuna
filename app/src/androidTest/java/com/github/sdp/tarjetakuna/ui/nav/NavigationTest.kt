package com.github.sdp.tarjetakuna.ui.nav

import androidx.test.core.app.ApplicationProvider.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        Thread.sleep(1000);
    }

    @After
    fun after() {
        Intents.release()
    }

    @Test
    fun test_changeToWebApi() {
        activityScenarioRule.scenario.onActivity {
            it.changeFragment(R.id.nav_webapi)
            // TODO : implement better navigation tests
            // doenst work yet
//            checkNotNull(fragment)
//            assert(fragment is WebApiFragment)
//            assert(fragment.isVisible)
        }
    }


}
