package com.github.bjolidon.bootcamp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GoogleMapsTest {

    @Test
    fun testActivityOpensProperly() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MapsActivity::class.java)
        val activity = ActivityScenario.launch<MapsActivity>(intent)
        Espresso.onView(ViewMatchers.withId(R.id.map))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        activity.close()
    }

    @Test
    fun testMarkerExists() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MapsActivity::class.java)
        val activity = ActivityScenario.launch<MapsActivity>(intent)
        val device: UiDevice = UiDevice.getInstance(getInstrumentation())
        val marker: UiObject = device.findObject(UiSelector().descriptionContains("Satellite"))
        marker.click()
        activity.close()
    }

    @Test
    fun testMarkerShouldNotExist() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MapsActivity::class.java)
        val activity = ActivityScenario.launch<MapsActivity>(intent)
        val device: UiDevice = UiDevice.getInstance(getInstrumentation())
        try {
            val marker: UiObject = device.findObject(UiSelector().descriptionContains("INVALID MARKER"))
            marker.click()
            assert(false)
        } catch (e: Exception) {
            assert(true)
        }
        activity.close()
    }
}