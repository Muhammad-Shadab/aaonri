package com.aaonri.app.data.classified

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.tabs.FavoriteClassifiedFragment
import com.aaonri.app.ui.dashboard.fragment.classified.tabs.MyClassifiedFragment
import com.aaonri.app.ui.dashboard.fragment.classified.tabs.AllClassifiedFragment

class ClassifiedPagerAdapter(fragment: Fragment, val isFilterEnabled: Boolean) :
    FragmentStateAdapter(fragment) {

    fun removeTabPage() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putBoolean("filterEnabled", isFilterEnabled)
        val allClassifiedFragment = AllClassifiedFragment()
        allClassifiedFragment.arguments = bundle
        val myClassifiedFragment = MyClassifiedFragment()
        myClassifiedFragment.arguments = bundle
        return when (position) {
            0 -> allClassifiedFragment
            1 -> myClassifiedFragment
            else -> {
                FavoriteClassifiedFragment()
            }
        }
    }
}