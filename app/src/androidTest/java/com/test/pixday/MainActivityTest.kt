package com.test.pixday

import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import junit.framework.Assert.assertEquals
import org.junit.Test

class MainActivityTest {

    @Test
    fun listIsDisplayed() {
        ActivityScenario.launch<MainActivity>(
            Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        )

        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun searchPhotoIsDisplayed() {
        ActivityScenario.launch<MainActivity>(
            Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        )

        onView(withId(R.id.search_photo)).check(matches(isDisplayed()))
    }

    @Test
    fun retryButtonIsNotDisplayed() {
        ActivityScenario.launch<MainActivity>(
            Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        )

        onView(withId(R.id.retry_button)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val button = view as Button
            assertEquals(false, button.visibility == View.VISIBLE)
        }
    }

    @Test
    fun listIsScrollable() {
        ActivityScenario.launch<MainActivity>(
            Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        )

        onView(withId(R.id.list)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val currentItemCount = recyclerView.adapter?.itemCount ?: 0
            recyclerView.scrollToPosition(currentItemCount - 1)
        }
    }

    @Test
    fun showSomePhotos() {
        ActivityScenario.launch<MainActivity>(
            Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        )

        onView(withId(R.id.list)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val isNotEmpty = recyclerView.adapter?.itemCount ?: 0 > 0
            assertEquals(true, isNotEmpty)
        }
    }
}