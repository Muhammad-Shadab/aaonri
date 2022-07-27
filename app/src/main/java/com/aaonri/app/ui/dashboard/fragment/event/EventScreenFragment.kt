package com.aaonri.app.ui.dashboard.fragment.event

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventScreenBinding
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventPagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.bumptech.glide.Glide
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

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.PROFILE_USER, ""] }

        val pagerAdapter = EventPagerAdapter(fragment)

        eventScreenBinding?.apply {

            eventsScreenViewPager.isUserInputEnabled = false

            navigateBack.setOnClickListener {
                activity?.onBackPressed()
            }

            context?.let { Glide.with(it).load(profile).into(profilePicIv) }

            filterEvent.setOnClickListener {
                findNavController().navigate(R.id.eventFilterScreenFragment)
            }

            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    eventsScreenTabLayout.getTabAt(0)?.select()
                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(
                            EventConstants.SEARCH_KEYWORD_FILTER, textView.text.toString()
                        )
                    dashboardCommonViewModel.setIsFilterApplied("callEventApiWithFilter")
                }
                false
            }

            searchViewIcon.setOnClickListener {
                eventsScreenTabLayout.getTabAt(0)?.select()
                if (searchView.text.toString().isNotEmpty()) {
                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(
                            ClassifiedConstant.SEARCH_KEYWORD_FILTER, searchView.text.toString()
                        )
                    dashboardCommonViewModel.setIsFilterApplied("callEventApiWithFilter")
                }
            }

            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (keyword.toString().isEmpty()) {
                        dashboardCommonViewModel.setIsFilterApplied("false")
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            floatingActionBtnEvents.setOnClickListener {
                val intent = Intent(requireContext(), EventScreenActivity::class.java)
                startActivityForResult(intent, 2)
            }

            eventsScreenViewPager.adapter = pagerAdapter

            TabLayoutMediator(eventsScreenTabLayout, eventsScreenViewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                eventsScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }

            eventsScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 2) {
                        selectedFilters.visibility = View.INVISIBLE
                    } else {
                        selectedFilters.visibility = View.VISIBLE
                    }
                    if (tab?.position == 1) {
                        eventViewModel.hideFloatingButtonInSecondTab.observe(viewLifecycleOwner) {
                            if (it) {
                                floatingActionBtnEvents.visibility = View.GONE
                            }
                        }
                    } else {
                        floatingActionBtnEvents.visibility = View.VISIBLE
                    }
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

        postEventViewModel.sendDataToClassifiedDetailsScreen.observe(viewLifecycleOwner) {
            if (postEventViewModel.navigateToEventDetailScreen) {
                val action =
                    EventScreenFragmentDirections.actionEventScreenFragmentToEventDetailsScreenFragment(
                        it
                    )
                findNavController().navigate(action)
                postEventViewModel.setNavigateToEventDetailScreen(
                    value = false
                )
            }
        }

        return eventScreenBinding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        dashboardCommonViewModel.setIsFilterApplied("callEventApi")
        context?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(
                EventConstants.SEARCH_KEYWORD_FILTER,
                ""
            )
    }
}