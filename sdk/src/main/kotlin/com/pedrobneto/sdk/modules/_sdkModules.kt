package com.pedrobneto.sdk.modules

import com.pedrobneto.sdk.factory.ApiFactory
import com.pedrobneto.sdk.factory.MoshiFactory
import com.pedrobneto.sdk.factory.OkHttpClientFactory
import com.pedrobneto.sdk.repository.GiphyRepository
import com.pedrobneto.sdk.services.FileService
import org.koin.dsl.module

val networkingModule = module {
    factory { OkHttpClientFactory.create() }
    factory { MoshiFactory.create() }
    single { ApiFactory.create(get(), get()) }
}

val storageModule = module {
    factory { FileService() }
}

val repositoryModule = module {
    single { GiphyRepository(get(), get()) }
}
