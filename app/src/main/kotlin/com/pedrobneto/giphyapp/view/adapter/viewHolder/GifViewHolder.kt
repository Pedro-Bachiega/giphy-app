package com.pedrobneto.giphyapp.view.adapter.viewHolder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.bumptech.glide.Glide
import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.entities.Gif
import com.pedrobneto.giphyapp.extensions.onEndLoading
import com.pedrobneto.giphyapp.extensions.startDrawableTransition
import com.pedrobneto.giphyapp.extensions.swapVisibility
import com.pedrobneto.giphyapp.view.util.getTransitionDrawableFromVectors

class GifViewHolder(context: Context) : RecyclerView.ViewHolder(
    View.inflate(context, R.layout.item_gif, null)
) {
    private val titleLabel by viewProvider<TextView>(R.id.title_label)
    private val gifImageView by viewProvider<ImageView>(R.id.gif_image_view)
    private val buttonFavorite by viewProvider<ImageView>(R.id.favorite_button)
    private val loadingLayout by viewProvider<View>(R.id.loading_layout)

    private val drawableTransitionDuration: Int
        get() = itemView.resources.getInteger(R.integer.app_drawable_transition_duration)

    private val loadingTransitionDuration: Long
        get() = itemView.resources.getInteger(R.integer.app_loading_transition_duration).toLong()

    private fun getFavoriteDrawable() = getTransitionDrawableFromVectors(
        itemView.context,
        R.drawable.ic_favorite_off,
        R.drawable.ic_favorite_on
    )

    fun bind(gif: Gif, onFavoriteGif: (Context, Gif) -> Unit) {
        titleLabel.text = gif.title

        buttonFavorite.setImageDrawable(getFavoriteDrawable())
        if (gif.isFavorite) buttonFavorite.startDrawableTransition(0, true)

        buttonFavorite.setOnClickListener {
            gif.isFavorite = !gif.isFavorite
            buttonFavorite.startDrawableTransition(drawableTransitionDuration, gif.isFavorite)
            onFavoriteGif(it.context, gif)
        }

        loadingLayout.alpha = 1f
        loadingLayout.visibility = View.VISIBLE
        itemView.doOnPreDraw {
            val gifSize = itemView.measuredWidth
            gifImageView.updateLayoutParams { height = gifSize }

            Glide.with(itemView.context)
                .load(gif.url)
                .override(gifSize, gifSize)
                .error(ColorDrawable(Color.GREEN))
                .fallback(ColorDrawable(Color.GREEN))
                .onEndLoading {
                    swapVisibility(
                        loadingTransitionDuration,
                        gifImageView,
                        loadingLayout
                    )
                }
                .into(gifImageView)
        }
    }
}
