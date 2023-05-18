package com.github.sdp.tarjetakuna.database.localDatabase

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.github.sdp.tarjetakuna.mockdata.CommonFirebase
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.google.android.gms.tasks.Tasks
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Tests for [UserRTDB]
 */
@RunWith(AndroidJUnit4::class)
class UserRTDBTest {

    private lateinit var userRTDB: UserRTDB

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    @Before
    fun setUp() {
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
        FirebaseDB().returnDatabaseReference().updateChildren(CommonFirebase.goodFirebase)
        userRTDB = UserRTDB(FirebaseDB())
    }

    @After
    fun tearDown() {
        val task = FirebaseDB().clearDatabase()
        Tasks.await(task, 5, TimeUnit.SECONDS)
    }

    @Test
    fun validGetUsers() {
        val users = userRTDB.getUsers().get()
        assertEquals(CommonFirebase.GoodFirebaseAttributes.username1, users[0].uid)
        assertEquals(CommonFirebase.GoodFirebaseAttributes.email1, users[0].username)
        assertEquals(CommonFirebase.GoodFirebaseAttributes.lat1, users[0].location.latitude, 0.1)
        assertEquals(CommonFirebase.GoodFirebaseAttributes.long1, users[0].location.longitude, 0.1)
        assertNotEquals(0, users[0].cards.size)
    }

    @Test
    fun validGetUser() {
        val user = userRTDB.getUserByUsername(CommonFirebase.GoodFirebaseAttributes.email1).get()
        assertNotEquals(null, user)
        if (user != null) {
            assertEquals(CommonFirebase.GoodFirebaseAttributes.username1, user.uid)
            assertEquals(CommonFirebase.GoodFirebaseAttributes.email1, user.username)
            assertEquals(CommonFirebase.GoodFirebaseAttributes.lat1, user.location.latitude, 0.1)
            assertEquals(CommonFirebase.GoodFirebaseAttributes.long1, user.location.longitude, 0.1)
            Log.d("UserRTDBTest", user.toString())
            assertNotEquals(0, user.cards.size)
        }
    }
}
