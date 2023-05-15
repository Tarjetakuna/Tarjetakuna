package com.github.sdp.tarjetakuna.ui.chat

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.RecyclerViewAssertions
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>
    private val viewModel = ChatViewModel

    @Before
    fun setUp() {
        Intents.init()

        // launch main activity
        activityRule = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private fun changeToNavChat() {
        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_chat, null)
        }

        onView(withId(R.id.chat_constraintLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_user1_chat1() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser1
        viewModel.o_chat = ChatsData.fakeChat1

        changeToNavChat()

        Thread.sleep(1000)
        onView(withId(R.id.chat_username_text)).check(matches(withText(ChatsData.fakeUser2.username)))
        onView(withId(R.id.chat_messages_recyclerView)).check(
            RecyclerViewAssertions.hasItemCount(
                ChatsData.fakeChat1.messages.size
            )
        )
    }

    @Test
    fun test_user2_chat1() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser2
        viewModel.o_chat = ChatsData.fakeChat1

        changeToNavChat()

        onView(withId(R.id.chat_username_text)).check(matches(withText(ChatsData.fakeUser1.username)))
        onView(withId(R.id.chat_messages_recyclerView)).check(
            RecyclerViewAssertions.hasItemCount(
                ChatsData.fakeChat1.messages.size
            )
        )
    }

    @Test
    fun test_user1_chat2() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser1
        viewModel.o_chat = ChatsData.fakeChat2

        changeToNavChat()

        onView(withId(R.id.chat_username_text)).check(matches(withText(ChatsData.fakeUser2.username)))

        onView(withId(R.id.chat_messages_recyclerView)).check(
            RecyclerViewAssertions.hasItemCount(
                ChatsData.fakeChat2.messages.size
            )
        )
    }

    @Test
    fun test_user2_chat2() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser2
        viewModel.o_chat = ChatsData.fakeChat2

        changeToNavChat()

        onView(withId(R.id.chat_username_text)).check(matches(withText(ChatsData.fakeUser1.username)))

        onView(withId(R.id.chat_messages_recyclerView)).check(
            RecyclerViewAssertions.hasItemCount(
                ChatsData.fakeChat2.messages.size
            )
        )
    }

    @Test
    fun test_user1_emptyChat() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser1
        viewModel.o_chat = ChatsData.fakeChat1_emptyMsgs // user1 has no chats with user2

        changeToNavChat()

        Thread.sleep(1000)
        onView(withId(R.id.chat_username_text)).check(matches(withText(ChatsData.fakeUser2.username)))

        onView(withId(R.id.chat_messages_recyclerView)).check(RecyclerViewAssertions.isEmpty())
    }

}
