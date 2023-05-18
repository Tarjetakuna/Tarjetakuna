package com.github.sdp.tarjetakuna.extra

import android.Manifest
import android.location.LocationListener
import android.location.LocationManager
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.utils.FBEmulator
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class LocationTest {

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    private lateinit var activityRule: ActivityScenario<MainActivity>
    private lateinit var locationManagerMock: LocationManager

    @Before
    fun setUp() {
        locationManagerMock = mock(LocationManager::class.java)
        setLocationTo(1.0, 2.0)
        Location.setLastPushedToFirebase(0L)

        val mck = mock(Authenticator::class.java)
        `when`(mck.isUserLoggedIn()).thenReturn(true)
        `when`(mck.getUserUID()).thenReturn("test")
        SignIn.setSignIn(mck)

        activityRule = ActivityScenario.launch(MainActivity::class.java)
    }

    private fun setLocationTo(lat: Double, long: Double) {
        `when`(
            locationManagerMock.requestLocationUpdates(
                anyString(),
                anyLong(),
                anyFloat(),
                any(LocationListener::class.java)
            )
        ).thenAnswer {
            val listener = it.arguments[3] as LocationListener
            listener.onLocationChanged(android.location.Location("gps").apply {
                latitude = lat
                longitude = long
            })
        }
    }

    @Test
    fun currentLocationChanged() {
        activityRule.onActivity {
            Location.setLocationManager(locationManagerMock)
            Location.captureCurrentLocation(it)

            verify(locationManagerMock).requestLocationUpdates(
                eq(LocationManager.GPS_PROVIDER),
                eq(0L),
                eq(0f),
                any(LocationListener::class.java)
            )
            assertEquals(Coordinates(1.0, 2.0), Location.getCurrentLocation())
        }
    }

    @Test
    fun cannotAddLocationToFirebaseBefore5MinsWorks() {
        setLocationTo(25.0, 35.0)
        // set location to 1.0, 2.0
        activityRule.onActivity {
            Location.setLocationManager(locationManagerMock)
            Location.setLastPushedToFirebase(0L) // so that it always pushes to firebase
            Location.captureCurrentLocation(it)
            assertEquals(Coordinates(25.0, 35.0), Location.getCurrentLocation())
        }

        setLocationTo(10.0, 20.0)

        activityRule.onActivity {
            Location.setLastPushedToFirebase(System.currentTimeMillis()) // so that it does not push to firebase
            Location.captureCurrentLocation(it)
        }
        val userRTDB = UserRTDB(FirebaseDB())
        userRTDB.getUserLocation("test").thenAccept {
            assertEquals(25.0, it.latitude, 0.1)
            assertEquals(35.0, it.longitude, 0.1)
        }.exceptionally {
            assertThat("error '$it' should not have happened", false)
            null
        }.get()
    }

    @Test
    fun userIsNotLoggedDoesNotPushToFirebase() {
        `when`(SignIn.getSignIn().isUserLoggedIn()).thenReturn(false)
        `when`(SignIn.getSignIn().getUserUID()).thenReturn("fakeUser")
        setLocationTo(0.0, 5.0)
        activityRule.onActivity {
            Location.setLocationManager(locationManagerMock)
            Location.setLastPushedToFirebase(0L) // so that it always pushes to firebase
            Location.captureCurrentLocation(it)
        }

        val userRTDB = UserRTDB(FirebaseDB())
        userRTDB.getUserLocation("fakeUser").thenAccept {
            assertThat("should not have pushed to firebase", false)
        }.exceptionally {
            assertThat("No card in the database", true)
            null
        }.get()
    }


}
