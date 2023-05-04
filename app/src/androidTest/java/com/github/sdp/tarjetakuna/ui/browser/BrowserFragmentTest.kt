package com.github.sdp.tarjetakuna.ui.browser;

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrowserFragmentTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        activityRule = ActivityScenario.launch(MainActivity::class.java)

        // Get a reference to the fragment's view
        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_browser, null)
        }
    }

    @After
    fun after() {
        activityRule.close()
    }

    @Test
    fun testViewPagerIsDisplayed() {
        onView(withId(R.id.viewPager))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTabLayoutIsDisplayed() {
        onView(withId(R.id.tabLayout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTabLayoutHasCorrectTabs() {
        onView(withText("User"))
            .check(matches(isDisplayed()))
        onView(withText("BrowserApi"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testScrollingBetweenTabsWorksCorrectly() {
        onView(withId(R.id.user_collection_filter_button)).check(matches(isDisplayed()))
        onView(withId(R.id.viewPager)).perform(swipeLeft())
        onView(withId(R.id.api_random_card_button)).check(matches(isDisplayed()))
        onView(withId(R.id.viewPager)).perform(swipeRight())
        onView(withId(R.id.user_collection_filter_button)).check(matches(isDisplayed()))
    }

}
