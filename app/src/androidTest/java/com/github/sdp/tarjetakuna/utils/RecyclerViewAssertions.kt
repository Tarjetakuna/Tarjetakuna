package com.github.sdp.tarjetakuna.utils

import android.view.View
import android.view.View.FIND_VIEWS_WITH_TEXT
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.EspressoException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.comparesEqualTo
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse


/**
 * taken from https://gist.github.com/txusballesteros/eed78bd057ddb9d5353bd96e76d8c799
 * Helper to test RecyclerViews
 */
class RecyclerViewAssertions {
    companion object {
        fun hasItems() = ViewAssertion { view, noViewFoundException ->
            if (view is RecyclerView) {
                assertThat(
                    "adapter item count is not more than 0 ",
                    view.adapter!!.itemCount,
                    greaterThan(0)
                )
            } else {
                throw noViewFoundException
            }
        }

        fun isEmpty() = ViewAssertion { view, noViewFoundException ->
            if (view is RecyclerView) {
                assertThat(
                    "adapter item count is not 0",
                    view.adapter!!.itemCount,
                    comparesEqualTo(0)
                )
            } else {
                throw noViewFoundException
            }
        }

        fun hasItemCount(expectedCount: Int) = ViewAssertion { view, noViewFoundException ->
            if (view is RecyclerView) {
                assertThat(
                    "adapter item count is not correct",
                    view.adapter!!.itemCount,
                    comparesEqualTo(expectedCount)
                )
            } else {
                throw noViewFoundException
            }
        }

        fun checkOnItemAtPosition(
            position: Int = 0,
            viewInteraction: ViewInteraction,
            viewAssertion: ViewAssertion
        ) = ViewAssertion { view, noViewFoundException ->
            if (view is RecyclerView) {
                view.findViewHolderForAdapterPosition(position)!!.itemView
                    .let {
                        viewInteraction.check(viewAssertion)
                    }
            } else {
                throw noViewFoundException
            }
        }

        fun hasViewWithTextAtPosition(
            position: Int = 0,
            expectedValue: CharSequence
        ) = ViewAssertion { view, noViewFoundException ->
            if (view is RecyclerView) {
                val outViews = arrayListOf<View>()
                view.findViewHolderForAdapterPosition(position)!!.itemView
                    .findViewsWithText(outViews, expectedValue, FIND_VIEWS_WITH_TEXT)
                assertFalse(outViews.isEmpty())
            } else {
                throw noViewFoundException
            }
        }

        inline fun hasViewWithIdAtPosition(
            position: Int = 0,
            viewId: Int,
            crossinline assert: (View) -> Unit
        ) = ViewAssertion { view, noViewFoundException ->
            if (view is RecyclerView) {
                view.findViewHolderForAdapterPosition(position)!!.itemView.findViewById<View>(viewId)
                    ?.let {
                        assert(it)
                    }
                    ?: throw RecyclerViewAssertionException("The view holder hasn't got a view with the specified ID, $viewId.")
            } else {
                throw noViewFoundException
            }
        }

        fun hasViewWithIdAndTextAtPosition(
            position: Int = 0,
            viewId: Int,
            expectedValue: CharSequence
        ) = ViewAssertion { view, noViewFoundException ->
            if (view is RecyclerView) {
                view.findViewHolderForAdapterPosition(position)!!.itemView.findViewById<TextView>(
                    viewId
                )
                    ?.let {
                        assertEquals(expectedValue, it.text)
                    }
                    ?: throw RecyclerViewAssertionException("The view holder hasn't got a view with the specified ID, $viewId.")
            } else {
                throw noViewFoundException
            }
        }
    }

}


class RecyclerViewAssertionException(message: String) : RuntimeException(message), EspressoException
