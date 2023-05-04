package com.github.sdp.tarjetakuna.ui


import androidx.navigation.Navigation.findNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {


    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        // mock the authentication
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
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
        activityRule = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
    }

    /**
     * Test that the authentication fragment is displayed when the corresponding button is clicked
     */
    @Test
    fun testClickOnSignInGoogle() {
        onView(withId(R.id.home_authentication_button)).perform(click())

        activityRule.onActivity { activity ->
            val navController = findNavController(activity, R.id.nav_host_fragment_content_drawer)
            assertThat(
                navController.currentDestination?.id,
                equalTo(R.id.nav_authentication_button)
            )
        }

    }

//    @Test
//    fun testGreetingFragmentSignOut() {
//        onView(withId(R.id.home_signOut_button)).perform(click())
//        activityRule.scenario.onActivity { activity ->
//            val navController =
//                findNavController(activity, R.id.nav_host_fragment_content_drawer)
//            // check if it goes back to nav_authentication first and then to nav_authentication_button
//            Assert.assertEquals(
//                navController.previousBackStackEntry?.destination?.id,
//                R.id.nav_authentication
//            )
//            Assert.assertEquals(
//                navController.currentDestination?.id,
//                R.id.nav_authentication_button
//            )
//        }
//    }

//    @Test
//    fun testGreetingFragmentBackToHome() {
//        onView(withId(R.id.authenticationButton_signOut_button)).perform(click())
//        activityRule.scenario.onActivity { activity ->
//            val navController =
//                findNavController(activity, R.id.nav_host_fragment_content_drawer)
//            Assert.assertEquals(navController.currentDestination?.id, R.id.nav_home)
//        }
//    }
    @Test
    fun buttonAddRandomCardWorks() {
        val databaseCards: List<DBMagicCard>
        onView(withId(R.id.add_random_card_button)).perform(click())
        runBlocking {
            withTimeout(5000) {
                databaseCards =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                        .magicCardDao().getAllCards()
            }
        }
        assert(databaseCards.isNotEmpty())
    }

}
