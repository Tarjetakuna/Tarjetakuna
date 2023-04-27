package com.github.sdp.tarjetakuna.ui


import androidx.navigation.Navigation.findNavController
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {


    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Test that the authentication fragment is displayed when the corresponding button is clicked
     */
    @Test
    fun testClickOnSignInGoogle() {
        onView(withId(R.id.home_authenticationButton)).perform(click())

        activityRule.scenario.onActivity { activity ->
            val navController = findNavController(activity, R.id.nav_host_fragment_content_drawer)
            assertThat(
                navController.currentDestination?.id,
                equalTo(R.id.nav_authentication_button)
            )
        }

    }

//    @Test
//    fun buttonAddRandomCardWorks() {
//        val databaseCards: List<MagicCardEntity>
//        onView(withId(R.id.addRandomCardButton)).perform(click())
//        runBlocking {
//            withTimeout(5000) {
//                databaseCards = database.magicCardDao().getAllCards()
//            }
//        }
//        assert(databaseCards.isNotEmpty())
//    }

}
