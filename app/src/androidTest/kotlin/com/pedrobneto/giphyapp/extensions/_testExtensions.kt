package com.pedrobneto.giphyapp.extensions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import com.pedrobneto.giphyapp.actions.ClickIgnoringConstraintAction
import com.pedrobneto.giphyapp.actions.TabLayoutIndexClick
import org.hamcrest.CoreMatchers.not

private fun String.onText() = onView(ViewMatchers.withText(this))

private fun Int.onId() = onView(ViewMatchers.withId(this))

//region Matchers
fun String.isDisplayed(): ViewInteraction =
    this.onText().check(matches(ViewMatchers.isDisplayed()))

fun Int.isDisplayed(): ViewInteraction =
    this.onId().check(matches(ViewMatchers.isDisplayed()))

fun Int.isNotDisplayed(): ViewInteraction =
    this.onId().check(matches(not(ViewMatchers.isDisplayed())))
//endregion

//region Actions
fun Int.click(): ViewInteraction =
    this.onId().perform(ClickIgnoringConstraintAction())

fun Int.selectTab(tabIndex: Int): ViewInteraction =
    this.onId().perform(TabLayoutIndexClick(tabIndex))

fun Int.typeText(text: String): ViewInteraction =
    this.onId().perform(ViewActions.typeText(text))
//endregion
