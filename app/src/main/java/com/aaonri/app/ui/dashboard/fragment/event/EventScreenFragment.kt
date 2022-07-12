package com.aaonri.app.ui.dashboard.fragment.event

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.MainActivity
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedPagerAdapter
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventScreenBinding
import com.aaonri.app.ui.dashboard.fragment.classified.ClassifiedScreenFragmentDirections
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventScreenFragment : Fragment() {
    var eventScreenBinding: FragmentEventScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    private val tabTitles =
        arrayListOf("All Events", "My Events", "Recent Events")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventScreenBinding = FragmentEventScreenBinding.inflate(inflater, container, false)
        val fragment = this

        val pagerAdapter = EventPagerAdapter(fragment)

        eventScreenBinding?.apply {

            eventsScreenViewPager.isUserInputEnabled = false

            /*filterEvent.setOnClickListener {
                findNavController().navigate(R.id.action_eventScreenFragment_to_classifiedFilterFragmentBottom)
            }*/

            floatingActionBtnEvents.setOnClickListener {
                val intent = Intent(requireContext(), EventScreenActivity::class.java)
                startActivity(intent)
            }

            eventsScreenViewPager.adapter = pagerAdapter

            TabLayoutMediator(eventsScreenTabLayout, eventsScreenViewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as TextView
                eventsScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }

            eventsScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 2) {
                        selectedFilters.visibility = View.GONE
                    } else {
                        selectedFilters.visibility = View.VISIBLE
                    }
                    /*if (tab?.position == 1) {
                        floatingActionBtnEvents.visibility = View.GONE
                    } else {
                        floatingActionBtnEvents.visibility = View.VISIBLE
                    }*/
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }
            })

            dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
                if (it) {
                    floatingActionBtnEvents.visibility = View.GONE
                    eventsScreenTabLayout.visibility = View.GONE
                    eventsScreenViewPager.setPadding(0, 40, 0, 0)
                    eventsScreenViewPager.isUserInputEnabled = false
                }
            }
        }

        /*eventViewModel.hideFloatingButtonInSecondTab.observe(viewLifecycleOwner) {
            if (it) {
                eventScreenBinding?.floatingActionBtnEvents?.visibility = View.GONE
            } else {
                eventScreenBinding?.floatingActionBtnEvents?.visibility = View.VISIBLE
            }
        }*/

        postEventViewModel.sendDataToClassifiedDetailsScreen.observe(viewLifecycleOwner) {
            if (postEventViewModel.navigateToClassifiedDetail) {
                val action =
                    EventScreenFragmentDirections.actionEventScreenFragmentToEventDetailsScreenFragment(
                        it
                    )
                findNavController().navigate(action)
                postEventViewModel.setNavigateToClassifiedDetailsScreen(false)
            }
        }

        return eventScreenBinding?.root
    }


}