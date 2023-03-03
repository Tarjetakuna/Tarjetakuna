package com.github.bjolidon.bootcamp

import android.content.Intent
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

//    @get:Rule
//    public val activityRule = ActivityScenarioRule(MainActivity::class.java)

//    @Test
//    fun testMainActivity() {
//        Intents.init()
//        val enteredName = Intent()
//        onView(withId(R.id.mainName)).perform(typeText("Test"))
//        closeSoftKeyboard()
//        onView(withId(R.id.mainGoButton)).perform(click())
//        Intents.intended(hasComponent(GreetingActivity::class.java.name))
//        Intents.intended(hasExtra("name", "Test"))
//        Intents.release()
//    }
}