package com.github.sdp.tarjetakuna.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentInActivityTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        Intents.init()
        activityRule = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun after() {
        activityRule.close()
        Intents.release()
    }

}
