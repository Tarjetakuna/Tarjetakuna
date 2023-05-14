package com.github.sdp.tarjetakuna.model

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.utils.FBEmulator
import org.hamcrest.CoreMatchers
import org.junit.Assert
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
        Assert.assertThrows(IllegalArgumentException::class.java) {
            User(validUID, invalidUsername1, validListOfCards, validCoordinates)
        }

        Assert.assertThrows(IllegalArgumentException::class.java) {
            User(validUID, invalidUsername2, validListOfCards, validCoordinates)
        }

        Assert.assertThrows(IllegalArgumentException::class.java) {
            User(validUID, invalidUsername3, validListOfCards, validCoordinates)
        }
    }

    @Test
    fun blankUsernameIsInvalid() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
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
        val user =
            User(
                validUID,
                validUsername,
                validListOfCards,
                validCoordinates,
                FirebaseDB()
            )
        fbEmulator.fb.reference.child("bonjour").setValue("WORLD")
        val card = CommonMagicCard.aeronautTinkererCard
        val fbcard = DBMagicCard(card, CardPossession.OWNED)
        user.addCard(card, CardPossession.OWNED)
        assertThat(
            fbEmulator.fb.reference
                .child("users")
                .child(validUID)
                .child("owned")
                .child(fbcard.code + fbcard.number)
                .key,
            CoreMatchers.`is`("MT1543")
        )
    }
}
