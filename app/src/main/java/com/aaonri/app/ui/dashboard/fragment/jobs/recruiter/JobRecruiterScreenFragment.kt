package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentJobRecruiterScreenBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.RecruiterPagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class JobRecruiterScreenFragment : Fragment() {
    var binding:FragmentJobRecruiterScreenBinding? = null
    private val tabTitles =
        arrayListOf("All Talents", "My Posted Jobs", "Consultant Profile")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobRecruiterScreenBinding.inflate(inflater, container, false)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val fragment = this
        val jobPagerAdapter = RecruiterPagerAdapter(fragment)

        binding?.apply {

            context?.let { Glide.with(it).load(profile).into(profilePicIv) }

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            jobScreenViewPager.adapter = jobPagerAdapter
            TabLayoutMediator(
                jobsScreenTabLayout,
                jobScreenViewPager
            ) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                jobsScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }
            jobsScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 2) {

                    } else {

                    }
                    if (tab?.position != 0) {

                    } else {

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }
            })
            jobScreenViewPager.isUserInputEnabled = false
        }





        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}