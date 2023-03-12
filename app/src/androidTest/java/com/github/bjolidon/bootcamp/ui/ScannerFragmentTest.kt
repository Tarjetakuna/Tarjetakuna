package com.github.bjolidon.bootcamp.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.bjolidon.bootcamp.ui.scanner.ScannerFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.github.bjolidon.bootcamp.R

@RunWith(AndroidJUnit4::class)
class ScannerFragmentTest {

    private lateinit var scenario: FragmentScenario<ScannerFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer()
    }

    @Test
    fun testButtonIsClickable() {
        onView(withId(R.id.button_scan)).check(matches(isClickable()))
    }
}