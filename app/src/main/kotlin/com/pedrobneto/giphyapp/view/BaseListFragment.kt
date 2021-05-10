package com.pedrobneto.giphyapp.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.entities.DataResult
import com.pedrobneto.giphyapp.entities.GiphyData
import com.pedrobneto.giphyapp.extensions.ResponseLiveData
import com.pedrobneto.giphyapp.extensions.observeData
import com.pedrobneto.giphyapp.extensions.swapVisibility
import com.pedrobneto.giphyapp.view.adapter.GifListAdapter
import com.pedrobneto.giphyapp.view.adapter.GifPlaceholderAdapter
import com.pedrobneto.giphyapp.view.util.GridSpacingItemDecoration
import com.pedrobneto.giphyapp.view.util.RecyclerViewScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val COLUMN_COUNT = 2

abstract class BaseListFragment : Fragment(R.layout.fragment_list) {

    abstract val emptyListMessageRes: Int

    abstract val liveData: ResponseLiveData<GiphyData>

    private val loadingTransitionDuration: Long
        get() = resources.getInteger(R.integer.app_loading_transition_duration).toLong()

    private val messageLayout by viewProvider<View>(R.id.message_layout)
    private val messageImage by viewProvider<ImageView>(R.id.message_image)
    private val messageLabel by viewProvider<TextView>(R.id.message_label)

    private val loadingLayout by viewProvider<View>(R.id.loading_layout)
    private val loadingRecyclerView by viewProvider<RecyclerView>(R.id.loading_recycler_view)

    private val contentRecyclerView by viewProvider<RecyclerView>(R.id.content_recycler_view)
    protected val contentAdapter = GifListAdapter()
    protected var scrollListener: RecyclerViewScrollListener? = null

    protected val viewModel by viewModel<GiphyViewModel>()

    protected var shouldResetList = false

    abstract fun fetchData(page: Int)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liveData.observeData(viewLifecycleOwner, ::onReceiveData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingRecyclerView.adapter = GifPlaceholderAdapter()
        contentRecyclerView.adapter = contentAdapter.setOnFavoriteGif(viewModel::saveFavoriteGif)

        val contentLayoutManager = createGridLayoutManager()
        contentRecyclerView.layoutManager = contentLayoutManager
        loadingRecyclerView.layoutManager = createGridLayoutManager()

        scrollListener = RecyclerViewScrollListener(contentLayoutManager, ::fetchData)
        contentRecyclerView.addOnScrollListener(scrollListener!!)

        val spacing = resources.getDimensionPixelSize(R.dimen.app_gif_spacing)
        val decoration = GridSpacingItemDecoration(COLUMN_COUNT, spacing)
        contentRecyclerView.addItemDecoration(decoration)
        loadingRecyclerView.addItemDecoration(decoration)

        fetchData(0)
    }

    override fun onDetach() {
        viewModel.onDetach()
        super.onDetach()
    }

    private fun createGridLayoutManager() =
        GridLayoutManager(view?.context, COLUMN_COUNT, RecyclerView.VERTICAL, false)

    private fun onReceiveData(data: DataResult<GiphyData>) {
        onLoading(data.isLoading)
        data.data?.run(::onSuccess)
        data.error?.run { onError() }
    }

    private fun onLoading(isLoading: Boolean) {
        if (shouldResetList) contentAdapter.clear()
        else if (!shouldResetList && contentAdapter.itemCount > 0) return

        if (isLoading) display(loading = true)

        shouldResetList = false
    }

    protected open fun onSuccess(data: GiphyData) {
        if (data.gifList.isEmpty() && contentAdapter.itemCount == 0) {
            display(empty = true)
        } else {
            display(data = true)
            contentAdapter.addItems(data.gifList)
        }
    }

    private fun onError() = display(error = true)

    protected fun display(
        loading: Boolean = false,
        data: Boolean = false,
        error: Boolean = false,
        empty: Boolean = false
    ) {
        when {
            loading -> {
                swapVisibility(
                    loadingTransitionDuration,
                    loadingLayout,
                    contentRecyclerView,
                    messageLayout
                )
            }
            data -> {
                swapVisibility(
                    loadingTransitionDuration,
                    contentRecyclerView,
                    loadingLayout,
                    messageLayout
                )
            }
            error -> {
                messageImage.setImageResource(R.drawable.img_list_error)
                messageLabel.text = getString(R.string.app_list_error)

                swapVisibility(
                    loadingTransitionDuration,
                    messageLayout,
                    contentRecyclerView,
                    loadingLayout
                )
            }
            empty -> {
                messageImage.setImageResource(R.drawable.img_empty_list)
                messageLabel.text = getString(emptyListMessageRes)

                swapVisibility(
                    loadingTransitionDuration,
                    messageLayout,
                    loadingLayout,
                    contentRecyclerView
                )
            }
        }
    }
}
