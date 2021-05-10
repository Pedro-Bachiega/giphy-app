package com.pedrobneto.sdk.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pedrobneto.sdk.api.GiphyApi
import com.pedrobneto.sdk.entities.Response
import com.pedrobneto.sdk.modules.networkingModule
import com.pedrobneto.sdk.modules.storageModule
import com.pedrobneto.sdk.services.FileService
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.Single
import java.io.File
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class GiphyRepositoryTest {

    private val context = mockk<Context>()

    private val api = mockk<GiphyApi>()
    private val fileService = mockk<FileService>()
    private val giphyRepository = GiphyRepository(api, fileService)

    private val giphyData = Response.GiphyData(listOf())

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private fun mockkContextMethods() {
        every { context.getExternalFilesDir(any()) } returns File("/test")
    }

    @Before
    fun setUp() {
        mockkContextMethods()
        startKoin {
            androidContext(context)
            loadKoinModules(listOf(networkingModule, storageModule))
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
        stopKoin()
    }

    @Test
    fun whenFetchGifs_shouldReturnSingleGiphyData() {
        every { api.fetchTrendingGifs(any(), any(), any()) } returns Single.just(giphyData)

        giphyRepository.fetchGifs(context, "", 0)
            .test()
            .assertValues(giphyData)
            .dispose()
    }

    @Test
    fun whenFetchGifsWithEmptySearchText_shouldFetchTrendingGifs() {
        every { api.fetchTrendingGifs(any(), any()) } returns Single.just(giphyData)

        giphyRepository.fetchGifs(context, "", 0)
        verify(exactly = 1) { api.fetchTrendingGifs(any(), any()) }
        verify(exactly = 0) { api.searchGifs(any(), any(), any()) }
    }

    @Test
    fun whenFetchGifsWithSearchText_shouldSearchGifs() {
        every { api.searchGifs(any(), any(), any()) } returns Single.just(giphyData)

        giphyRepository.fetchGifs(context, "search", 0)
        verify(exactly = 0) { api.fetchTrendingGifs(any(), any()) }
        verify(exactly = 1) { api.searchGifs(any(), any(), any()) }
    }

    @Test
    fun whenSavingFavorite_shouldSaveJsonLocally() {
        every { fileService.saveFile(context, any(), any()) } just Runs

        giphyRepository.saveFavoriteGif(context, "id", "title", "url", true)
        verify { fileService.saveFile(context, "id", any()) }
    }

    @Test
    fun whenDeletingFavorite_shouldDeleteLocalJson() {
        every { fileService.deleteFile(context, any()) } just Runs

        giphyRepository.saveFavoriteGif(context, "id", "title", "url", false)
        verify { fileService.deleteFile(context, "id") }
    }

}