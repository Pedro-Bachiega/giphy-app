package com.pedrobneto.giphyapp.view.adapter

import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.view.favorites.FavoritesFragment
import com.pedrobneto.giphyapp.view.list.ListFragment

private const val ITEM_COUNT = 2

class ViewPagerAdapter(private val resources: Resources, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val listFragment = ListFragment()
    private val favoritesFragment = FavoritesFragment()

    override fun getCount() = ITEM_COUNT

    override fun getItem(position: Int) = if (position == 0) listFragment else favoritesFragment

    override fun getPageTitle(position: Int) =
        if (position == 0) resources.getString(R.string.app_trending)
        else resources.getString(R.string.app_favorites)

    fun shouldShowSearch(currentPosition: Int) = currentPosition == 0

    fun search(searchText: String) = listFragment.search(searchText)
}
