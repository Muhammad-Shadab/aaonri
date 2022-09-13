package com.aaonri.app.ui.dashboard.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentJobScreenBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.adapter.JobAdapter
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class JobScreenFragment : Fragment() {
    var jobBinding : FragmentJobScreenBinding? = null
    var jobAdapter: JobAdapter? = null
    private val tabTitles =
        arrayListOf("All Jobs", "Job Alerts", "My Profile")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        jobBinding = FragmentJobScreenBinding.inflate(inflater, container, false)
        jobBinding?.apply {
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
        }



        return jobBinding?.root
    }

}