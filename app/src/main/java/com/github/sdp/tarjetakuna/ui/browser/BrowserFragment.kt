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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_browser, container, false)

        // 1. Create an instance of the ViewPager2
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)

        // 2. Create an instance of the CardCollectionPagerAdapter
        val pagerAdapter = CardCollectionPagerAdapter(requireActivity())

        // 3. Set the adapter on the ViewPager2
        viewPager.adapter = pagerAdapter

        // Set up the tab layout
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        pagerAdapter.setupTabLayout(tabLayout, viewPager)

        return view
    }
}
