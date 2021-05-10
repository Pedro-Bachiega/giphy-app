package com.pedrobneto.giphyapp.actions

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class ClickIgnoringConstraintAction() : ViewAction {
    override fun getConstraints(): Matcher<View> = ViewMatchers.isEnabled()

    override fun getDescription(): String = "click ignoring constraints"

    override fun perform(uiController: UiController?, view: View?) {
        view?.performClick()
    }
}