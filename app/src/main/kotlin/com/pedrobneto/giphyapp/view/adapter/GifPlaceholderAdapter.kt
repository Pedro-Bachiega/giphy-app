package com.pedrobneto.giphyapp.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedrobneto.giphyapp.view.adapter.viewHolder.LoadingViewHolder

private const val LOADING_ITEM_COUNT = 30

class GifPlaceholderAdapter : RecyclerView.Adapter<LoadingViewHolder>() {

    private val items = mutableListOf<Int>()

    init {
        for (index in 0 until LOADING_ITEM_COUNT) items.add(index)
        notifyItemRangeInserted(0, LOADING_ITEM_COUNT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LoadingViewHolder(parent.context)

    override fun onBindViewHolder(holder: LoadingViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = items.size
}