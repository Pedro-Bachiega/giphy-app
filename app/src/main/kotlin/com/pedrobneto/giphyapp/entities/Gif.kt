package com.pedrobneto.giphyapp.entities

import com.pedrobneto.sdk.entities.Response

class Gif(
    val id: String,
    val title: String,
    val url: String,
    var isFavorite: Boolean
) {
    constructor(response: Response.Gif) : this(
        id = response.id,
        title = response.title,
        url = response.images.downsized.url,
        isFavorite = response.isFavorite
    )
}
