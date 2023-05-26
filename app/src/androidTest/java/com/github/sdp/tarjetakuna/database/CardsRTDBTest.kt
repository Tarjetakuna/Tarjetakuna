package com.github.sdp.tarjetakuna.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.utils.Utils
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CompletableFuture

@RunWith(AndroidJUnit4::class)
class CardsRTDBTest {


    private lateinit var cardsRTDB: CardsRTDB

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()
        cardsRTDB = CardsRTDB(FirebaseDB())
        val task = FirebaseDB().clearDatabase()
        Utils.waitUntilTrue(10, 100) { task.isComplete }
    }

    @After
    fun tearDown() {
        val task = FirebaseDB().clearDatabase()
        Utils.waitUntilTrue(10, 100) { task.isComplete }
    }

    @Test
    fun getMultipleCardsFromGlobalCollectionWorks() {
        cardsRTDB.addCardToGlobalCollection(CommonMagicCard.aeronautTinkererCard.toDBMagicCard())
            .get()
        cardsRTDB.addCardToGlobalCollection(CommonMagicCard.solemnOfferingCard.toDBMagicCard())
            .get()
        cardsRTDB.addCardToGlobalCollection(CommonMagicCard.venomousHierophantCard.toDBMagicCard())
            .get()

        val listOfFutures =
            cardsRTDB.getMultipleCardsFromGlobalCollection(
                listOf(
                    CommonMagicCard.aeronautTinkererCard.toDBMagicCard().getFbKey(),
                    CommonMagicCard.solemnOfferingCard.toDBMagicCard().getFbKey(),
                )
            )

        CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenRun {
            assertThat("should have 3 cards", listOfFutures.size == 2)
        }
    }


    @Test
    fun addMultipleCardsToCollectionWorks() {
        val cards = listOf(
            CommonMagicCard.aeronautTinkererCard.toDBMagicCard(),
            CommonMagicCard.solemnOfferingCard.toDBMagicCard(),
            CommonMagicCard.venomousHierophantCard.toDBMagicCard()
        )

        cardsRTDB.addMultipleCardsToGlobalCollection(cards).get()

        val listOfFutures =
            cardsRTDB.getMultipleCardsFromGlobalCollection(cards.map { it.getFbKey() })

        CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenRun {
            assertThat("should have 3 cards", listOfFutures.size == 3)
        }

    }

    @Test
    fun getAllCardsFromCollectionWorks() {
        val card1 = CommonMagicCard.aeronautTinkererCard.toDBMagicCard()
        val card2 = CommonMagicCard.solemnOfferingCard.toDBMagicCard()
        val card3 = CommonMagicCard.venomousHierophantCard.toDBMagicCard()
        cardsRTDB.addCardToGlobalCollection(card1).get()
        cardsRTDB.addCardToGlobalCollection(card2).get()
        cardsRTDB.addCardToGlobalCollection(card3).get()

        val future = cardsRTDB.getAllCardsFromGlobalCollection().get()
        val cards = future.value as HashMap<*, *>
        assertThat("should have 3 cards", cards.size == 3)
    }

    @Test
    fun removeCardFromGlobalCollectionWorks() {
        val card1 = CommonMagicCard.aeronautTinkererCard.toDBMagicCard()
        val card2 = CommonMagicCard.solemnOfferingCard.toDBMagicCard()

        cardsRTDB.addCardToGlobalCollection(card1).get()
        cardsRTDB.addCardToGlobalCollection(card2).get()

        cardsRTDB.removeCardFromGlobalCollection(card1.getFbKey()).get()

        val future = cardsRTDB.getAllCardsFromGlobalCollection().get()
        val cards = future.value as HashMap<*, *>
        assertThat("should have 1 card", cards.size == 1)
    }

    @Test
    fun removeMultipleCardsFromCollectionWorks() {
        val card1 = CommonMagicCard.aeronautTinkererCard.toDBMagicCard()
        val card2 = CommonMagicCard.solemnOfferingCard.toDBMagicCard()
        val card3 = CommonMagicCard.venomousHierophantCard.toDBMagicCard()
        cardsRTDB.addCardToGlobalCollection(card1).get()
        cardsRTDB.addCardToGlobalCollection(card2).get()
        cardsRTDB.addCardToGlobalCollection(card3).get()

        cardsRTDB.removeMultipleCardsFromGlobalCollection(
            listOf(
                card1.getFbKey(),
                card2.getFbKey()
            )
        ).get()

        val future = cardsRTDB.getAllCardsFromGlobalCollection().get()
        val cards = future.value as HashMap<*, *>
        assertThat("should have 1 card", cards.size == 1)
    }
}
