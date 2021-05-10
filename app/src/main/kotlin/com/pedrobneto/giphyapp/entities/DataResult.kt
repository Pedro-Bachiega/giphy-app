package com.pedrobneto.giphyapp.entities

class DataResult<T>(
    val data: T? = null,
    val isLoading: Boolean,
    val error: Throwable? = null
)
