package com.github.sdp.tarjetakuna.model

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.ChatsRTDB
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.DBMessage
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.MessagesRTDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.google.android.gms.tasks.Tasks
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.equalTo
import org.junit.*
import org.junit.Assert.assertThrows
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * Tests for [User]
 */
@RunWith(AndroidJUnit4::class)
class UserTest {
    @get:Rule
    val globalTimeout = Timeout(30, TimeUnit.SECONDS)

    private val validUsername = "validEmail@google.com"
    private val invalidUsername1 = "invalidEmail"
    private val invalidUsername2 = "invalidEmail@"
    private val invalidUsername3 = "invalidEmail@google."
    private val validUID = "onetwothree"
    private val validListOfCards = mutableListOf<DBMagicCard>()
    private val validCoordinates = Coordinates(45.0, 75.0)
    private val validUser =
        User(
            validUID,
            validUsername,
            validListOfCards,
            validCoordinates
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

    @Before
    fun setUp() {
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
    }

    @After
    fun tearDown() {
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
    }

    @Test
    fun blankEmailIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
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
        assert(validUser.addCard(card, CardPossession.OWNED).get())
        assert(validUser.addCard(card, CardPossession.OWNED).get())

        runBlocking {
            var count = 0L
            withTimeout(1000) {
                fbEmulator.fb.reference
                    .child("users")
                    .child(validUID)
                    .child("owned")
                    .child(fbcard.getFbKey()).get().addOnSuccessListener {
                        count = it.value as Long
                    }
            }
            delay(1000)
            assertThat(count, CoreMatchers.`is`(2L))
        }
    }

    @Test
    fun removeCardTest() {
        runBlocking {
            validUser.addCard(card, CardPossession.OWNED)
            validUser.addCard(card, CardPossession.OWNED)
            validUser.addCard(card, CardPossession.OWNED)
            validUser.removeCard(card, CardPossession.OWNED)
            var count = 0L
            withTimeout(1000) {
                fbEmulator.fb.reference
                    .child("users")
                    .child(validUID)
                    .child("owned")
                    .child(fbcard.getFbKey()).get().addOnSuccessListener {
                        count = it.value as Long
                    }
            }
            delay(1000)
            assertThat(count, CoreMatchers.`is`(2L))

        }
    }

    @Test
    fun getCardExistsTest() {
        assert(validUser.addCard(card, CardPossession.WANTED).get())

        val futureCard1 = validUser.getCard(card.set.code, card.number, CardPossession.WANTED)

        val actualCard1 = futureCard1.get()
        val fbCard1 = Gson().fromJson(actualCard1.value as String, DBMagicCard::class.java)
        val magicCard1 = fbCard1.toMagicCard()
        assertThat(magicCard1, CoreMatchers.`is`(card))
    }

    @Test
    fun getCardDoesNotExistTest() {
        runBlocking {
            assertThrows(ExecutionException::class.java) {
                val card = validUser.getCard("blablabla", 1, CardPossession.OWNED)
                val getCard = card.get(5, TimeUnit.SECONDS)
            }
        }
    }

    @Test
    fun test_getNoChat() {
        val chats = validUser.getChats().get()
        assertThat("no chat in db for user", chats.size, CoreMatchers.`is`(0))
    }

    @Test
    fun test_newChat() {
        val user: User = ChatsData.fakeUser1
        val mChat: Chat = ChatsData.fakeChat1

        // create new chat
        user.newChat(mChat).get(1, TimeUnit.SECONDS)

        // get chats
        val chats = user.getChats().get()
        assertThat("nb of chats is different", chats.size, equalTo(user.chats.size))

        // check if the chat are the same
        user.chats.sortBy { it.uid }
        val sortedChats = chats.toList().sortedBy { it.uid }

        for (i in user.chats.indices) {
            assertThat("chat uid ", sortedChats[i].uid, equalTo(user.chats[i].uid))
            assertThat("user1 id", sortedChats[i].user1, equalTo(user.chats[i].user1))
            assertThat("user2 id", sortedChats[i].user2, equalTo(user.chats[i].user2))
            assertThat(
                "nb of messages in chat ",
                sortedChats[i].messages.size,
                equalTo(user.chats[i].messages.size)
            )
        }
    }

