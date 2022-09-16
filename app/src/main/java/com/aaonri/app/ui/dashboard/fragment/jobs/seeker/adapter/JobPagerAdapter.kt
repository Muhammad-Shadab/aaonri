package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs.JobAlertsFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs.MyJobProfileFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.tabs.AllJobFragment

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