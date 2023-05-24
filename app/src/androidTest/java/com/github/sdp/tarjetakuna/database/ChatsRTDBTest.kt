package com.github.sdp.tarjetakuna.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.github.sdp.tarjetakuna.utils.Utils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * Tests for [ChatsRTDB]
 */
@RunWith(AndroidJUnit4::class)
class ChatsRTDBTest {
    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    private val chatsRTDB = ChatsRTDB()


    @Before
    fun setUp() {
        // make sure db is empty of messages
        val task = FirebaseDB().clearDatabase()
        Utils.waitUntilTrue(10, 100) { task.isComplete }
    }

    @After
    fun tearDown() {
        // make sure db is empty of messages
        val task = FirebaseDB().clearDatabase()
        Utils.waitUntilTrue(10, 100) { task.isComplete }
    }

    @Test
    fun test_AddAndGet_Chat() {

        // add chat in db
        chatsRTDB.addChat(ChatsData.fakeDBChat1).get(1, TimeUnit.SECONDS)

        // get chat from db
        chatsRTDB.getChat(ChatsData.fakeDBChat1.uid).thenAccept { chat ->
            assertThat("chat is not fakeDBChat1", chat, equalTo(ChatsData.fakeDBChat1))
        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()

    }

    @Test
    fun test_addGet_Chats() {

        val mDBChats = arrayListOf(ChatsData.fakeDBChat1, ChatsData.fakeDBChat2).sortedBy { it.uid }

        // add chat in db
        chatsRTDB.addChat(ChatsData.fakeDBChat1)
            .thenCompose { chatsRTDB.addChat(ChatsData.fakeDBChat2) }
            .get(1, TimeUnit.SECONDS)

        // get chat from db
        chatsRTDB.getChats(mDBChats.map { it.uid }).thenAccept { chats ->
            assertThat("same number of chat", chats.size, equalTo(mDBChats.size))
            for (i in chats.indices) {
                assertThat("chat is not fakeDBChat$i", chats[i], equalTo(mDBChats[i]))
            }
        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()

    }

    @Test
    fun test_removeChat() {
        // add message in db
        val future1 = chatsRTDB.addChat(ChatsData.fakeDBChat2)
        future1.get(1, TimeUnit.SECONDS)

        // get message from db
        chatsRTDB.getChat(ChatsData.fakeDBChat2.uid).thenAccept { message ->
            assertThat("chat is not fakeDBChat2", message, equalTo(ChatsData.fakeDBChat2))
        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()


        // remove message from db
        val future2 = chatsRTDB.removeChat(ChatsData.fakeDBChat2.uid)
        future2.get(1, TimeUnit.SECONDS)

        // get chat from db
        chatsRTDB.getChat(ChatsData.fakeDBChat2.uid).thenAccept { chat ->
            assertThat("chat $chat should have been removed", false)
        }.exceptionally {
            assertThat(
                "error '$it' should be NoSuchFieldException",
                it.cause!!::class.java,
                equalTo(NoSuchFieldException::class.java)
            )
            null
        }.get()
    }

    @Test
    fun test_addAndGet_ChatFromDatabase() {
        // add chat in db
        val mChat = ChatsData.fakeChat1
        val future = chatsRTDB.addChatToDatabase(mChat)
        future.get(1, TimeUnit.SECONDS)

        // get chat from db and check
        chatsRTDB.getChatFromDatabase(mChat.uid).thenAccept { chat ->
            assertThat("chat should not be valid", chat.valid, equalTo(false))
            assertThat("chat id is different", chat.uid, equalTo(mChat.uid))
            assertThat("user1 id is different", chat.user1.uid, equalTo(mChat.user1.uid))
            assertThat("user2 id is different", chat.user2.uid, equalTo(mChat.user2.uid))
            for (message in chat.messages) {

                // check message in chat
                assertThat(
                    "chat should contain message",
                    mChat.messages.map { it.uid }.contains(message.uid),
                    equalTo(true)
                )
                val filtered = chat.messages.filter { it.uid == message.uid }
                val mMessage = mChat.messages.filter { it.uid == message.uid }[0]

                assertThat("message should be once only in db chat", filtered.size, equalTo(1))

                checkMessageInChat(filtered[0], mMessage)
            }

        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()
    }

    @Test
    fun test_addAndGet_ChatsFromDatabase() {
        // add chat in db
        val mChat1 = ChatsData.fakeChat1
        val mChat2 = ChatsData.fakeChat2
        val future = chatsRTDB.addChatToDatabase(mChat1)
            .thenApply { chatsRTDB.addChatToDatabase(mChat2) }
        future.get(1, TimeUnit.SECONDS)

        val mChats = arrayListOf(mChat1, mChat2).sortedBy { it.uid }

        // get chat from db and check
        chatsRTDB.getChatsFromDatabase(arrayListOf(mChat1.uid, mChat2.uid)).thenAccept { it ->
            val chats = it.sortedBy { it.uid }
            assertThat("chats size is different", chats.size, equalTo(mChats.size))

            for (i in mChats.indices) {
                val chat = chats[i]
                val mChat = mChats[i]

                assertThat("chat should not be valid", chat.valid, equalTo(false))
                assertThat("chat id is different", chat.uid, equalTo(mChat.uid))
                assertThat("user1 id is different", chat.user1.uid, equalTo(mChat.user1.uid))
                assertThat("user2 id is different", chat.user2.uid, equalTo(mChat.user2.uid))
                for (message in chat.messages) {
                    // check message in chat
                    assertThat(
                        "chat should contain message",
                        mChat.messages.map { it.uid }.contains(message.uid),
                        equalTo(true)
                    )
                    val filtered = chat.messages.filter { it.uid == message.uid }
                    val mMessage = mChat.messages.filter { it.uid == message.uid }[0]

                    assertThat("message should be once only in db chat", filtered.size, equalTo(1))

                    checkMessageInChat(filtered[0], mMessage)
                }
            }


        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()
    }

    @Test
    fun test_addMessageToChat() {
        // add chat in db
        val mChat = ChatsData.fakeChat2
        chatsRTDB.addChatToDatabase(mChat).get(2, TimeUnit.SECONDS)

        // add message to chat
        val mMessage = ChatsData.fakeDBMessage1_1
        val future = chatsRTDB.addMessageToChat(mChat.uid, mMessage)
        Utils.waitUntilTrue(10, 100) { future.isDone }

        assertThat(
            "future should complete correctly",
            future.isCompletedExceptionally,
            equalTo(false)
        )

    }

    @Test
    fun test_addListenersOnChats() {
        // add chat in db
        val mChat = ChatsData.fakeChat2
        chatsRTDB.addChatToDatabase(mChat).get(1, TimeUnit.SECONDS)

        // add chat listener to chat
        var called = false
        chatsRTDB.addChatListener(mChat.uid) { dbChat ->
            if (dbChat == null) {
                assertThat("chat should not be null", false)
                return@addChatListener
            }
            assertThat("chat id is different", dbChat.uid, equalTo(mChat.uid))
            assertThat("user1 id is different", dbChat.user1, equalTo(mChat.user1.uid))
            assertThat("user2 id is different", dbChat.user2, equalTo(mChat.user2.uid))

            if (dbChat.messages.size > mChat.messages.size) {
                assertThat(
                    "new message added to chat",
                    dbChat.messages.size,
                    equalTo(mChat.messages.size + 1)
                )
                called = true
            }
        }

        // add message to chat to trigger listener
        val mMessage = ChatsData.fakeDBMessage1_1
        val future = chatsRTDB.addMessageToChat(mChat.uid, mMessage)
        Utils.waitUntilTrue(10, 100) { future.isDone }

        // check that listener was called
        Utils.waitUntilTrue(10, 100) { called }
        assertThat("listener should have been called", called, equalTo(true))

        // remove listener
        chatsRTDB.removeChatListener(mChat.uid)
    }

    @Test
    fun test_removeChatListener_noListener() {
        val mChat = ChatsData.fakeChat2
        chatsRTDB.removeChatListener(mChat.uid)

        assert(true)
    }

    @Test
    fun test_clearChats() {
        val mDBChats = arrayListOf(ChatsData.fakeDBChat1, ChatsData.fakeDBChat2)

        // add chats in db
        mDBChats.map { chatsRTDB.addChat(it) }
            .forEach { it.get(1, TimeUnit.SECONDS) }

        // control before removing chats from db
        val dbChats0 = chatsRTDB.getChats(mDBChats.map { it.uid }).get(1, TimeUnit.SECONDS)
        assertThat("chats size not correct", dbChats0.size, equalTo(mDBChats.size))

        // clear chats
        chatsRTDB.clearChats().get(1, TimeUnit.SECONDS)

        // get chats from db
        assertThrows(ExecutionException::class.java) {
            chatsRTDB.getChats(mDBChats.map { it.uid }).get(1, TimeUnit.SECONDS)
        }
    }

    private fun checkMessageInChat(inChat: Message, expected: Message) {
        assertThat("message should not be valid", inChat.valid, equalTo(false))
        assertThat("id is different", inChat.uid, equalTo(expected.uid))
        assertThat("content is different", inChat.content, equalTo(expected.content))
        assertThat("sender id is different", inChat.sender.uid, equalTo(expected.sender.uid))
        assertThat("receiver id is different", inChat.receiver.uid, equalTo(expected.receiver.uid))
    }
}
