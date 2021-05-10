package com.pedrobneto.giphyapp.view.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun getTransitionDrawableFromVectors(
    context: Context,
    @DrawableRes firstVectorDrawable: Int,
    @DrawableRes secondVectorDrawable: Int
): TransitionDrawable {
    val transitionDrawable = TransitionDrawable(
        arrayOf(
            getBitmapDrawableFromVectorDrawable(context, firstVectorDrawable),
            getBitmapDrawableFromVectorDrawable(context, secondVectorDrawable)
        )
    )
    transitionDrawable.isCrossFadeEnabled = true
    return transitionDrawable
}

private fun getBitmapDrawableFromVectorDrawable(
    context: Context,
    @DrawableRes drawableId: Int
): BitmapDrawable {
    val drawable = ContextCompat.getDrawable(context, drawableId)
        ?: throw IllegalArgumentException("Drawable ID not found: $drawableId")

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return BitmapDrawable(context.resources, bitmap)
}
