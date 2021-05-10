package com.pedrobneto.giphyapp.view.favorites

import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.entities.GiphyData
import com.pedrobneto.giphyapp.extensions.ResponseLiveData
import com.pedrobneto.giphyapp.view.BaseListFragment

class FavoritesFragment : BaseListFragment() {

    override val emptyListMessageRes: Int = R.string.app_empty_favorites

    override val liveData: ResponseLiveData<GiphyData>
        get() = viewModel.favoriteGifsLiveData

    override fun onResume() {
        super.onResume()
        fetchData(0)
    }

    override fun fetchData(page: Int) = viewModel.fetchFavoriteGifs(requireContext())

    override fun onSuccess(data: GiphyData) {
        contentAdapter.clear()
        super.onSuccess(data)
    }
}
