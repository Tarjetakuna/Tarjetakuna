package com.github.sdp.tarjetakuna.model

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ChatTest {

    @Test
    fun test_createChatFromUidOnly() {
        val timeBefore = Date()
        val chat = Chat("chatUID")
        val timeAfter = Date()
        assertThat("chat uid incorrect", chat.uid, equalTo("chatUID"))
        checkUsersEqual("user1 incorrect", chat.user1, User(""))
        checkUsersEqual("user2 incorrect", chat.user2, User(""))
        assertThat("messages not empty", chat.messages, equalTo(ArrayList()))
        assertThat("user1LastRead to big", chat.user1LastRead, lessThanOrEqualTo(timeAfter))
        assertThat("user1LastRead to small", chat.user1LastRead, greaterThanOrEqualTo(timeBefore))

        assertThat("user2LastRead to big", chat.user2LastRead, lessThanOrEqualTo(timeAfter))
        assertThat("user2LastRead to small", chat.user2LastRead, greaterThanOrEqualTo(timeBefore))
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
