package com.pedrobneto.giphyapp.extensions

import androidx.viewpager.widget.ViewPager

fun ViewPager.onPageChanged(func: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            //Do nothing
        }

        override fun onPageScrollStateChanged(state: Int) {
            //Do nothing
        }

        override fun onPageSelected(position: Int) = func.invoke(position)
    })
}
