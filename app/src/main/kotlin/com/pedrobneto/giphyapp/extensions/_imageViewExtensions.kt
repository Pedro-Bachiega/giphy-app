package com.pedrobneto.giphyapp.extensions

import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView

fun ImageView.startDrawableTransition(duration: Int, forward: Boolean) {
    (drawable as? TransitionDrawable)?.run {
        if (forward) startTransition(duration) else reverseTransition(duration)
    }
}
