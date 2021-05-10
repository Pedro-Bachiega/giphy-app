package com.pedrobneto.giphyapp.entities

import com.pedrobneto.sdk.entities.Response

class GiphyData(val gifList: List<Gif>) {
    constructor(response: Response.GiphyData) : this(gifList = response.gifList.map(::Gif))
}
