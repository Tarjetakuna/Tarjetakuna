package com.github.sdp.tarjetakuna.database.localDatabase

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDatabaseTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>
    private lateinit var database: AppDatabase

    private var cards = generateCards().map { it.toMagicCardEntity() }

    @Test
    fun dummyTest() {
    }

//    @Before
//    fun setUp() {
//        // database that will disappear after the end of the test
//        database =
//            LocalDatabaseProvider.setDatabase(
//                ApplicationProvider.getApplicationContext(), "", true
//            )!!
//        activityRule = ActivityScenario.launch(MainActivity::class.java)
//
//    }
//
//    @After
//    fun tearDown() {
//        LocalDatabaseProvider.closeDatabase()
//    }
//
//    @Test
//    fun testInsertCard() {
//        var databaseCards: List<MagicCardEntity>
//        runBlocking {
//            withTimeout(5000) {
//                database.magicCardDao().insertCard(cards[0])
//                databaseCards = database.magicCardDao().getAllCards()
//            }
//        }
//        assert(databaseCards.isNotEmpty())
////        assert(databaseCards[0].name == cards[0].name)
//        assert(databaseCards.size == 1)
//    }
//
//    @Test
//    fun testEmptyDatabase() {
//        var databaseCards: List<MagicCardEntity>
//        runBlocking {
//            withTimeout(5000) {
//                databaseCards = database.magicCardDao().getAllCards()
//            }
//        }
//        assert(databaseCards.isEmpty())
//    }
//
//    @Test
//    fun testDeleteCard() {
//        var databaseCards: List<MagicCardEntity>
//        runBlocking {
//            withTimeout(5000) {
//                database.magicCardDao().insertCard(cards[5])
//                database.magicCardDao().deleteCard(cards[5])
//                databaseCards = database.magicCardDao().getAllCards()
//            }
//        }
//        assert(databaseCards.isEmpty())
//    }
//
//    @Test
//    fun testAdd2Cards() {
//        var databaseCards: List<MagicCardEntity>
//        runBlocking {
//            withTimeout(5000) {
//                database.magicCardDao().insertCard(cards[0])
//                database.magicCardDao().insertCard(cards[1])
//                databaseCards = database.magicCardDao().getAllCards()
//            }
//        }
//        assert(databaseCards.isNotEmpty())
////        assert(databaseCards[0].name == cards[0].name)
////        assert(databaseCards[1].name == cards[1].name)
//        assert(databaseCards.size == 2)
//    }
//
//    @Test
//    fun testDeleteAll() {
//        var databaseCards: List<MagicCardEntity>
//        runBlocking {
//            withTimeout(5000) {
//                for (i in 0..5) {
//                    database.magicCardDao().insertCard(cards[i])
//                }
//                database.magicCardDao().deleteAllCards()
//                databaseCards = database.magicCardDao().getAllCards()
//            }
//        }
//        assert(databaseCards.isEmpty())
//    }
}
