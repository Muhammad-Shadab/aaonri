package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs.RecruiterAllTalentsFragmet
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs.RecruiterConsultantProfileFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs.RecruiterMyPostedJobFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs.AllJobFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs.JobAlertsFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs.MyJobProfileFragment

class RecruiterPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    fun removeTabPage() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecruiterAllTalentsFragmet()
            1 -> RecruiterMyPostedJobFragment()
            else -> {
                RecruiterConsultantProfileFragment()
            }
        }
    }
}