package com.github.sdp.tarjetakuna.utils

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.test.espresso.idling.CountingIdlingResource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * This class is used to load images with Glide and keep track of the loading process for testing purposes.
 */
object CustomGlide {

    private const val RESOURCE = "GLOBAL"
    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    /**
     * Loads an image from the given url and calls the callback when the image is loaded.
     */
    fun loadDrawable(fragmentContext: Fragment, url: String, callback: (Drawable) -> Unit) {
        countingIdlingResource.increment()
        Glide.with(fragmentContext)
            .load(url)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    if (!countingIdlingResource.isIdleNow) {
                        countingIdlingResource.decrement()
                    }

                    callback(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }
}
