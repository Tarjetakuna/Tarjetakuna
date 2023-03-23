package com.github.sdp.tarjetakuna.utils

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Custom TypeSafeMatcher to use inside Espresso matchers
 */
object CustomTypeSafeMatcher {

    /**
     * Matcher to check if an ImageView has the same drawable as a bitmap
     */
    fun withDrawable(bitmap: Bitmap) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with drawable same as a bitmap")
        }

        override fun matchesSafely(view: View): Boolean {
            return view is ImageView && view.drawable.toBitmap().sameAs(bitmap)
        }
    }
}