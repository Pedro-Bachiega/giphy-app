package com.pedrobneto.sdk.api

import com.pedrobneto.sdk.BuildConfig
import com.pedrobneto.sdk.entities.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("trending")
    fun fetchTrendingGifs(
        @Query("limit") gifLimit: Int,
        @Query("offset") offset: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Single<Response.GiphyData>

    @GET("search")
    fun searchGifs(
        @Query("limit") gifLimit: Int,
        @Query("offset") offset: Int,
        @Query("q") searchText: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Single<Response.GiphyData>
}

