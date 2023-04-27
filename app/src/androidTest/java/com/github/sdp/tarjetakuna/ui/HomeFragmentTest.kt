package com.github.sdp.tarjetakuna

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.database.local.MagicCardEntity
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private val cards = generateCards()
    private lateinit var database: AppDatabase


    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        database =
            LocalDatabaseProvider.setTestDatabase(ApplicationProvider.getApplicationContext())!!
        activityRule = ActivityScenario.launch(MainActivity::class.java)

    }

    @After
    fun tearDown() {
        LocalDatabaseProvider.closeDatabase()
    }

    @Test
    fun testFilterButtonWorks() {
        onView(withId(R.id.filterFragmentButton)).perform(click())
        val bundle = Bundle()
        bundle.putString("cards", Gson().toJson(cards))

        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_filter, bundle)
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
            ViewMatchers.assertThat(
                navController.currentDestination?.id,
                Matchers.equalTo(R.id.nav_filter)
            )
        }
    }

    @Test
    fun buttonAddRandomCardWorks() {
        val databaseCards: List<MagicCardEntity>
        onView(withId(R.id.addRandomCardButton)).perform(click())
        runBlocking {
            withTimeout(5000) {
                databaseCards = database.magicCardDao().getAllCards()
            }
        }
        assert(databaseCards.isNotEmpty())
    }
}
