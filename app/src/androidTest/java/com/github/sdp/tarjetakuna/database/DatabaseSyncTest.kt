package com.github.sdp.tarjetakuna.database

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class DatabaseSyncTest {

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    private lateinit var userRTDB: UserRTDB

    @Before
    fun setUp() {
        // close the database that could have been opened because of the previous tests
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        Mockito.`when`(mockedAuth.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mockedAuth)

        LocalDatabaseProvider.setDatabase(
            ApplicationProvider.getApplicationContext(),
            LocalDatabaseProvider.CARDS_DATABASE_NAME,
            true
        )
        userRTDB = UserRTDB(FirebaseDB())
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
    }

    @After
    fun tearDown() {
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
    }

    private fun test1(possession: CardPossession) {
        val card1 = DBMagicCard(CommonMagicCard.aeronautTinkererCard, possession, 1)
            .copy(lastUpdate = 1)

        val card2 = DBMagicCard(CommonMagicCard.solemnOfferingCard, possession, 1)
            .copy(lastUpdate = 2)

        userRTDB.addCard(card1, SignIn.getSignIn().getUserUID()!!).get()
        userRTDB.addCard(card2, SignIn.getSignIn().getUserUID()!!).get()
        Log.i(
            "DatabaseSyncTest", "carte2: ${
                userRTDB.getCardFromUserPossession(
                    SignIn.getSignIn().getUserUID()!!,
                    card2.code,
                    card2.number,
                    possession
                ).get()
            }"
        )
        DatabaseSync.sync().get()
        var cards: List<DBMagicCard> = listOf()
        runBlocking {
            withTimeout(5000) {
                cards =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                        .magicCardDao()
                        .getAllCardsByPossession(possession.toString())
                if (cards.isEmpty()) {
                    fail("cards is empty")
                } else {
                    Log.i("DatabaseSyncTest", "cards retrieved: $cards")
                }
            }
            assertThat(
                "card has been added to local database",
                cards.contains(card1)
            )
            assertThat(
                "card has been added to local database",
                cards.contains(card2)
            )
        }

    }

    @Test
    fun addOwnedCardFromFirebaseToLocalDatabaseWorks() {
        test1(CardPossession.OWNED)

    }

    @Test
    fun addWantedCardFromFirebaseToLocalDatabaseWorks() {
        test1(CardPossession.WANTED)
    }

    @Test
    fun addTradeCardFromFirebaseToLocalDatabaseWorks() {
        test1(CardPossession.TRADE)
    }

    @Test
    fun addDifferentTypeOfCardWorks() {
        val card1 = DBMagicCard(CommonMagicCard.aeronautTinkererCard, CardPossession.OWNED, 1)
            .copy(lastUpdate = 1)

        val card2 = DBMagicCard(CommonMagicCard.solemnOfferingCard, CardPossession.TRADE, 1)
            .copy(lastUpdate = 2)

        userRTDB.addCard(card1, SignIn.getSignIn().getUserUID()!!).get()
        userRTDB.addCard(card2, SignIn.getSignIn().getUserUID()!!).get()

        DatabaseSync.sync().get()

        runBlocking {
            withTimeout(5000) {
                val databaseCards =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                        .magicCardDao()
                        .getAllCardsByPossession(CardPossession.TRADE.toString())

                assertThat(
                    "card has been added to local database",
                    databaseCards.contains(card2)
                )

                val databaseCards2 =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                        .magicCardDao()
                        .getAllCardsByPossession(CardPossession.OWNED.toString())

                assertThat(
                    "card has been added to local database",
                    databaseCards2.contains(card1)
                )
            }
        }
    }

    @Test
    fun firebaseEmptyButNotLocalDatabase() {
        val card1 = DBMagicCard(CommonMagicCard.aeronautTinkererCard, CardPossession.OWNED, 1)
            .copy(lastUpdate = 1)

        val card2 = DBMagicCard(CommonMagicCard.solemnOfferingCard, CardPossession.OWNED, 1)
            .copy(lastUpdate = 2)

        runBlocking {
            LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                .magicCardDao().insertCards(listOf(card1, card2))
        }

        DatabaseSync.sync().get()

        val userRTDB = UserRTDB(FirebaseDB())
        val cards = userRTDB.getListOfFullCardsInfos(
            SignIn.getSignIn().getUserUID()!!,
            CardPossession.OWNED
        ).get()
        assertThat("cards contains card1", cards.contains(card1))
        assertThat("cards contains card2", cards.contains(card2))
    }

    @Test
    fun firebaseAndLocalDatabaseEmpty() {
        DatabaseSync.sync().get()

        userRTDB.getListOfFullCardsInfos(
            SignIn.getSignIn().getUserUID()!!,
            CardPossession.OWNED
        ).thenAccept {
            assertThat("cards is not empty", false)
        }.exceptionally {
            assertThat("cards is empty", true)
            null
        }.get()

        userRTDB.getListOfFullCardsInfos(
            SignIn.getSignIn().getUserUID()!!,
            CardPossession.WANTED
        ).thenAccept {
            assertThat("cards is not empty", false)
        }.exceptionally {
            assertThat("cards is empty", true)
            null
        }.get()

        userRTDB.getListOfFullCardsInfos(
            SignIn.getSignIn().getUserUID()!!,
            CardPossession.TRADE
        ).thenAccept {
            assertThat("cards is not empty", false)
        }.exceptionally {
            assertThat("cards is empty", true)
            null
        }.get()
    }

    @Test
    fun userNotSignedIn() {
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(false)
        Mockito.`when`(mockedAuth.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mockedAuth)

        DatabaseSync.sync()
            .thenAccept {
                assertThat("exception is not null", false)
            }
            .exceptionally { exception ->
                assertThat("exception is not null", exception != null)
                null
            }.get()

    }

    @Test
    fun cannotSyncTwiceAtATime() {
        userRTDB.addCard(
            DBMagicCard(
                CommonMagicCard.aeronautTinkererCard,
                CardPossession.OWNED,
                1
            ).copy(lastUpdate = 1), SignIn.getSignIn().getUserUID()!!
        ).get()

        userRTDB.addCard(
            DBMagicCard(
                CommonMagicCard.solemnOfferingCard,
                CardPossession.OWNED,
                1
            ).copy(lastUpdate = 2), SignIn.getSignIn().getUserUID()!!
        ).get()

        val sync = DatabaseSync.sync()
        DatabaseSync.sync()
            .thenAccept {
                assertThat("sync started", false)
            }
            .exceptionally { exception ->
                assertThat("sync not started", exception != null)
                null
            }.get()

        sync.get()
    }

    @Test
    fun databaseNotInitialized() {
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
        DatabaseSync.sync()
            .thenAccept {
                assertThat("exception is not null", false)
            }
            .exceptionally {
                assertThat("exception is not null", true)
                null
            }.get()
    }

    @Test
    fun userNotConnectedAndDatabaseNotInitialized() {
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(false)
        Mockito.`when`(mockedAuth.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mockedAuth)

        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
        DatabaseSync.sync()
            .thenAccept {
                assertThat("exception is not null", false)
            }
            .exceptionally {
                assertThat("exception is not null", true)
                null
            }.get()
    }

    @Test
    fun fbCardOlderThanLocalCard() {
        val card1 = DBMagicCard(CommonMagicCard.aeronautTinkererCard, CardPossession.OWNED, 1)
            .copy(lastUpdate = 1)

        val card2 = DBMagicCard(CommonMagicCard.solemnOfferingCard, CardPossession.OWNED, 1)
            .copy(lastUpdate = 100)

        runBlocking {
            LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                .magicCardDao().insertCard(card2)
        }

        userRTDB.addCard(
            card1,
            SignIn.getSignIn().getUserUID()!!
        ).get()

        DatabaseSync.sync().get()

        runBlocking {
            withTimeout(5000) {
                val databaseCards =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                        .magicCardDao()
                        .getAllCardsByPossession(CardPossession.OWNED.toString())

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

        userRTDB.getListOfFullCardsInfos(
            SignIn.getSignIn().getUserUID()!!,
            CardPossession.OWNED
        ).thenAccept {
            assertThat("cards contains card1", it.contains(card1))
            assertThat("cards contains card2", it.contains(card2))
        }.exceptionally {
            assertThat("cards is empty", false)
            null
        }.get()
    }
}
