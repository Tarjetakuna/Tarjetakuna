package com.github.sdp.tarjetakuna.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.Utils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for [DBChat]
 */
@RunWith(AndroidJUnit4::class)
class DBChatTest {

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()
    }

    @Test
    fun test_toDBChat() {
        val chat: Chat = ChatsData.fakeChat1
        val dbChat: DBChat = DBChat.toDBChat(chat)

        assertThat("Chat id is not the same", chat.uid, equalTo(dbChat.uid))
        assertThat("User1 is not the same", chat.user1.uid, equalTo(dbChat.user1))
        assertThat("User2 is not the same", chat.user2.uid, equalTo(dbChat.user2))
        for (i in chat.messages.indices) {
            assertThat(
                "Message $i is not the same",
                chat.messages[i].uid,
                equalTo(dbChat.messages[i])
            )
        }
        assertThat(
            "User1LastRead is not the same",
            chat.user1LastRead,
            equalTo(dbChat.user1LastRead)
        )
        assertThat(
            "User2LastRead is not the same",
            chat.user2LastRead,
            equalTo(dbChat.user2LastRead)
        )
    }

    @Test
    fun test_toChat() {
        val dbChat: DBChat = ChatsData.fakeDBChat1
        val chat: Chat = DBChat.fromDBChat(dbChat)

        assertThat("Chat is valid", chat.valid, equalTo(false))

        assertThat("Chat id is not the same", chat.uid, equalTo(dbChat.uid))
        assertThat("User1 is not the same", chat.user1.uid, equalTo(dbChat.user1))
        assertThat("User2 is not the same", chat.user2.uid, equalTo(dbChat.user2))
        for (i in chat.messages.indices) {
            assertThat(
                "Message $i is not the same",
                chat.messages[i].uid,
                equalTo(dbChat.messages[i])
            )
        }
        assertThat(
            "User1LastRead is not the same",
            chat.user1LastRead,
            equalTo(dbChat.user1LastRead)
        )
        assertThat(
            "User2LastRead is not the same",
            chat.user2LastRead,
            equalTo(dbChat.user2LastRead)
        )
    }
}