    @Test
    fun test_sendMessage() {
        val userRTDB = UserRTDB(FirebaseDB())
        val chatsRTDB = ChatsRTDB()
        val messagesRTDB = MessagesRTDB()

        val user1: User = ChatsData.fakeUser1
        val user2: User = ChatsData.fakeUser2

        val message = "Hello"
        val pair = user1.sendMessageToUser(message, user2.uid).get(1, TimeUnit.SECONDS)

        println("dbChat ${pair.first}")
        println("dbMessage ${pair.second}")

        val dbChats1 = userRTDB.getChatsFromUser(user1.uid).get(1, TimeUnit.SECONDS)
        val dbChats2 = userRTDB.getChatsFromUser(user2.uid).get(1, TimeUnit.SECONDS)

        assertThat("chats user1 length not 1", dbChats1.size, equalTo(1))
        assertThat("chats user1 and user2 are different", dbChats1, equalTo(dbChats2))
        assertThat("chats are different", pair.first.toString(), equalTo(dbChats1[0].toString()))

        val chat1 = chatsRTDB.getChatFromDatabase(dbChats1[0].uid).get(1, TimeUnit.SECONDS)
        val dbMessage = messagesRTDB.getMessage(dbChats1[0].messages[0]).get(1, TimeUnit.SECONDS)

        assertThat("message is different", pair.second.toString(), equalTo(dbMessage.toString()))
        assertThat(
            "message is different",
            pair.second.toString(),
            equalTo(DBMessage.toDBMessage(chat1.messages[0]).toString())
        )
    }

//    @Test
//    fun test_sendMessages() {
//        val userRTDB = UserRTDB(FirebaseDB())
//        val chatsRTDB = ChatsRTDB()
//        val messagesRTDB = MessagesRTDB()
//
//        val user1: User = ChatsData.fakeUser1
//        val user2: User = ChatsData.fakeUser2
//
//        var chats1_updated = 0
//        var chats2_updated = 0
//
//        user1.addChatsListener { chats1_updated++ }
//        user2.addChatsListener { chats2_updated++ }
//
//        val message = "Hello"
//        val pair1 = user1.sendMessageToUser(message, user2.uid).get(1, TimeUnit.SECONDS)
//
//        Utils.waitUntilTrue(50, 20) { chats1_updated >= 2 && chats2_updated >= 2 }
//        val pair2 = user2.sendMessageToUser("$message back", user1.uid).get(1, TimeUnit.SECONDS)
//
//        println("dbChat ${pair1.first}")
//        println("dbMessage ${pair1.second}")
//
//        val dbChats1 = userRTDB.getChatsFromUser(user1.uid).get(1, TimeUnit.SECONDS)
//        val dbChats2 = userRTDB.getChatsFromUser(user2.uid).get(1, TimeUnit.SECONDS)
//
//        assertThat("chats user1 length not 1", dbChats1.size, equalTo(1))
//        assertThat("chats user1 and user2 are different", dbChats1, equalTo(dbChats2))
//        assertThat("chats are different", pair1.first.toString(), equalTo(dbChats1[0].toString()))
//
//        val chat1 = chatsRTDB.getChatFromDatabase(dbChats1[0].uid).get(1, TimeUnit.SECONDS)
//        val dbMessage = messagesRTDB.getMessage(dbChats1[0].messages[0]).get(1, TimeUnit.SECONDS)
//
//        assertThat("message is different", pair1.second.toString(), equalTo(dbMessage.toString()))
//        assertThat(
//            "message is different",
//            pair1.second.toString(),
//            equalTo(DBMessage.toDBMessage(chat1.messages[0]).toString())
//        )
//    }
}
