package com.pedrobneto.giphyapp.view.util

import android.animation.Animator

class AnimatorListener(
    private val onStart: () -> Unit = {},
    private val onEnd: () -> Unit = {},
    private val onCancel: () -> Unit = {},
    private val onRepeat: () -> Unit = {}
) : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {
        onStart()
    }

    override fun onAnimationEnd(animation: Animator?) {
        onEnd()
    }

    override fun onAnimationCancel(animation: Animator?) {
        onCancel()
    }

    override fun onAnimationRepeat(animation: Animator?) {
        onRepeat()
    }
}