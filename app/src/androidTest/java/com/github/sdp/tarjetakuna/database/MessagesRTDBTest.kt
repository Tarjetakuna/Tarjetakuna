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
 * Tests for [MessagesRTDB]
 */
@RunWith(AndroidJUnit4::class)
class MessagesRTDBTest {

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    private val messagesRTDB = MessagesRTDB()

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
    fun test_addGetMessageDB() {

        // add message in db
        messagesRTDB.addMessage(ChatsData.fakeDBMessage1_1).get(1, TimeUnit.SECONDS)

        // get message from db
        messagesRTDB.getMessage(ChatsData.fakeDBMessage1_1.uid).thenAccept { message ->
            assertThat(
                "message is not fakeDBMessage1_1",
                message,
                equalTo(ChatsData.fakeDBMessage1_1)
            )
        }.exceptionally {
            assertThat("error $it", false)
            null
        }.get()
    }

    @Test
    fun test_removeMessageDB() {
        // add message in db
        messagesRTDB.addMessage(ChatsData.fakeDBMessage1_2).get(1, TimeUnit.SECONDS)

        // get message from db
        messagesRTDB.getMessage(ChatsData.fakeDBMessage1_2.uid).thenAccept { message ->
            assertThat(
                "message is not fakeDBMessage1_2",
                message,
                equalTo(ChatsData.fakeDBMessage1_2)
            )
        }.exceptionally {
            assertThat("error $it", false)
            null
        }.get()

        // remove message from db
        val future2 = messagesRTDB.removeMessage(ChatsData.fakeDBMessage1_2.uid)
        Utils.waitUntilTrue(10, 100) { future2.isDone }

        // get message from db
        messagesRTDB.getMessage(ChatsData.fakeDBMessage1_2.uid).thenAccept { message ->
            assertThat("message $message should have been removed", false)
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
    fun test_addGetMessage() {

        // add message in db
        messagesRTDB.addMessageToDatabase(ChatsData.fakeMessage1_1).get(1, TimeUnit.SECONDS)

        // get message from db
        messagesRTDB.getMessageFromDatabase(ChatsData.fakeMessage1_1.uid).thenAccept { message ->
            assertThat(
                "message id is different",
                message.uid,
                equalTo(ChatsData.fakeMessage1_1.uid)
            )
            assertThat(
                "message content is different",
                message.content,
                equalTo(ChatsData.fakeMessage1_1.content)
            )
            assertThat(
                "message sender id is different",
                message.sender.uid,
                equalTo(ChatsData.fakeMessage1_1.sender.uid)
            )
            assertThat(
                "message receiver id is different",
                message.receiver.uid,
                equalTo(ChatsData.fakeMessage1_1.receiver.uid)
            )
            assertThat(
                "message should not be valid",
                message.valid,
                equalTo(false)
            )
        }.exceptionally {
            assertThat("error $it", false)
            null
        }.get()
    }

    @Test
    fun test_addGetMessages() {

        val hashMap = hashMapOf<String, Message>(
            ChatsData.fakeMessage1_3.uid to ChatsData.fakeMessage1_3,
            ChatsData.fakeMessage1_4.uid to ChatsData.fakeMessage1_4,
        )

        // add messages to db
        for (kv in hashMap.toList()) {
            messagesRTDB.addMessageToDatabase(kv.second).get(1, TimeUnit.SECONDS)
        }

        // get messages from db
        messagesRTDB.getMessagesFromDatabase(hashMap.toList().map { it.first })
            .thenAccept { messages ->
                for (message in messages) {
                    assertThat(
                        "message should not be valid",
                        message.valid,
                        equalTo(false)
                    )

                    assertThat("message should be in hashSet", hashMap.containsKey(message.uid))

                    val msgFromHashSet = hashMap[message.uid]!!

                    assertThat(
                        "message id is different",
                        message.uid,
                        equalTo(msgFromHashSet.uid)
                    )
                    assertThat(
                        "message content is different",
                        message.content,
                        equalTo(msgFromHashSet.content)
                    )
                    assertThat(
                        "message sender id is different",
                        message.sender.uid,
                        equalTo(msgFromHashSet.sender.uid)
                    )
                    assertThat(
                        "message receiver id is different",
                        message.receiver.uid,
                        equalTo(msgFromHashSet.receiver.uid)
                    )
                }

            }.exceptionally {
                assertThat("error $it", false)
                null
            }.get()
    }

    @Test
    fun test_clearMessages() {
        val mDBMessages = arrayListOf(ChatsData.fakeDBMessage1_1, ChatsData.fakeDBMessage1_2)

        // add chats in db
        mDBMessages.map { messagesRTDB.addMessage(it) }
            .forEach { it.get(1, TimeUnit.SECONDS) }

        // control before removing messages from db
        mDBMessages.map {
            messagesRTDB.getMessage(it.uid).get(1, TimeUnit.SECONDS)
        }.forEach {
            assertThat("message should be in db", it != null)
        }

        // clear messages
        messagesRTDB.clearMessages().get(1, TimeUnit.SECONDS)

        // get messages from db
        mDBMessages.map {
            assertThrows(ExecutionException::class.java) {
                messagesRTDB.getMessage(it.uid).get(1, TimeUnit.SECONDS)
            }
        }
    }

}
