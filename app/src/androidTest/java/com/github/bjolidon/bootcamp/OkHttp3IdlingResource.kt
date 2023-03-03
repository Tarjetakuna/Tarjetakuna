package com.github.bjolidon.bootcamp

import androidx.annotation.CheckResult
import androidx.test.espresso.IdlingResource
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

class OkHttp3IdlingResource private constructor(
    private val name: String,
    private val dispatcher: Dispatcher
) : IdlingResource {
    @Volatile
    var callback: IdlingResource.ResourceCallback? = null

    init {
        dispatcher.idleCallback = Runnable {
            val callback = callback
            callback?.onTransitionToIdle()
        }
    }

    override fun getName(): String {
        return name
    }

    override fun isIdleNow(): Boolean {
        return dispatcher.runningCallsCount() == 0
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }

    companion object {
        @CheckResult  // Extra guards as a library.
        fun create(name: String, client: OkHttpClient): OkHttp3IdlingResource {
            return OkHttp3IdlingResource(name, client.dispatcher)
        }
    }
}