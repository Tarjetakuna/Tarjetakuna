package com.github.sdp.tarjetakuna.model

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.utils.ChatsData
import junit.framework.TestCase
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class MessageTest {
    @Test
    fun test_FromUIDOnly() {
        val timeBefore = Date()
        val message = Message("MessageUID")
        val timeAfter = Date()
        assertThat("message uid incorrect", message.uid, equalTo("MessageUID"))
        checkUsersEqual("sender incorrect", message.sender, User(""))
        checkUsersEqual("receiver incorrect", message.receiver, User(""))
        assertThat("messages not empty", message.content, equalTo(""))
        assertThat("timestamp to big", message.timestamp, lessThanOrEqualTo(timeAfter))
        assertThat("timestamp to small", message.timestamp, greaterThanOrEqualTo(timeBefore))
    }

    @Test
    fun test_clone() {
        val message = Message("MessageUID")
        val clone = message.clone()
        TestCase.assertNotSame("clone is the same", clone, message)

        assertThat("message uid incorrect", clone.uid, equalTo(message.uid))
        checkUsersEqual("sender incorrect", clone.sender, message.sender)
        checkUsersEqual("receiver incorrect", clone.receiver, message.receiver)
        assertThat("messages incorrect", clone.content, equalTo(message.content))
        assertThat("timestamp incorrect", clone.timestamp, equalTo(message.timestamp))
    }

    @Test
    fun test_setOfVariables() {
        val ref = ChatsData.fakeMessage1_1.clone()
        val message = Message(ref.uid)

        message.sender = ref.sender
        message.receiver = ref.receiver
        message.content = ref.content
        message.timestamp = ref.timestamp
        message.valid = ref.valid

        checkUsersEqual("sender incorrect", message.sender, ref.sender)
        checkUsersEqual("receiver incorrect", message.receiver, ref.receiver)
        assertThat("content incorrect", message.content, equalTo(ref.content))
        assertThat("timestamp incorrect", message.timestamp, equalTo(ref.timestamp))
        assertThat("valid incorrect", message.valid, equalTo(ref.valid))
    }


    private fun checkUsersEqual(msg: String, user1: User, user2: User) {
        assertThat("$msg uid incorrect", user1.uid, equalTo(user2.uid))
        assertThat("$msg username incorrect", user1.username, equalTo(user2.username))
        assertThat("$msg cards incorrect", user1.cards, equalTo(user2.cards))
        assertThat("$msg location incorrect", user1.location, equalTo(user2.location))
        assertThat("$msg chats incorrect", user1.chats, equalTo(user2.chats))
        assertThat("$msg messages incorrect", user1.messages, equalTo(user2.messages))
        assertThat("$msg valid incorrect", user1.valid, equalTo(user2.valid))
    }
}
