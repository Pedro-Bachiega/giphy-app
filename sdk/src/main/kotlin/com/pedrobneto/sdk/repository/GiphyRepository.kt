package com.pedrobneto.sdk.repository

import android.content.Context
import com.pedrobneto.sdk.api.GiphyApi
import com.pedrobneto.sdk.entities.Response
import com.pedrobneto.sdk.services.FileService
import com.squareup.moshi.Moshi
import io.reactivex.Single
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import org.koin.core.KoinComponent
import org.koin.core.get

private const val GIF_LIMIT = 30

class GiphyRepository(private val api: GiphyApi, private val fileService: FileService) :
    KoinComponent {

    private val updatedGifs = mutableListOf<Response.Gif>()

    private fun getGifsDirPath(context: Context): String {
        var path = context.getExternalFilesDir(null)?.absolutePath ?: ""
        path += "${File.separator}/Giphy App/favorite_gifs${File.separator}"

        return path
    }

    private fun getLocalGifs(context: Context): List<Response.Gif> {
        val gifsDir = File(getGifsDirPath(context))
        val gifList = mutableListOf<Response.Gif>()

        if (gifsDir.exists()) {
            gifsDir.listFiles()?.forEach { file ->
                val objectInputStream = ObjectInputStream(FileInputStream(file))
                val json = objectInputStream.readObject() as? String
                objectInputStream.close()

                json?.let {
                    get<Moshi>()
                        .adapter(Response.Gif::class.java)
                        .fromJson(it)
                        ?.run(gifList::add)
                }
            }
        }

        return gifList
    }

    fun fetchGifs(context: Context, searchText: String, page: Int): Single<Response.GiphyData> {
        val response = if (searchText.isEmpty()) {
            api.fetchTrendingGifs(GIF_LIMIT, page)
        } else {
            api.searchGifs(GIF_LIMIT, page, searchText)
        }

        val favoriteGifs = getLocalGifs(context)
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

    fun fetchFavoriteGifs(context: Context) = Response.GiphyData(getLocalGifs(context))

    fun fetchUpdatedGifs(): List<Response.Gif> = updatedGifs

    fun clearUpdatedGifs() = updatedGifs.clear()
}