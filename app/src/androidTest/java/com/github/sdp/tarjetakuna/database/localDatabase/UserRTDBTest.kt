package com.github.sdp.tarjetakuna.database.localDatabase

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.github.sdp.tarjetakuna.mockdata.CommonFirebase
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.utils.FBEmulator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith

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
        userRTDB = UserRTDB(FirebaseDB())
        fbEmulator.fb.reference.setValue(null)
        fbEmulator.fb.reference.updateChildren(CommonFirebase.goodFirebase)
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
