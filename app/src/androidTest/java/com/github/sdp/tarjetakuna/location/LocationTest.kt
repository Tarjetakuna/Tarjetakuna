package com.github.sdp.tarjetakuna.location

import android.Manifest
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.model.Coordinates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class LocationTest {

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        activityRule = ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun currentLocationChanged() {
        assertEquals(Location.getCurrentLocation(), Coordinates(0.0, 0.0))

        val locationManagerMock = mock(LocationManager::class.java)
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
                latitude = 1.0
                longitude = 2.0
            })
        }

        val contextMock = mock(Context::class.java)
        `when`(contextMock.getSystemService(Context.LOCATION_SERVICE)).thenReturn(
            locationManagerMock
        )

        activityRule.onActivity {
            Location.captureCurrentLocation(it)
            assertNotEquals(Coordinates(0.0, 0.0), Location.getCurrentLocation())
        }

//        verify(locationManagerMock).requestLocationUpdates(
//            eq(LocationManager.GPS_PROVIDER),
//            eq(0L),
//            eq(0f),
//            any(LocationListener::class.java)
//        )
    }

}
