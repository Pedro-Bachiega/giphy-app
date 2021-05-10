package com.pedrobneto.giphyapp

import android.app.Application
import com.pedrobneto.giphyapp.modules.viewModelModule
import com.pedrobneto.sdk.modules.networkingModule
import com.pedrobneto.sdk.modules.repositoryModule
import com.pedrobneto.sdk.modules.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

open class GiphyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GiphyApp)
            modules(networkingModule, storageModule, repositoryModule, viewModelModule)
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}
