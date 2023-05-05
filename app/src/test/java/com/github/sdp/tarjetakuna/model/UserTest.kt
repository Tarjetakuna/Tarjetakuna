package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.database.DBMagicCard
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [User]
 */
class UserTest {

    private val validEmail = "validEmail@google.com"
    private val invalidEmail1 = "invalidEmail"
    private val invalidEmail2 = "invalidEmail@"
    private val invalidEmail3 = "invalidEmail@google."
    private val validUsername = "validUsername"
    private val validListOfCards = listOf<DBMagicCard>()
    private val validCoordinates = Coordinates(45.0f, 75.0f)

    @Test
    fun blankEmailIsInvalid() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            User("", validUsername, validListOfCards, validCoordinates)
        }
    }

    @Test
    fun notAnEmailIsInvalid() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            User(invalidEmail1, validUsername, validListOfCards, validCoordinates)
        }

        Assert.assertThrows(IllegalArgumentException::class.java) {
            User(invalidEmail2, validUsername, validListOfCards, validCoordinates)
        }

        Assert.assertThrows(IllegalArgumentException::class.java) {
            User(invalidEmail3, validUsername, validListOfCards, validCoordinates)
        }
    }

    @Test
    fun blankUsernameIsInvalid() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            User(validEmail, "", validListOfCards, validCoordinates)
        }
    }

    @Test
    fun validUser() {
        val user = User(validEmail, validUsername, validListOfCards, validCoordinates)
        assertEquals(user.email, validEmail)
        assertEquals(user.username, validUsername)
        assertEquals(user.cards, validListOfCards)
        assertEquals(user.location, validCoordinates)
    }
}
