package com.pedrobneto.giphyapp.view.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun whenLaunchWithApiFailure_shouldShowErrorLayout() {
        arrange {
            withApiFailure()
        } act {
        } assert {
            errorIsShown()
        }
    }

    @Test
    fun whenLaunchWithEmptyList_shouldShowEmptyListLayout() {
        arrange {
            withEmptyList()
        } act {
        } assert {
            emptyListIsShown()
        }
    }

    @Test
    fun whenLaunchWithSuccessfulApi_shouldShowContent() {
        arrange {
            withApiSuccess()
        } act {
        } assert {
            gifListIsShown()
        }
    }

    @Test
    fun whenClickFavoriteButton_shouldSaveGifLocally() {
        arrange {
            withApiSuccess()
        } act {
            selectFavorite()
        } assert {
            favoriteWasSaved()
        }
    }

    @Test
    fun whenDeleteFavoriteGif_shouldDeleteGifLocally() {
        arrange {
            withApiSuccess()
        } act {
            selectFavorite()
            deleteFavorite()
        } assert {
            gifWasDeleted()
        }
    }
}