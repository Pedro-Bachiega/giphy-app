package com.pedrobneto.giphyapp.view.list

import androidx.fragment.app.testing.FragmentScenario
import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.extensions.click
import com.pedrobneto.giphyapp.extensions.isDisplayed
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

private const val gifUrl = "https://media.giphy.com/media/QACUDztJuq78aEmr5c/giphy.gif"

fun ListFragmentTest.arrange(func: ListFragmentRobot.() -> Unit) = ListFragmentRobot().apply(func)

class ListFragmentRobot : KoinComponent {
    private val giphyRepository = mockk<GiphyRepository>(relaxed = true)

    private val response = Response.GiphyData(
        listOf(
            Response.Gif(
                id = "id",
                title = "title",
                url = gifUrl,
                isFavorite = false
            )
        )
    )

    fun withApiFailure() {
        every { giphyRepository.fetchGifs(any(), any(), any()) } returns Single.error(Throwable())
    }

    fun withApiSuccess() {
        every { giphyRepository.fetchGifs(any(), any(), any()) } returns Single.just(response)
        every { giphyRepository.fetchFavoriteGifs(any()) } returns response
    }

    fun withEmptyList() {
        every { giphyRepository.fetchGifs(any(), any(), any()) } returns Single.just(
            Response.GiphyData(listOf())
        )
        every { giphyRepository.fetchFavoriteGifs(any()) } returns response
    }

    private fun mockRepository() {
        loadKoinModules(module {
            single(override = true) { giphyRepository }
        })
    }

    infix fun act(func: ListFragmentAction.() -> Unit): ListFragmentAction {
        mockRepository()

        FragmentScenario.launchInContainer(ListFragment::class.java)
        return ListFragmentAction().apply(func)
    }
}

class ListFragmentAction {
    fun selectFavorite() {
        R.id.favorite_button.click()
    }

    fun deleteFavorite() {
        R.id.favorite_button.click()
    }

    infix fun assert(func: ListFragmentResult.() -> Unit) = ListFragmentResult().apply(func)
}

class ListFragmentResult : KoinComponent {
    private val giphyRepository = get<GiphyRepository>()

    fun gifListIsShown() {
        "title".isDisplayed()
    }

    fun emptyListIsShown() {
        R.id.message_layout.isDisplayed()
        "No gifs could be found.\nPlease try widening your search criteria.".isDisplayed()
    }

    fun errorIsShown() {
        R.id.message_layout.isDisplayed()
        "An error occurred fetching the latest gifs.".isDisplayed()
    }

    fun gifWasDeleted() {
        verify { giphyRepository.saveFavoriteGif(any(), "id", "title", gifUrl, false) }
    }

    fun favoriteWasSaved() {
        verify { giphyRepository.saveFavoriteGif(any(), "id", "title", gifUrl, true) }
    }
}
