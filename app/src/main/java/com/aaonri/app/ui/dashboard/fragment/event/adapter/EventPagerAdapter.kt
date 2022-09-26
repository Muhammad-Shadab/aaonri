package com.aaonri.app.ui.dashboard.fragment.event.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.dashboard.fragment.event.fragment.AllEventFragment
import com.aaonri.app.ui.dashboard.fragment.event.fragment.MyEventFragment
import com.aaonri.app.ui.dashboard.fragment.event.fragment.RecentEventFragment

class EventPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllEventFragment()
            1 -> MyEventFragment()
            2 ->  RecentEventFragment()
            else -> { throw Resources.NotFoundException("Position not found")}
        }
    }
}