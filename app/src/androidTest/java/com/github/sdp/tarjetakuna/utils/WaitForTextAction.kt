package com.github.sdp.tarjetakuna.utils

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.HumanReadables
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException

/**
 * Get Text from id in test
 */
fun getStringInTest(id: Int): String {
    return InstrumentationRegistry.getInstrumentation().targetContext.getString(id)
}

fun getStringInTest(id: Int, vararg formatArgs: Any): String {
    return InstrumentationRegistry.getInstrumentation().targetContext.getString(id, *formatArgs)
}

/**
 * Waits up to [timeout] milliseconds for a [viewId]'s text to change to [text].
 */
fun waitForText(viewId: Int, textId: Int, timeout: Long) {
    onView(withId(viewId)).perform(waitForText(textId, timeout))
}

fun waitForText(viewId: Int, text: String, timeout: Long) {
    onView(withId(viewId)).perform(waitForText(text, timeout))
}

/**
 * @return a [WaitForTextAction] instance created with the given [text] and [timeout] parameters.
 */
fun waitForText(text: String, timeout: Long): ViewAction {
    return WaitForTextAction(text, timeout)
}

/**
 * @return a [WaitForTextAction] instance created with the given [textId] and [timeout] parameters.
 */
fun waitForText(textId: Int, timeout: Long): ViewAction {
    val text = getStringInTest(textId)
    return waitForText(text, timeout)
}

/**
 * @return a [WaitForTextAction] instance created with the given [textId] and [timeout] parameters.
 */
fun waitForTextDiff(text: String, timeout: Long): ViewAction {
    return WaitForTextAction(text, timeout, false)
}

/**
 * @return a [WaitForTextAction] instance created with the given [textId] and [timeout] parameters.
 */
fun waitForTextDiff(textId: Int, timeout: Long): ViewAction {
    val text = InstrumentationRegistry.getInstrumentation().targetContext.getString(textId)
    return waitForTextDiff(text, timeout)
}

/**
 * A [ViewAction] that waits up to [timeout] milliseconds for a [View]'s text to change to [text].
 *
 * @param text the text to wait for.
 * @param timeout the length of time in milliseconds to wait for.
 */
class WaitForTextAction(
    private val text: String,
    private val timeout: Long,
    private val equality: Boolean = true
) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(TextView::class.java)
    }

    override fun getDescription(): String {
        return "wait up to $timeout milliseconds for the view to ${if (equality) "" else "not"} have text $text"
    }

    override fun perform(uiController: UiController, view: View) {
        val endTime = System.currentTimeMillis() + timeout

        do {
            uiController.loopMainThreadForAtLeast(50)
            if (equality) {
                if ((view as? TextView)?.text?.toString()?.trim() == text.trim()) return
                else Log.d(
                    "WaitForTextAction",
                    "waiting - current text: -${(view as? TextView)?.text}- expected text: -$text- " +
                            "equals: ${
                                (view as? TextView)?.text?.toString()?.trim() == text.trim()
                            }"
                )
            } else {
                if ((view as? TextView)?.text?.toString()?.trim() != text.trim()) return
                else Log.d(
                    "WaitForTextAction",
                    "waiting - current text: -${(view as? TextView)?.text}- expected text: -$text- " +
                            "not equals: ${
                                (view as? TextView)?.text?.toString()?.trim() != text.trim()
                            }"
                )
            }
        } while (System.currentTimeMillis() < endTime)

        throw PerformException.Builder()
            .withActionDescription(description)
            .withCause(TimeoutException("Waited $timeout milliseconds"))
            .withViewDescription(HumanReadables.describe(view))
            .build()
    }
}
