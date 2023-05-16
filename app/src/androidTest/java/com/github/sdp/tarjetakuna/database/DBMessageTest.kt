package com.github.sdp.tarjetakuna.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.FBEmulator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for [DBMessage]
 */
@RunWith(AndroidJUnit4::class)
class DBMessageTest {
    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    @Test
    fun test_toDBMessage() {
        val message: Message = ChatsData.fakeMessage1_1
        val dbMessage: DBMessage = DBMessage.toDBMessage(message)

        assertThat("Message id is different", dbMessage.uid, equalTo(message.uid))
        assertThat("Sender is different", dbMessage.sender, equalTo(message.sender.uid))
        assertThat(
            "Receiver is different",
            dbMessage.receiver,
            equalTo(message.receiver.uid)
        )
        assertThat("Content is different", dbMessage.content, equalTo(message.content))
        assertThat(
            "Timestamp is different",
            dbMessage.timestamp,
            equalTo(message.timestamp)
        )
    }

    @Test
    fun test_toMessage() {
        val dbMessage: DBMessage = ChatsData.fakeDBMessage1_1
        val message: Message = DBMessage.fromDBMessage(dbMessage)

        assertThat("Message is valid", message.valid, equalTo(false))

        assertThat("Message id is different", message.uid, equalTo(dbMessage.uid))
        assertThat("Sender is different", message.sender.uid, equalTo(dbMessage.sender))
        assertThat(
            "Receiver is different",
            message.receiver.uid,
            equalTo(dbMessage.receiver)
        )
        assertThat("Content is different", message.content, equalTo(dbMessage.content))
        assertThat(
            "Timestamp is different",
            message.timestamp,
            equalTo(dbMessage.timestamp)
        )

    }
}
