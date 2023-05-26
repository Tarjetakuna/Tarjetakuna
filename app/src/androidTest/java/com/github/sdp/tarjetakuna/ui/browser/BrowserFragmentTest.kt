package com.github.sdp.tarjetakuna.ui.browser;

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.DatabaseSync
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.utils.Utils
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class BrowserFragmentTest {

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()

        activityRule = ActivityScenario.launch(MainActivity::class.java)
        DatabaseSync.activateSync = false
        Intents.init()

        // mock the authentication
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        Mockito.`when`(mockedAuth.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mockedAuth)

        // close the database that could have been opened because of the previous tests
        LocalDatabaseProvider.closeDatabase("test")
        LocalDatabaseProvider.closeDatabase("test2")
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)

        LocalDatabaseProvider.setDatabase(
            ApplicationProvider.getApplicationContext(),
            LocalDatabaseProvider.CARDS_DATABASE_NAME,
            true
        )

        val magicDAO = LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
            ?.magicCardDao()
        runBlocking {
            withTimeout(5000) {
                magicDAO?.insertCard(
                    DBMagicCard(
                        CommonMagicCard.venomousHierophantCard,
                        CardPossession.OWNED,
                        1
                    )
                )
                magicDAO?.insertCard(
                    DBMagicCard(
                        CommonMagicCard.aeronautTinkererCard,
                        CardPossession.WANTED,
                        1
                    )
                )
            }
        }

        // Get a reference to the fragment's view
        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_browser, null)
        }
    }

    @After
    fun after() {
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
        activityRule.close()
        Intents.release()
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
        onView(withText(R.string.browser_page_adapter_collection_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.browser_page_adapter_wanted_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testScrollingBetweenTabsWorksCorrectly() {
        onView(withId(R.id.viewPager)).check(matches(hasDescendant(withText(CommonMagicCard.venomousHierophantCard.name))))

        onView(withId(R.id.viewPager)).perform(swipeLeft())

        onView(withId(R.id.viewPager)).check(matches(hasDescendant(withText(CommonMagicCard.aeronautTinkererCard.name))))

        onView(withId(R.id.viewPager)).perform(swipeRight())

        onView(withId(R.id.viewPager)).check(matches(hasDescendant(withText(CommonMagicCard.venomousHierophantCard.name))))
    }

}
