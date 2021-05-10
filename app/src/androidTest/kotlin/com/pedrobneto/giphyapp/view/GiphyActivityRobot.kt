package com.pedrobneto.giphyapp.view

import androidx.test.core.app.ActivityScenario
import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.extensions.click
import com.pedrobneto.giphyapp.extensions.isDisplayed
import com.pedrobneto.giphyapp.extensions.isNotDisplayed
import com.pedrobneto.giphyapp.extensions.selectTab
import com.pedrobneto.giphyapp.extensions.typeText
import com.pedrobneto.sdk.entities.Response
import com.pedrobneto.sdk.repository.GiphyRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.get
import org.koin.dsl.module

fun GiphyActivityTest.arrange(func: GiphyActivityRobot.() -> Unit) =
    GiphyActivityRobot().apply(func)

class GiphyActivityRobot : KoinComponent {
    private val giphyRepository = mockk<GiphyRepository>(relaxed = true)

    private fun mockApiCall() {
        loadKoinModules(module {
            single(override = true) { giphyRepository }
        })

        val response = Response.GiphyData(listOf())
        every { giphyRepository.fetchGifs(any(), any(), any()) } returns Single.just(response)
        every { giphyRepository.fetchFavoriteGifs(any()) } returns response
    }

    infix fun act(func: GiphyActivityAction.() -> Unit): GiphyActivityAction {
        mockApiCall()

        ActivityScenario.launch(GiphyActivity::class.java)
        return GiphyActivityAction().apply(func)
    }
}

class GiphyActivityAction {
    fun clickOnTrendingTab() {
        R.id.tab_layout.selectTab(0)
    }

    fun clickOnFavoritesTab() {
        R.id.tab_layout.selectTab(1)
    }

    fun clickOnSearchIcon() {
        R.id.toolbar_icon.click()
    }

    fun cancelSearch() {
        clickOnSearchIcon()
    }

    fun typeOnSearchBar() {
        R.id.search_edit_text.typeText("search")
    }

    infix fun assert(func: GiphyActivityResult.() -> Unit) = GiphyActivityResult().apply(func)
}

class GiphyActivityResult : KoinComponent {

    fun bothTabsAreDisplayed() {
        "Trending".isDisplayed()
        "Favorites".isDisplayed()
    }

    fun toolbarTitleIsDisplayed() {
        R.id.toolbar_title.isDisplayed()
    }

    fun toolbarTitleIsNotDisplayed() {
        R.id.toolbar_title.isNotDisplayed()
    }

    fun searchIconIsDisplayed() {
        R.id.toolbar_icon.isDisplayed()
    }

    fun searchIconIsNotDisplayed() {
        Thread.sleep(50L) //Inconsistent without delay :(
        R.id.toolbar_icon.isNotDisplayed()
    }

    fun searchEditTextIsDisplayed() {
        R.id.search_edit_text.isDisplayed()
    }

    fun searchEditTextIsNotDisplayed() {
        R.id.search_edit_text.isNotDisplayed()
    }

    fun searchApiIsCalledWithSearchParameter() {
        Thread.sleep(500L) //Runnable delay to prevent calling the api at every character change
        val giphyRepository = get<GiphyRepository>()
        verify { giphyRepository.fetchGifs(any(), "search", 0) }
    }

    fun searchApiIsCalledWithEmptySearchParameter() {
        Thread.sleep(500L) //Runnable delay to prevent calling the api at every character change
        val giphyRepository = get<GiphyRepository>()
        verify { giphyRepository.fetchGifs(any(), "", 0) }
    }
}
