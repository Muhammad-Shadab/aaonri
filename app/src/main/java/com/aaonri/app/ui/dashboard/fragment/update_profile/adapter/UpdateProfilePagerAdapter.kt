package com.aaonri.app.ui.dashboard.fragment.update_profile.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aaonri.app.ui.authentication.register.AddressDetailsFragment
import com.aaonri.app.ui.authentication.register.BasicDetailsFragment
import com.aaonri.app.ui.authentication.register.LocationDetailsFragment
import com.aaonri.app.ui.authentication.register.ServicesCategoryFragment

class UpdateProfilePagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    fun removeTabPage() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BasicDetailsFragment()
            1 -> AddressDetailsFragment()
            2 -> LocationDetailsFragment()
            else -> {
                ServicesCategoryFragment()
            }
        }
    }
}