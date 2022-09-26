package com.aaonri.app.ui.dashboard.fragment.immigration.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.dashboard.fragment.immigration.tabs.AllImmigrationFragment
import com.aaonri.app.ui.dashboard.fragment.immigration.tabs.InformationCenterImmigrationFragment
import com.aaonri.app.ui.dashboard.fragment.immigration.tabs.MyDiscussionImmigrationFragment

class ImmigrationPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllImmigrationFragment()
            1 -> MyDiscussionImmigrationFragment()
            2 -> InformationCenterImmigrationFragment()
            else -> {
                throw Resources.NotFoundException("Position not found")
            }
        }
    }
}