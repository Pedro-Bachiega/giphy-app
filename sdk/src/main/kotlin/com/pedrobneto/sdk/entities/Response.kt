package com.pedrobneto.sdk.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class Response {
    @JsonClass(generateAdapter = true)
    class GiphyData(
        @Json(name = "data") val gifList: List<Gif>
    )

    @JsonClass(generateAdapter = true)
    class Gif(
        @Json(name = "id") val id: String,
        @Json(name = "title") val title: String,
        @Json(name = "images") val images: Images,
        var isFavorite: Boolean = false
    ) {
        constructor(id: String, title: String, url: String, isFavorite: Boolean) : this(
            id = id,
            title = title,
            images = Images(
                downsized = Image(
                    url = url
                )
            ),
            isFavorite = isFavorite
        )
    }

    @JsonClass(generateAdapter = true)
    class Images(
        @Json(name = "downsized") val downsized: Image
    )

    @JsonClass(generateAdapter = true)
    class Image(
        @Json(name = "url") val url: String
    )
}