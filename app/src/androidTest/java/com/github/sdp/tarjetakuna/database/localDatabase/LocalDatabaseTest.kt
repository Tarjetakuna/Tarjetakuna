package com.github.sdp.tarjetakuna.database.localDatabase

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import com.github.sdp.tarjetakuna.utils.Utils
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class LocalDatabaseTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>
    private lateinit var database: AppDatabase

    private var cards = generateCards().map { DBMagicCard.fromMagicCard(it) }

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()

        // mock the authentication
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        Mockito.`when`(mockedAuth.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mockedAuth)

        // close the database that could have been opened because of the previous tests
        LocalDatabaseProvider.closeDatabase("test")
        LocalDatabaseProvider.closeDatabase("test2")
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)

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
