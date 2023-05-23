package com.github.sdp.tarjetakuna.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.tarjetakuna.R
import com.google.android.material.tabs.TabLayout

/**
 * Fragment that displays the cards in a recycler view
 */
class BrowserFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: CardCollectionPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_browser, container, false)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        initPageAdapter()

        return view
    }

    override fun onResume() {
        super.onResume()
        initPageAdapter()
    }

    /**
     * Initialize the page adapter
     */
    private fun initPageAdapter() {
        pagerAdapter = CardCollectionPagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        pagerAdapter.setupTabLayout(tabLayout, viewPager)
    }
}
