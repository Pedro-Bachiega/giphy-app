package com.pedrobneto.sdk.repository

import android.content.Context
import com.pedrobneto.sdk.api.GiphyApi
import com.pedrobneto.sdk.entities.Response
import com.pedrobneto.sdk.services.FileService
import com.squareup.moshi.Moshi
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.get

private const val GIF_LIMIT = 30

class GiphyRepository(private val api: GiphyApi, private val fileService: FileService) :
    KoinComponent {

    private val updatedGifs = mutableListOf<Response.Gif>()

    fun fetchGifs(context: Context, searchText: String, page: Int): Single<Response.GiphyData> {
        val response = if (searchText.isEmpty()) {
            api.fetchTrendingGifs(GIF_LIMIT, page)
        } else {
            api.searchGifs(GIF_LIMIT, page, searchText)
        }

        val favoriteGifs = fileService.getLocalGifs(context)
        return response.map { giphyData ->
            giphyData.gifList.forEach { gif ->
                gif.isFavorite = favoriteGifs.find { it.id == gif.id } != null
            }
            return@map giphyData
        }
    }

    fun saveFavoriteGif(
        context: Context,
        id: String,
        title: String,
        url: String,
        isFavorite: Boolean
    ) {
        val gif = Response.Gif(id, title, url, isFavorite)
        updatedGifs.find { it.id == gif.id }?.let {
            it.isFavorite = gif.isFavorite
        } ?: updatedGifs.add(gif)

        if (isFavorite) {
            val json = get<Moshi>()
                .adapter(Response.Gif::class.java)
                .toJson(gif)
            fileService.saveFile(context, id, json)
        } else {
            fileService.deleteFile(context, id)
        }
    }

    fun fetchFavoriteGifs(context: Context) = Response.GiphyData(fileService.getLocalGifs(context))

    fun fetchUpdatedGifs(): List<Response.Gif> = updatedGifs

    fun clearUpdatedGifs() = updatedGifs.clear()
}