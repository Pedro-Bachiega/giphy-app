package com.pedrobneto.giphyapp.view.adapter.viewHolder

import android.content.Context
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.pedrobneto.giphyapp.R

class LoadingViewHolder(context: Context) : RecyclerView.ViewHolder(
    View.inflate(context, R.layout.item_placeholder, null)
) {
    private val placeholderView by viewProvider<View>(R.id.placeholder_view)

    fun bind() {
        itemView.doOnPreDraw {
            placeholderView.updateLayoutParams {
                height =
                    it.width + it.resources.getDimensionPixelSize(R.dimen.app_favorite_button_total_space)
            }
        }
    }
}