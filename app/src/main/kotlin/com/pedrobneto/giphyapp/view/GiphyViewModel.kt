package com.pedrobneto.giphyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pedrobneto.giphyapp.entities.Gif
import com.pedrobneto.giphyapp.entities.GiphyData
import com.pedrobneto.giphyapp.extensions.MutableResponseLiveData
import com.pedrobneto.giphyapp.extensions.ResponseLiveData
import com.pedrobneto.giphyapp.extensions.call
import com.pedrobneto.giphyapp.extensions.postData
import com.pedrobneto.giphyapp.extensions.postLoading
import com.pedrobneto.sdk.repository.GiphyRepository
import io.reactivex.disposables.CompositeDisposable

class GiphyViewModel(private val giphyRepository: GiphyRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _allGifsLiveData = MutableResponseLiveData<GiphyData>()
    val allGifsLiveData: ResponseLiveData<GiphyData>
        get() = _allGifsLiveData

    private val _favoriteGifsLiveData = MutableResponseLiveData<GiphyData>()
    val favoriteGifsLiveData: ResponseLiveData<GiphyData>
        get() = _favoriteGifsLiveData

    fun fetchGifs(context: Context, searchText: String = "", page: Int = 0) {
        _allGifsLiveData.postLoading()
        compositeDisposable.add(
            giphyRepository.fetchGifs(context, searchText, page).call(::GiphyData, _allGifsLiveData)
        )
    }

    fun fetchFavoriteGifs(context: Context) {
        _favoriteGifsLiveData.postData(GiphyData(giphyRepository.fetchFavoriteGifs(context)))
    }

    fun saveFavoriteGif(context: Context, gif: Gif) {
        giphyRepository.saveFavoriteGif(context, gif.id, gif.title, gif.url, gif.isFavorite)
        fetchFavoriteGifs(context)
    }

    fun clearUpdatedGifs() = giphyRepository.clearUpdatedGifs()

    fun fetchUpdatedGifs() = giphyRepository.fetchUpdatedGifs().map(::Gif)

    fun onDetach() {
        compositeDisposable.dispose()
    }
}