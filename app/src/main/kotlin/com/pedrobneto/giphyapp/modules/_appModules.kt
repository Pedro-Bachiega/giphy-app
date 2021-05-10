package com.pedrobneto.giphyapp.modules

import com.pedrobneto.giphyapp.view.GiphyViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { GiphyViewModel(get()) }
}
