package com.github.sdp.tarjetakuna.model

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.Database
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Singleton

/**
 * Tests for [User]
 */
@RunWith(AndroidJUnit4::class)
class UserTest {

    private val validUsername = "validEmail@google.com"
    private val invalidUsername1 = "invalidEmail"
    private val invalidUsername2 = "invalidEmail@"
    private val invalidUsername3 = "invalidEmail@google."
    private val validUID = "validUID"
    private val validListOfCards = mutableListOf<DBMagicCard>()
    private val validCoordinates = Coordinates(45.0f, 75.0f)

    private lateinit var mockedDB: Database

    @Singleton
    private fun provideEmulator(): DatabaseReference {
        val fb = Firebase.database
        fb.useEmulator("10.0.2.2", 9000)
        return fb.reference

    }

    @Before
    fun setup() {
        mockedDB = Mockito.mock(Database::class.java)
        mockedDB.provideEmulator()
//        Mockito.`when`(mockedDB.returnDatabaseReference())
//            .thenReturn(provideEmulator())
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
        val user = User(validUID, validUsername, validListOfCards, validCoordinates)
        val card = CommonMagicCard.aeronautTinkererCard
        val fbcard = DBMagicCard(card, CardPossession.OWNED)
        user.addCard(card, CardPossession.OWNED)
        assertThat(user.cards[0].toMagicCard(), CoreMatchers.`is`(card))
//        assertThat(user.cards.contains(fbcard), CoreMatchers.`is`(true))
    }
}
