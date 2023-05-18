package com.github.sdp.tarjetakuna.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.utils.FBEmulator
import org.hamcrest.MatcherAssert.assertThat
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserRTDBTest {

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    val userRTDB = UserRTDB(FirebaseDB())

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

}
