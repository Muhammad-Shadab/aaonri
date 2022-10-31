package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs.RecruiterAllTalentsFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs.RecruiterConsultantProfileFragment
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs.RecruiterMyPostedJobFragment

class RecruiterPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    fun removeTabPage() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecruiterAllTalentsFragment()
            1 -> RecruiterMyPostedJobFragment()
            else -> {
                RecruiterConsultantProfileFragment()
            }
        }
    }
}