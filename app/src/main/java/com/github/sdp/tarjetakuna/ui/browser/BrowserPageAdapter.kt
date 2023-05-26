package com.github.sdp.tarjetakuna.ui.browser

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Adapter for the view pager
 */
class CardCollectionPagerAdapter(val activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments = listOf(
        UserCollectionFragment(CardPossession.OWNED),
        UserCollectionFragment(CardPossession.WANTED)
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    /**
     * Setup the tab layout
     */
    fun setupTabLayout(tabLayout: TabLayout, viewPager: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> activity.getString(R.string.browser_page_adapter_collection_title)
                1 -> activity.getString(R.string.browser_page_adapter_wanted_title)
                else -> null
            }
        }.attach()
    }

}
