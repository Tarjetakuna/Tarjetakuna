package com.github.sdp.tarjetakuna.ui.chat

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.CurrentUserInterface
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.github.sdp.tarjetakuna.utils.RecyclerViewAssertions
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest {

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
        val mockUser = Mockito.mock(CurrentUserInterface::class.java)
        Mockito.`when`(mockUser.isUserLoggedIn()).thenReturn(false)
        Mockito.`when`(mockUser.getCurrentUser()).thenReturn(ChatsData.fakeUser1)
        viewModel.setCurrentUserInterface(mockUser)
        viewModel.o_chat.postValue(ChatsData.fakeChat1)

        changeToNavChat()

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
        val mockUser = Mockito.mock(CurrentUserInterface::class.java)
        Mockito.`when`(mockUser.isUserLoggedIn()).thenReturn(false)
        Mockito.`when`(mockUser.getCurrentUser()).thenReturn(ChatsData.fakeUser2)
        viewModel.setCurrentUserInterface(mockUser)
        viewModel.o_chat.postValue(ChatsData.fakeChat1)

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
        val mockUser = Mockito.mock(CurrentUserInterface::class.java)
        Mockito.`when`(mockUser.isUserLoggedIn()).thenReturn(false)
        Mockito.`when`(mockUser.getCurrentUser()).thenReturn(ChatsData.fakeUser1)
        viewModel.setCurrentUserInterface(mockUser)
        viewModel.o_chat.postValue(ChatsData.fakeChat2)

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
        val mockUser = Mockito.mock(CurrentUserInterface::class.java)
        Mockito.`when`(mockUser.isUserLoggedIn()).thenReturn(false)
        Mockito.`when`(mockUser.getCurrentUser()).thenReturn(ChatsData.fakeUser2)
        viewModel.setCurrentUserInterface(mockUser)
        viewModel.o_chat.postValue(ChatsData.fakeChat2)

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
        val mockUser = Mockito.mock(CurrentUserInterface::class.java)
        Mockito.`when`(mockUser.isUserLoggedIn()).thenReturn(false)
        Mockito.`when`(mockUser.getCurrentUser()).thenReturn(ChatsData.fakeUser1)
        viewModel.setCurrentUserInterface(mockUser)
        viewModel.o_chat.postValue(ChatsData.fakeChat1_emptyMsgs)

        changeToNavChat()

        onView(withId(R.id.chat_username_text)).check(matches(withText(ChatsData.fakeUser2.username)))
        onView(withId(R.id.chat_messages_recyclerView)).check(RecyclerViewAssertions.isEmpty())
    }

}
