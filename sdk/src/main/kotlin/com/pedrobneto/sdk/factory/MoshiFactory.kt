package com.pedrobneto.sdk.factory

import com.squareup.moshi.Moshi

object MoshiFactory {
    fun create() = Moshi.Builder().build()
}