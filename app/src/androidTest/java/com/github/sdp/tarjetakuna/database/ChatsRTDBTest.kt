package com.github.sdp.tarjetakuna.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.github.sdp.tarjetakuna.utils.Utils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith

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

    val chatsRTDB = ChatsRTDB()


    @Before
    fun setUp() {
//        // make sure db is empty of chats and messages
//        val task1 = chatsRTDB.clearChats()
//        Utils.waitUntilTrue(10, 100) { task1.isComplete }
//
//        val task2 = MessagesRTDB().clearMessages()
//        Utils.waitUntilTrue(10, 100) { task2.isComplete }
    }

    @After
    fun tearDown() {
//        // make sure db is empty of chats and messages
//        val task1 = chatsRTDB.clearChats()
//        Utils.waitUntilTrue(10, 100) { task1.isComplete }
//
//        val task2 = MessagesRTDB().clearMessages()
//        Utils.waitUntilTrue(10, 100) { task2.isComplete }
    }

    @Test
    fun test_addGetChatDB() {

        // add chat in db
        val task1 = chatsRTDB.addChat(ChatsData.fakeDBChat1)
        Utils.waitUntilTrue(10, 100) { task1.isComplete }

        // get chat from db
        chatsRTDB.getChat(ChatsData.fakeDBChat1.uid).thenAccept { chat ->
            assertThat("chat is not fakeDBChat1", chat, equalTo(ChatsData.fakeDBChat1))
        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()

    }

    @Test
    fun test_removeChatDB() {
        // add message in db
        val task1 = chatsRTDB.addChat(ChatsData.fakeDBChat2)
        Utils.waitUntilTrue(10, 100) { task1.isComplete }

        // get message from db
        chatsRTDB.getChat(ChatsData.fakeDBChat2.uid).thenAccept { message ->
            assertThat("chat is not fakeDBChat2", message, equalTo(ChatsData.fakeDBChat2))
        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()


        // remove message from db
        val task2 = chatsRTDB.removeChat(ChatsData.fakeDBChat2.uid)
        Utils.waitUntilTrue(10, 100) { task2.isComplete }

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
    fun test_addGetChat() {
        // add chat in db
        val mChat = ChatsData.fakeChat1
        waitForChatToBeAdded(mChat)

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
    fun test_addMessageToChat() {
        // add chat in db
        val mChat = ChatsData.fakeChat2
        waitForChatToBeAdded(mChat)

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
        // TODO test_addListenersOnChats
    }

    private fun waitForChatToBeAdded(chat: Chat) {
        val tasks1 = chatsRTDB.addChatToDatabase(chat)
        Utils.waitUntilTrue(10, 100) {
            var complete = false
            for (task in tasks1) {
                if (task.isComplete) {
                    complete = true
                    break
                }
            }
            complete
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
