package com.pedrobneto.giphyapp.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import br.com.arch.toolkit.delegate.viewProvider
import com.google.android.material.tabs.TabLayout
import com.pedrobneto.giphyapp.R
import com.pedrobneto.giphyapp.extensions.onPageChanged
import com.pedrobneto.giphyapp.view.adapter.ViewPagerAdapter
import com.pedrobneto.giphyapp.view.util.SearchTextWatcher

class GiphyActivity : AppCompatActivity(R.layout.activity_giphy) {

    private val toolbarTransitionDuration: Long
        get() = resources.getInteger(R.integer.app_toolbar_transition_duration).toLong()

    private val root by viewProvider<ViewGroup>(R.id.root)

    private val toolbarTitle by viewProvider<View>(R.id.toolbar_title)
    private val searchEditText by viewProvider<EditText>(R.id.search_edit_text)
    private val toolbarImageView by viewProvider<ImageView>(R.id.toolbar_icon)

    private val tabLayout by viewProvider<TabLayout>(R.id.tab_layout)
    private val viewPager by viewProvider<ViewPager>(R.id.view_pager)

    private var adapter: ViewPagerAdapter? = null

    private var isSearching = false
        set(value) {
            field = value

            val transition = AutoTransition()
            transition.removeTarget(toolbarImageView)
            transition.duration = toolbarTransitionDuration
            TransitionManager.beginDelayedTransition(root, transition)
            toolbarTitle.isVisible = !field
            searchEditText.isVisible = field

            toolbarImageView.setImageResource(
                if (field) R.drawable.ic_clear_search else R.drawable.ic_search
            )

            if (field) {
                searchEditText.requestFocus()
            } else if (!searchEditText.text.isNullOrEmpty()) {
                searchEditText.setText("")
                adapter?.search("")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViewPager()

        searchEditText.addTextChangedListener(SearchTextWatcher(adapter!!::search))
        toolbarImageView.setOnClickListener { isSearching = !isSearching }
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(resources, supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        viewPager.onPageChanged {
            val shouldShowSearch = adapter?.shouldShowSearch(it) ?: false

            val transition = AutoTransition()
            transition.duration = toolbarTransitionDuration
            TransitionManager.beginDelayedTransition(root, AutoTransition())
            toolbarTitle.isVisible = !shouldShowSearch || !isSearching
            toolbarImageView.isInvisible = !shouldShowSearch
            searchEditText.isVisible = shouldShowSearch && isSearching
        }
    }
}
