package com.pedrobneto.giphyapp.extensions

import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.isVisible
import com.pedrobneto.giphyapp.view.util.AnimatorListener

fun swapVisibility(duration: Long, visibleView: View, vararg invisibleViews: View) {
    visibleView.alpha = 0f
    visibleView.visibility = View.VISIBLE

    val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    valueAnimator.addUpdateListener {
        visibleView.alpha = it.animatedValue as Float
        invisibleViews.forEach { view ->
            if (view.isVisible) view.alpha = 1f - (it.animatedValue as Float)
        }
    }
    valueAnimator.addListener(AnimatorListener(onEnd = {
        invisibleViews.forEach { it.visibility = View.GONE }
    }))
    valueAnimator.duration = duration
    valueAnimator.start()
}
