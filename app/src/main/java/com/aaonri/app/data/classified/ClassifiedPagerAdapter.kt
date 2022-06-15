package com.aaonri.app.data.classified

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.fragment.FavoriteClassifiedFragment
import com.aaonri.app.ui.dashboard.fragment.classified.fragment.MyClassifiedFragment
import com.aaonri.app.ui.dashboard.fragment.classified.tabs.AllClassifiedFragment

class ClassifiedPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    fun removeTabPage() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllClassifiedFragment()
            1 -> MyClassifiedFragment()
            else -> {
                FavoriteClassifiedFragment()
            }
        }
    }
}