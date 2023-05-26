package com.github.sdp.tarjetakuna.model

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.Utils
import junit.framework.TestCase.assertNotSame
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ChatTest {

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()
    }

    @Test
    fun test_FromUIDOnly() {
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
        assertThat("valid is not false", chat.valid, equalTo(false))
    }

    @Test
    fun test_clone() {
        val chat = Chat("chatUID")
        val clone = chat.clone()
        assertNotSame("clone is the same", clone, chat)

        assertThat("chat uid incorrect", clone.uid, equalTo(chat.uid))
        checkUsersEqual("user1 incorrect", clone.user1, chat.user1)
        checkUsersEqual("user2 incorrect", clone.user2, chat.user2)
        assertThat("messages incorrect", clone.messages, equalTo(chat.messages))
        assertThat("user1LastRead incorrect", clone.user1LastRead, equalTo(chat.user1LastRead))
        assertThat("user2LastRead incorrect", clone.user2LastRead, equalTo(chat.user2LastRead))
        assertThat("valid incorrect", clone.valid, equalTo(chat.valid))
    }

    @Test
    fun test_setOfVariables() {
        val ref = ChatsData.fakeChat1.clone()
        val chat = Chat(ref.uid)

        chat.user1 = ref.user1
        chat.user2 = ref.user2
        chat.messages = ref.messages
        chat.user1LastRead = ref.user1LastRead
        chat.user2LastRead = ref.user2LastRead
        chat.valid = ref.valid

        checkUsersEqual("user1 incorrect", chat.user1, ref.user1)
        checkUsersEqual("user2 incorrect", chat.user2, ref.user2)
        assertThat("messages incorrect", chat.messages, equalTo(ref.messages))
        assertThat("user1LastRead incorrect", chat.user1LastRead, equalTo(ref.user1LastRead))
        assertThat("user2LastRead incorrect", chat.user2LastRead, equalTo(ref.user2LastRead))
        assertThat("valid incorrect", chat.valid, equalTo(ref.valid))
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
