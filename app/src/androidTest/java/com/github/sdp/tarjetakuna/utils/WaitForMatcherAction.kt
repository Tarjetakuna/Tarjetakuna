package com.github.sdp.tarjetakuna.utils

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException


fun waitForMatcher(viewId: Int, matcher: Matcher<View>, timeout: Long) {
    onView(withId(viewId)).perform(waitForMatcher(matcher, timeout))
}

/**
 * @return a [WaitForMatcherAction] instance created with the given [matcher] and [timeout] parameters.
 */
fun waitForMatcher(matcher: Matcher<View>, timeout: Long): ViewAction {
    return WaitForMatcherAction(matcher, timeout)
}

/**
 * A [ViewAction] that waits up to [timeout] milliseconds for a [View]'s to match with the given [matcher].
 *
 * @param text the text to wait for.
 * @param timeout the length of time in milliseconds to wait for.
 */
class WaitForMatcherAction(
    private val viewMatcher: Matcher<View>,
    private val timeout: Long
) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(TextView::class.java)
    }

    override fun getDescription(): String {
        return "wait up to $timeout milliseconds for the view to match $viewMatcher"
    }

    override fun perform(uiController: UiController, view: View) {
        val endTime = System.currentTimeMillis() + timeout

        do {
            uiController.loopMainThreadForAtLeast(50)
            if (viewMatcher.matches(view)) return
            else Log.d(
                "WaitForMatcherAction",
                "waiting on " + HumanReadables.describe(view) + " to match " + viewMatcher
            )
        } while (System.currentTimeMillis() < endTime)

        throw PerformException.Builder()
            .withActionDescription(description)
            .withCause(TimeoutException("Waited $timeout milliseconds"))
            .withViewDescription(HumanReadables.describe(view))
            .build()
    }
}
