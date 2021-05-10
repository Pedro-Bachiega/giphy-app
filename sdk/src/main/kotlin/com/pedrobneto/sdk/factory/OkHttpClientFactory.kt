package com.pedrobneto.sdk.factory

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpClientFactory {
    fun create() = OkHttpClient.Builder()
        .readTimeout(15L, TimeUnit.SECONDS)
        .connectTimeout(15L, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
}