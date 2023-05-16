package com.github.sdp.tarjetakuna.ui.chat

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.github.sdp.tarjetakuna.utils.RecyclerViewAssertions
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatListFragmentTest {

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

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

    // TODO : wait for database refactoring to test this
    // TODO : when not connected, it should show a not connected message
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

        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.isEmpty())
    }

    @Test
    fun test_user2_noChat() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser2
        viewModel.o_chats = ChatsData.fakeChats_empty

        changeToNavChats()

        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.isEmpty())

    }

    @Test
    fun test_user1_chat() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser1
        viewModel.o_chats = ChatsData.fakeChats1

        changeToNavChats()

        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItems())
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItemCount(ChatsData.fakeChats1.size))
    }

    @Test
    fun test_user2_chat() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser2
        viewModel.o_chats = ChatsData.fakeChats1

        changeToNavChats()
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItems())
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItemCount(ChatsData.fakeChats1.size))
    }

    @Test
    fun test_onChatClick() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser1
        viewModel.o_chats = ChatsData.fakeChats1

        changeToNavChats()
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItems())
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItemCount(ChatsData.fakeChats1.size))

        // perform click on first item and check if it opens the chat
        onView(withId(R.id.chats_recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ChatListAdapter.ViewHolder>(
                0,
                click()
            )
        )

        // check if chat is open
        onView(withId(R.id.chat_constraintLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_notifIconShown() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser1
        viewModel.o_chats = ChatsData.fakeChats2

        changeToNavChats()
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItems())
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItemCount(ChatsData.fakeChats2.size))

        onView(withId(R.id.chat_item_notif)).check(matches(isDisplayed()))

    }

    @Test
    fun test_notifIconHidden() {
        // Set the viewmodel's data
        viewModel.o_currentUser = ChatsData.fakeUser3
        viewModel.o_chats = ChatsData.fakeChats2

        changeToNavChats()
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItems())
        onView(withId(R.id.chats_recyclerView)).check(RecyclerViewAssertions.hasItemCount(ChatsData.fakeChats2.size))

        onView(withId(R.id.chat_item_notif)).check(matches(not(isDisplayed())))
    }

}
