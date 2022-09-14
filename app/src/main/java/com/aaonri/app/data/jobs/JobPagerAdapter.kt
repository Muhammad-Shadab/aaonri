package com.aaonri.app.data.jobs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.tabs.AllClassifiedFragment
import com.aaonri.app.ui.dashboard.fragment.classified.tabs.FavoriteClassifiedFragment
import com.aaonri.app.ui.dashboard.fragment.classified.tabs.MyClassifiedFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.tabs.AllJobFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.tabs.JobAlertsFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.tabs.MyJobProfileFragment

class JobPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    fun removeTabPage() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllJobFragment()
            1 -> JobAlertsFragment()
            else -> {
                MyJobProfileFragment()
            }
        }
    }
}