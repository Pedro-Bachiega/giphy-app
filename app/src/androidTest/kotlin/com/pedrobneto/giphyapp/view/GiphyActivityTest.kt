package com.pedrobneto.giphyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GiphyActivityTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun whenLaunch_shouldDisplayTrendingAndFavoriteTabs() {
        arrange {
        } act {
        } assert {
            bothTabsAreDisplayed()
        }
    }

    @Test
    fun whenOnTrendingTab_searchIconShouldBeDisplayed() {
        arrange {
        } act {
        } assert {
            toolbarTitleIsDisplayed()
            searchIconIsDisplayed()
            searchEditTextIsNotDisplayed()
        }
    }

    @Test
    fun whenOnFavoritesTab_searchIconShouldNotBeDisplayed() {
        arrange {
        } act {
            clickOnFavoritesTab()
        } assert {
            toolbarTitleIsDisplayed()
            searchEditTextIsNotDisplayed()
            searchIconIsNotDisplayed()
        }
    }

    @Test
    fun whenGoToFavoritesAndBackToTrendingTab_searchIconShouldBeDisplayedAgain() {
        arrange {
        } act {
            clickOnFavoritesTab()
            clickOnTrendingTab()
        } assert {
            toolbarTitleIsDisplayed()
            searchEditTextIsNotDisplayed()
            searchIconIsDisplayed()
        }
    }

    @Test
    fun whenClickOnSearchIcon_toolbarTitleShouldBeHiddenAndSearchEditTextDisplayed() {
        arrange {
        } act {
            clickOnSearchIcon()
        } assert {
            toolbarTitleIsNotDisplayed()
            searchEditTextIsDisplayed()
        }
    }

    @Test
    fun whenClickOnSearchIconAndCancelSearch_toolbarShouldBeResetToTheOriginalState() {
        arrange {
        } act {
            clickOnSearchIcon()
            cancelSearch()
        } assert {
            toolbarTitleIsDisplayed()
            searchEditTextIsNotDisplayed()
            searchApiIsCalledWithEmptySearchParameter()
        }
    }

    @Test
    fun whenTypeSomethingOnSearchEditText_searchApiShouldBeCalled() {
        arrange {
        } act {
            clickOnSearchIcon()
            typeOnSearchBar()
        } assert {
            searchApiIsCalledWithSearchParameter()
        }
    }
}
