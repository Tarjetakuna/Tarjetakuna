package com.github.sdp.tarjetakuna.database

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.mockdata.CommonFirebase
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.google.android.gms.tasks.Tasks
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class UserRTDBTest {

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    private lateinit var userRTDB: UserRTDB

    @Before
    fun setUp() {
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
        FirebaseDB().returnDatabaseReference().updateChildren(CommonFirebase.goodFirebase)
        userRTDB = UserRTDB(FirebaseDB())
    }

    @Before
    fun setUp() {
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        Mockito.`when`(mockedAuth.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mockedAuth)

        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
    }

    @After
    fun tearDown() {
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
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

    @After
    fun tearDown() {
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
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
}
