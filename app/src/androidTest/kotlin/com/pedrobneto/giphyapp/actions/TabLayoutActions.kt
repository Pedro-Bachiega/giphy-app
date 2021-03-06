package com.pedrobneto.giphyapp.actions

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

class TabLayoutIndexClick(private val tabIndex: Int) : ViewAction {

    override fun getDescription(): String = "with tab at index $tabIndex"

    override fun getConstraints(): Matcher<View> =
        allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

    override fun perform(uiController: UiController?, view: View?) {
        val tabLayout = view as TabLayout
        val tabAtIndex = tabLayout.getTabAt(tabIndex) ?: throw PerformException
            .Builder()
            .withCause(Throwable(" No tab at index $tabIndex"))
            .build()

        tabAtIndex.select()
    }

}
