package com.github.sdp.tarjetakuna.utils

import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object WithIndexSafeMatcher {
    fun withIndex(matcher: Matcher<View?>, index: Int) = object : TypeSafeMatcher<View?>() {

        var currentIndex = 0

        override fun describeTo(description: Description) {
            description.appendText("with index: ")
            description.appendValue(index)
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            return matcher.matches(view) && currentIndex++ == index
        }
    }
}
