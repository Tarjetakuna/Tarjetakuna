package com.github.sdp.tarjetakuna.database.localDatabase

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDatabaseTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>
    private lateinit var database: AppDatabase

    private var cards = generateCards().map { DBMagicCard.fromMagicCard(it) }


    @Before
    fun setUp() {
        // database that will disappear after the end of the test
        LocalDatabaseProvider.debugging = true
        database =
            LocalDatabaseProvider.setDatabase(
                ApplicationProvider.getApplicationContext(), "test", true
            )!!
        activityRule = ActivityScenario.launch(MainActivity::class.java)

    }

    @After
    fun tearDown() {
        LocalDatabaseProvider.closeDatabase("test")
    }

    @Test
    fun testInsertCard() {
        var databaseCards: List<DBMagicCard>
        runBlocking {
            withTimeout(5000) {
                database.magicCardDao().insertCard(cards[0])
                databaseCards = database.magicCardDao().getAllCards()
            }
        }
        assert(databaseCards.isNotEmpty())
        assert(databaseCards[0].card == cards[0].card)
        assert(databaseCards.size == 1)
    }

    @Test
    fun testEmptyDatabase() {
        var databaseCards: List<DBMagicCard>
        runBlocking {
            withTimeout(5000) {
                databaseCards = database.magicCardDao().getAllCards()
            }
        }
        assert(databaseCards.isEmpty())
    }

    @Test
    fun testDeleteCard() {
        var databaseCards: List<DBMagicCard>
        runBlocking {
            withTimeout(5000) {
                database.magicCardDao().insertCard(cards[5])
                database.magicCardDao().deleteCard(cards[5])
                databaseCards = database.magicCardDao().getAllCards()
            }
        }
        assert(databaseCards.isEmpty())
    }

    @Test
    fun testAdd2Cards() {
        var databaseCards: List<DBMagicCard>
        runBlocking {
            withTimeout(5000) {
                database.magicCardDao().insertCard(cards[0])
                database.magicCardDao().insertCard(cards[1])
                databaseCards = database.magicCardDao().getAllCards()
            }
        }
        assert(databaseCards.isNotEmpty())
        assert(databaseCards[0].card == cards[0].card)
        assert(databaseCards[1].card == cards[1].card)
        assert(databaseCards.size == 2)
    }

    @Test
    fun testDeleteAll() {
        var databaseCards: List<DBMagicCard>
        runBlocking {
            withTimeout(5000) {
                for (i in 0..5) {
                    database.magicCardDao().insertCard(cards[i])
                }
                database.magicCardDao().deleteAllCards()
                databaseCards = database.magicCardDao().getAllCards()
            }
        }
        assert(databaseCards.isEmpty())
    }
}
