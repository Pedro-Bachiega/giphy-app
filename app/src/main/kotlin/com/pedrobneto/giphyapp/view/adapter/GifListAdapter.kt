package com.pedrobneto.giphyapp.view.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedrobneto.giphyapp.entities.Gif
import com.pedrobneto.giphyapp.view.adapter.viewHolder.GifViewHolder

class GifListAdapter : RecyclerView.Adapter<GifViewHolder>() {

    private var onFavoriteGif: (Context, Gif) -> Unit = { _, _ -> }

    private val items = mutableSetOf<Gif>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GifViewHolder(parent.context)

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) =
        holder.bind(items.elementAt(position), onFavoriteGif)

    override fun getItemCount(): Int = items.size

    fun addItems(newList: List<Gif>) {
        val lastSize = items.size
        items.addAll(newList)
        notifyItemRangeInserted(lastSize - 1, items.size - lastSize)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun setOnFavoriteGif(listener: (Context, Gif) -> Unit) = apply {
        onFavoriteGif = listener
    }

    fun updateGifs(gifs: List<Gif>) {
        items.forEachIndexed { index, currentGif ->
            val gif = gifs.find { it.id == currentGif.id } ?: return@forEachIndexed
            currentGif.isFavorite = gif.isFavorite
            notifyItemChanged(index)
        }
    }
}