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
    var noOfSelection = 0

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



            eventViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilterClicked ->
                if (isFilterClicked) {
                    noOfSelection = 0
                }
                setFilterVisibility()
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
            deleteFilterIv1.setOnClickListener {
                eventViewModel.setSelectedEventCity(
                    ""
                )
                eventScreenBinding?.filterCv1?.visibility = View.GONE
                eventViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }
            deleteFilterIv2.setOnClickListener {
                eventViewModel.setZipCodeInFilterScreen(
                    ""
                )
                eventScreenBinding?.filterCv2?.visibility = View.GONE
                eventViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }
            deleteFilterIv3.setOnClickListener {

                if (eventViewModel.isAllSelected) {
                    eventViewModel.setIsAllSelected(false)
                } else if (eventViewModel.isFreeSelected) {
                    eventViewModel.setIsFreeSelected(false)
                } else if (eventViewModel.isPaidSelected) {
                    eventViewModel.setIsPaidSelected(false)
                }
                onNoOfSelectedFilterItem(--noOfSelection)
                eventScreenBinding?.filterCv3?.visibility = View.GONE


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

    fun setFilterVisibility() {
        noOfSelection = 0
        if (eventViewModel.zipCodeInFilterScreen.isNotEmpty() || eventViewModel.cityFilter.isNotEmpty() || eventViewModel.isFreeSelected || eventViewModel.isPaidSelected || eventViewModel.isAllSelected) {
            eventScreenBinding?.selectedFilters?.visibility = View.VISIBLE

            if (eventViewModel.cityFilter.isNotEmpty()) {
                eventScreenBinding?.filterCv1?.visibility = View.VISIBLE
                eventScreenBinding?.filterText1?.text =
                    "${eventViewModel.cityFilter}"
                noOfSelection++
            } else {
                eventScreenBinding?.filterCv1?.visibility = View.GONE
            }

            if (eventViewModel.zipCodeInFilterScreen.isNotEmpty()) {
                eventScreenBinding?.filterCv2?.visibility = View.VISIBLE
                eventScreenBinding?.filterText2?.text =
                    "ZipCode: ${eventViewModel.zipCodeInFilterScreen}"
                noOfSelection++

            } else {
                eventScreenBinding?.filterCv2?.visibility = View.GONE
            }
            if (eventViewModel.isAllSelected) {
                eventScreenBinding?.filterCv3?.visibility = View.VISIBLE
                eventScreenBinding?.filterText3?.text = "Fee: All"
                noOfSelection++
            } else if (eventViewModel.isFreeSelected) {
                eventScreenBinding?.filterCv3?.visibility = View.VISIBLE
                eventScreenBinding?.filterText3?.text = "Fee: Free"
                noOfSelection++
            } else if (eventViewModel.isPaidSelected) {
                eventScreenBinding?.filterCv3?.visibility = View.VISIBLE
                eventScreenBinding?.filterText3?.text = "Fee: Paid"
                noOfSelection++
            } else {
                eventScreenBinding?.filterCv3?.visibility = View.GONE
            }


            onNoOfSelectedFilterItem(noOfSelection)

        } else {
            eventScreenBinding?.selectedFilters?.visibility = View.GONE
        }
        if (eventViewModel.zipCodeInFilterScreen.isNotEmpty() && eventViewModel.cityFilter.isNotEmpty()) {
            eventScreenBinding?.selectedFilters?.visibility = View.VISIBLE
        }
    }

    fun onNoOfSelectedFilterItem(noOfSelection: Int) {
        if (noOfSelection >= 1) {
            eventScreenBinding?.numberOfSelectedFilterCv?.visibility = View.VISIBLE
            eventScreenBinding?.selectedFilters?.visibility = View.VISIBLE
            eventScreenBinding?.numberOfSelectedFilterTv?.setText(noOfSelection.toString())
        } else {
            eventScreenBinding?.selectedFilters?.visibility = View.GONE
            eventScreenBinding?.numberOfSelectedFilterCv?.visibility = View.GONE

        }
    }
}