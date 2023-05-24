package com.github.sdp.tarjetakuna.database

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.mockdata.CommonFirebase
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.google.android.gms.tasks.Tasks
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
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
