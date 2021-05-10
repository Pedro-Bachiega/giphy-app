package com.pedrobneto.giphyapp.view.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val VISIBLE_ITEMS_THRESHOLD = 10

class RecyclerViewScrollListener(
    private val layoutManager: GridLayoutManager,
    private val onLoadMore: (page: Int) -> Unit
) : RecyclerView.OnScrollListener() {

    private var currentPage = 0
    private var visibleItemsThreshold = VISIBLE_ITEMS_THRESHOLD
    private var previousTotalItemCount = 0
    private var loading = true

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        if (totalItemCount < previousTotalItemCount) {
            currentPage = 0
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) loading = true
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleItemsThreshold > totalItemCount) {
            loading = true
            currentPage++
            onLoadMore(currentPage)
        }
    }

    fun resetListener() {
        loading = true
        currentPage = 0
        previousTotalItemCount = 0
    }
}
