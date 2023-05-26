package com.github.sdp.tarjetakuna.database


import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.mockdata.CommonFirebase
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.Utils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class UserRTDBTest {
    
    private lateinit var userRTDB: UserRTDB

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()

        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        Mockito.`when`(mockedAuth.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mockedAuth)

//        val task = FirebaseDB().clearDatabase()
//        Tasks.await(task, 5, TimeUnit.SECONDS)

        FirebaseDB().returnDatabaseReference().updateChildren(CommonFirebase.goodFirebase)
        userRTDB = UserRTDB(FirebaseDB())
    }

    @After
    fun tearDown() {
//        val task = FirebaseDB().clearDatabase()
//        Tasks.await(task, 5, TimeUnit.SECONDS)
    }

    @Test
    fun addLocationWorks() {
        userRTDB.pushUserLocation("test", Coordinates(10.15, 20.25))
        userRTDB.getUserLocation("test").thenAccept {
            assert(it.latitude == 10.15)
            assert(it.longitude == 20.25)
        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()
    }


    @Test
    fun getListOfFullCardsInfoWorks() {
        val card1 = CommonMagicCard.aeronautTinkererCard.toDBMagicCard(CardPossession.OWNED)
            .copy(lastUpdate = 20)
        val card2 = CommonMagicCard.venomousHierophantCard.toDBMagicCard(CardPossession.OWNED)
            .copy(lastUpdate = 30)

        userRTDB.addCard(card1, SignIn.getSignIn().getUserUID()!!).get()
        userRTDB.addCard(card2, SignIn.getSignIn().getUserUID()!!).get()

        userRTDB.getListOfFullCardsInfos(SignIn.getSignIn().getUserUID()!!, CardPossession.OWNED)
            .thenAccept {
                assertThat("size should be 2", it.size == 2)
                Log.e("test", "IL Y A $it")
                assertThat("card1 should be in the list", it.contains(card1.copy(quantity = 1)))
                assertThat("card2 should be in the list", it.contains(card2.copy(quantity = 1)))
            }
            .exceptionally {
                assertThat("error '$it' should not have happened", false)
                null
            }.get()
    }

    @Test
    fun validGetUsers() {
        val users = userRTDB.getUsers().get()
        Assert.assertEquals(CommonFirebase.GoodFirebaseAttributes.username1, users[0].uid)
        Assert.assertEquals(CommonFirebase.GoodFirebaseAttributes.email1, users[0].username)
        Assert.assertEquals(
            CommonFirebase.GoodFirebaseAttributes.lat1,
            users[0].location.latitude,
            0.1
        )
        Assert.assertEquals(
            CommonFirebase.GoodFirebaseAttributes.long1,
            users[0].location.longitude,
            0.1
        )
        Assert.assertNotEquals(0, users[0].cards.size)
    }

    @Test
    fun validGetUser() {
        val user = userRTDB.getUserByUsername(CommonFirebase.GoodFirebaseAttributes.email1).get()
        Assert.assertNotEquals(null, user)
        if (user != null) {
            Assert.assertEquals(CommonFirebase.GoodFirebaseAttributes.username1, user.uid)
            Assert.assertEquals(CommonFirebase.GoodFirebaseAttributes.email1, user.username)
            Assert.assertEquals(
                CommonFirebase.GoodFirebaseAttributes.lat1,
                user.location.latitude,
                0.1
            )
            Assert.assertEquals(
                CommonFirebase.GoodFirebaseAttributes.long1,
                user.location.longitude,
                0.1
            )
            Log.d("UserRTDBTest", user.toString())
            Assert.assertNotEquals(0, user.cards.size)
        }
    }

    @Test
    fun test_addChat() {

        val chat = ChatsData.fakeChat1

        // add the chat on the db
        userRTDB.addChat(chat).get(1, TimeUnit.SECONDS)

        // check that the chat is on the users table
        val chats1 = userRTDB.getChatsFromUser(chat.user1.uid).get(1, TimeUnit.SECONDS)
        val chats2 = userRTDB.getChatsFromUser(chat.user2.uid).get(1, TimeUnit.SECONDS)
        assertThat("chat should be on user1's chats", chats1.map { it.uid }, contains(chat.uid))
        assertThat("chat should be on user2's chats", chats2.map { it.uid }, contains(chat.uid))

        // check that the chat is on the chats table
        val dbChat = ChatsRTDB().getChat(chat.uid).get(1, TimeUnit.SECONDS)
        assertThat("", DBChat.toDBChat(chat), equalTo(dbChat))

        // check that the messages are in the messages table
        val messages =
            MessagesRTDB().getMessages(chat.messages.map { it.uid }).get(1, TimeUnit.SECONDS)

        assertThat(
            "all messages are not in db",
            messages.map { it.uid },
            equalTo(chat.messages.map { it.uid })
        )
    }
}
