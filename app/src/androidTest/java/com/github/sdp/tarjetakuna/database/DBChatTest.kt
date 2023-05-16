package com.github.sdp.tarjetakuna.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for [DBChat]
 */
@RunWith(AndroidJUnit4::class)
class DBChatTest {

    @Test
    fun test() {
        val chat = DBChat("chatID", "user1", "user2")
        Assert.assertEquals("chatID", chat.chatID)
        Assert.assertEquals("user1", chat.user1)
        Assert.assertEquals("user2", chat.user2)
    }
}
