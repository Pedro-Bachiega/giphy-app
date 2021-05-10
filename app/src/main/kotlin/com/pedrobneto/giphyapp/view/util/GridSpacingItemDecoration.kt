package com.pedrobneto.giphyapp.view.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val columnCount: Int, private val spacing: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % columnCount

        if (position < columnCount) outRect.top = spacing
        outRect.left = spacing - column * spacing / columnCount
        outRect.right = (column + 1) * spacing / columnCount
        outRect.bottom = spacing
    }
}
