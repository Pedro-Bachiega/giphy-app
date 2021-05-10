package com.pedrobneto.giphyapp.view.favorites

import androidx.fragment.app.testing.FragmentScenario
import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.extensions.click
import com.pedrobneto.giphyapp.extensions.isDisplayed
import com.pedrobneto.sdk.entities.Response
import com.pedrobneto.sdk.repository.GiphyRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.get
import org.koin.dsl.module

private const val gifUrl = "https://media.giphy.com/media/QACUDztJuq78aEmr5c/giphy.gif"

fun FavoritesFragmentTest.arrange(func: FavoritesFragmentRobot.() -> Unit) =
    FavoritesFragmentRobot().apply(func)

class FavoritesFragmentRobot : KoinComponent {
    private val giphyRepository = mockk<GiphyRepository>(relaxed = true)

    fun withEmptyList() {
        every { giphyRepository.fetchFavoriteGifs(any()) } returns Response.GiphyData(listOf())
    }

    fun withListNotEmpty() {
        every { giphyRepository.fetchFavoriteGifs(any()) } returns Response.GiphyData(
            listOf(
                Response.Gif(
                    id = "id",
                    title = "title",
                    url = gifUrl,
                    isFavorite = true
                )
            )
        )
    }

    private fun mockRepository() {
        loadKoinModules(module {
            single(override = true) { giphyRepository }
        })
    }

    infix fun act(func: FavoritesFragmentAction.() -> Unit): FavoritesFragmentAction {
        mockRepository()

        FragmentScenario.launchInContainer(FavoritesFragment::class.java)
        return FavoritesFragmentAction().apply(func)
    }
}

class FavoritesFragmentAction : KoinComponent {

    fun deleteLastFavorite() {
        val giphyRepository = get<GiphyRepository>()
        every { giphyRepository.fetchFavoriteGifs(any()) } returns Response.GiphyData(listOf())

        R.id.favorite_button.click()
    }

    infix fun assert(func: FavoritesFragmentResult.() -> Unit) =
        FavoritesFragmentResult().apply(func)
}

class FavoritesFragmentResult : KoinComponent {
    fun emptyListIsShown() {
        R.id.message_layout.isDisplayed()
        "No gifs could be found.\nStart by selecting a few favorites.".isDisplayed()
    }

    fun gifListIsShown() {
        "title".isDisplayed()
    }

    fun gifIsDeleted() {
        val giphyRepository = get<GiphyRepository>()
        verify { giphyRepository.saveFavoriteGif(any(), "id", "title", gifUrl, false) }
    }
}