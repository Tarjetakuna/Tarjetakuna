package com.github.sdp.tarjetakuna.model

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for [User]
 */
@RunWith(AndroidJUnit4::class)
class UserTest {

    private val validUsername = "validEmail@google.com"
    private val invalidUsername1 = "invalidEmail"
    private val invalidUsername2 = "invalidEmail@"
    private val invalidUsername3 = "invalidEmail@google."
    private val validUID = "onetwothree"
    private val validListOfCards = mutableListOf<DBMagicCard>()
    private val validCoordinates = Coordinates(45.0f, 75.0f)
    private val validUser =
        User(
            validUID,
            validUsername,
            validListOfCards,
            validCoordinates,
            FirebaseDB()
        )
    private val card = CommonMagicCard.aeronautTinkererCard
    private val card2 = CommonMagicCard.venomousHierophantCard
    private val card3 = CommonMagicCard.solemnOfferingCard
    private val fbcard = DBMagicCard(card, CardPossession.OWNED)


    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    @Test
    fun blankEmailIsInvalid() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            val cards = mutableListOf<DBMagicCard>()
            cards.addAll(validListOfCards)
            User(validUID, "", cards, validCoordinates)
        }
    }

    @Test
    fun notAnEmailIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            User(validUID, invalidUsername1, validListOfCards, validCoordinates)
        }

        assertThrows(IllegalArgumentException::class.java) {
            User(validUID, invalidUsername2, validListOfCards, validCoordinates)
        }

        assertThrows(IllegalArgumentException::class.java) {
            User(validUID, invalidUsername3, validListOfCards, validCoordinates)
        }
    }

    @Test
    fun blankUsernameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            User("", validUsername, validListOfCards, validCoordinates)
        }
    }

    @Test
    fun validUser() {
        val user = User(validUID, validUsername, validListOfCards, validCoordinates)
        assertThat(user.uid, CoreMatchers.`is`(validUID))
        assertThat(user.username, CoreMatchers.`is`(validUsername))
        assertThat(user.cards, CoreMatchers.`is`(validListOfCards))
        assertThat(user.location, CoreMatchers.`is`(validCoordinates))

    }

    @Test
    fun addCardTest() {
        runBlocking {
            validUser.addCard(card, CardPossession.OWNED)
            var count = 0L
            withTimeout(5000) {
                fbEmulator.fb.reference
                    .child("users")
                    .child(validUID)
                    .child("owned")
                    .child(fbcard.getFbKey()).get().addOnSuccessListener {
                        count = it.value as Long
                    }
            }
            delay(1000)
            assertThat(count, CoreMatchers.`is`(1L))

        }
        assertThat(
            fbEmulator.fb.reference
                .child("users")
                .child(validUID)
                .child("owned")
                .child(fbcard.getFbKey())
                .key,
            CoreMatchers.`is`("MT1543")
        )

    }

    @Test
    fun getCardExistsTest() {
        validUser.addCard(card, CardPossession.WANTED)
        var magiccard = MagicCard()
        runBlocking {
            //TODO problem: not even getting through here
            validUser.getCard(card.set.code, card.number, CardPossession.WANTED).exceptionally {
                assertThat("helsdflo", CoreMatchers.`is`("hhi"))
                null
            }
                .thenAccept {
                    val fbCard = Gson().fromJson(it.value as String, DBMagicCard::class.java)
                    magiccard = fbCard.toMagicCard()
                    assertThat("hello", CoreMatchers.`is`("hhi"))
                }
            delay(1000)
            //assertThat(magiccard, CoreMatchers.`is`(card))
        }
    }

    @Test
    fun getCardDoesntExistsTest() {
        //TODO
    }
}
