package com.pedrobneto.giphyapp.view.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun whenLaunchWithEmptyList_shouldShowEmptyList() {
        arrange {
            withEmptyList()
        } act {
        } assert {
            emptyListIsShown()
        }
    }

    @Test
    fun whenLaunchWithNotEmptyList_shouldShowContent() {
        arrange {
            withListNotEmpty()
        } act {
        } assert {
            gifListIsShown()
        }
    }

    @Test
    fun whenLaunchWithOneGifAndDeleteLastFavorite_shouldShowEmptyList() {
        arrange {
            withListNotEmpty()
        } act {
            deleteLastFavorite()
        } assert {
            emptyListIsShown()
            gifIsDeleted()
        }
    }
}