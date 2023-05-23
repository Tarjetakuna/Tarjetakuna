package com.github.sdp.tarjetakuna.database

import androidx.test.core.app.ApplicationProvider
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.utils.FBEmulator
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mockito

class DatabaseSyncTest {

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    private lateinit var userRTDB: UserRTDB
    private var database: AppDatabase? = null

    @Before
    fun setUp() {
        // close the database that could have been opened because of the previous tests
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        Mockito.`when`(mockedAuth.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mockedAuth)

        LocalDatabaseProvider.deleteDatabases(
            ApplicationProvider.getApplicationContext(),
            arrayListOf(LocalDatabaseProvider.CARDS_DATABASE_NAME)
        )

        database = LocalDatabaseProvider.setDatabase(
            ApplicationProvider.getApplicationContext(),
            LocalDatabaseProvider.CARDS_DATABASE_NAME,
            true
        )
        userRTDB = UserRTDB(FirebaseDB())
        FirebaseDB().clearDatabase()
    }

    @After
    fun tearDown() {
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
        LocalDatabaseProvider.deleteDatabases(
            ApplicationProvider.getApplicationContext(),
            arrayListOf(LocalDatabaseProvider.CARDS_DATABASE_NAME)
        )
        FirebaseDB().clearDatabase()
    }

    @Test
    fun addOwnedCardFromFirebaseToLocalDatabaseWorks() {
        val card1 = DBMagicCard(CommonMagicCard.aeronautTinkererCard, CardPossession.OWNED, 1)
        card1.lastUpdate = 1

        val card2 = DBMagicCard(CommonMagicCard.solemnOfferingCard, CardPossession.OWNED, 1)
        card2.lastUpdate = 2
        // assert to be sure that it has been added
        assertThat(
            "card has been added to firebase",
            userRTDB.addCard(card1, SignIn.getSignIn().getUserUID()!!).get()
        )
        assertThat(
            "card has been added to firebase",
            userRTDB.addCard(card2, SignIn.getSignIn().getUserUID()!!).get()
        )

        DatabaseSync.sync().get()

        runBlocking {
            withTimeout(5000) {
                val databaseCards = database!!.magicCardDao().getAllCards()
                assertThat(
                    "card has been added to local database",
                    databaseCards.contains(card1)
                )
                assertThat(
                    "card has been added to local database",
                    databaseCards.contains(card2)
                )
            }
        }

    }
}
