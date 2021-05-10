package com.pedrobneto.giphyapp.view.list

import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.entities.GiphyData
import com.pedrobneto.giphyapp.extensions.ResponseLiveData
import com.pedrobneto.giphyapp.view.BaseListFragment

class ListFragment : BaseListFragment() {

    override val emptyListMessageRes: Int = R.string.app_empty_list

    override val liveData: ResponseLiveData<GiphyData>
        get() = viewModel.allGifsLiveData

    private var searchText: String = ""

    override fun onResume() {
        super.onResume()

        val updatedGifs = viewModel.fetchUpdatedGifs()
        if (updatedGifs.isNotEmpty()) {
            viewModel.clearUpdatedGifs()
            contentAdapter.updateGifs(updatedGifs)
        }
    }

    override fun fetchData(page: Int) = viewModel.fetchGifs(requireContext(), searchText, page)

    fun search(searchText: String) {
        this.searchText = searchText
        scrollListener?.resetListener()
        shouldResetList = true
        fetchData(0)
    }
}
