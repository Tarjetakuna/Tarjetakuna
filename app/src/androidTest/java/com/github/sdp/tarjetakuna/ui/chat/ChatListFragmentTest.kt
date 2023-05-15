package com.github.sdp.tarjetakuna.ui.chat

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.RecyclerViewAssertion
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatListFragmentTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>
    private val viewModel = ChatListViewModel

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

    private fun changeToNavChats() {
        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_chats, null)
        }

        onView(withId(R.id.chats_linearLayout)).check(matches(isDisplayed()))
    }

//    @Test
//    fun test_UserNotConnected_noChat() {
//        // Set the viewmodel's data
//        viewModel.o_currentUser = null
//        viewModel.o_chats = ChatsData.fakeChats_empty
//
//        changeToNavChats()
//    }

    @Test
    fun test_user1_noChat() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser1
        viewModel.o_chats = ChatsData.fakeChats_empty

        changeToNavChats()

        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertion.isEmpty())
    }

    @Test
    fun test_user2_noChat() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser2
        viewModel.o_chats = ChatsData.fakeChats_empty

        changeToNavChats()

        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertion.isEmpty())

    }

    @Test
    fun test_user1_chat() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser1
        viewModel.o_chats = ChatsData.fakeChats1

        changeToNavChats()

        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertion.hasItems())
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertion.hasItemCount(ChatsData.fakeChats1.size))
    }

    @Test
    fun test_user2_chat() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser2
        viewModel.o_chats = ChatsData.fakeChats1

        changeToNavChats()
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertion.hasItems())
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertion.hasItemCount(ChatsData.fakeChats1.size))
    }

}
