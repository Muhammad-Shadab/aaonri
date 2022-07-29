package com.aaonri.app.ui.dashboard.fragment.event

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.EventStaticData
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.model.EventCategoryResponseItem
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventScreenBinding
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventPagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
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

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        if (EventStaticData.getEventCategory().isEmpty()) {
            postEventViewModel.getEventCategory()
        }

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
                    callEventApi(searchQuery = textView.text.toString())
                    eventViewModel.setSearchQuery(textView.text.toString())
                    eventViewModel.setClearAllFilter(true)
                    eventViewModel.setIsFilterEnable(true)
                }
                false
            }

            searchViewIcon.setOnClickListener {
                eventsScreenTabLayout.getTabAt(0)?.select()
                if (searchView.text.toString().isNotEmpty()) {
                    eventsScreenTabLayout.getTabAt(0)?.select()
                    callEventApi(searchView.text.toString())
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                } else {

                }
            }


            cancelbutton.setOnClickListener {
                eventViewModel.setClearAllFilter(true)
                eventViewModel.setClickOnClearAllFilterBtn(true)
                callEventApi()
                cancelbutton.visibility = View.GONE
                searchView.setText("")
                searchViewIcon.visibility = View.VISIBLE
            }

            eventViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilterClicked ->
                if (isFilterClicked) {
                    noOfSelection = 0
                }
                searchView.setText("")
                setFilterVisibility()
            }

            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (keyword.toString().isEmpty()) {
                        cancelbutton.visibility = View.GONE
                        searchViewIcon.visibility = View.VISIBLE
                        eventViewModel.setKeyClassifiedKeyboardListener(true)
                    } else {
                        cancelbutton.visibility = View.VISIBLE
                        searchViewIcon.visibility = View.GONE
                        eventViewModel.setKeyClassifiedKeyboardListener(false)
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
                    if (tab?.position != 0) {
                        filterEvent.isEnabled = false
                        filterEvent.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.graycolor
                            )
                        )
                        eventViewModel.setClearAllFilter(true)
                        eventViewModel.setClickOnClearAllFilterBtn(true)
                        if (searchView.text.isNotEmpty()) {
                            searchView.setText("")
                        }
                        SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                    } else {
                        filterEvent.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.white
                            )
                        )
                        filterEvent.isEnabled = true
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
                eventViewModel.setCityFilter(
                    ""
                )
                eventViewModel.setSelectedEventLocation("")
                eventScreenBinding?.filterCv1?.visibility = View.GONE
                eventViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }
            deleteFilterIv2.setOnClickListener {
                eventViewModel.setZipCodeInFilterScreen(
                    ""
                )
                eventViewModel.setIsMyLocationChecked(false)
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
                eventViewModel.setClickedOnFilter(true)
                filterCv3.visibility = View.GONE
                onNoOfSelectedFilterItem(--noOfSelection)
            }
            deleteFilterIv4.setOnClickListener {
                postEventViewModel.setSelectedEventCategory(
                    EventCategoryResponseItem(
                        false,
                        0,
                        0,
                        ""
                    )
                )
                eventViewModel.setCategoryFilter("")
                filterCv4.visibility = View.GONE
                eventViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }
        }

        eventViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilterClicked ->
            if (isFilterClicked) {

                eventViewModel.getAllEvent(
                    AllEventRequest(
                        category = eventViewModel.categoryFilter.ifEmpty { "" },
                        city = eventViewModel.cityFilter.ifEmpty { "" },
                        from = "",
                        isPaid = "",
                        keyword = eventViewModel.searchQueryFilter.ifEmpty { "" },
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = false,
                        userId = "",
                        zip = eventViewModel.zipCodeInFilterScreen.ifEmpty { "" }
                    )
                )

                eventViewModel.setClickedOnFilter(false)
                noOfSelection = 0
            }
            setFilterVisibility()

        }

        eventViewModel.clearAllFilter.observe(viewLifecycleOwner) { isClearAll ->
            if (isClearAll) {
                noOfSelection = 0
                onNoOfSelectedFilterItem(noOfSelection)
            }
        }

        postEventViewModel.eventCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    /*categoryBinding?.progressBar?.visibility = View.VISIBLE*/
                }
                is Resource.Success -> {
                    response.data?.let { EventStaticData.updateEventCategory(it) }
                }
                is Resource.Error -> {

                }
                else -> {}
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

        eventViewModel.clearAllFilter.observe(viewLifecycleOwner) { clearAllFilter ->
            if (clearAllFilter) {

                eventViewModel.setCategoryFilter("")
                eventViewModel.setSelectedEventLocation("")
                eventViewModel.setIsMyLocationChecked(false)
                eventViewModel.setZipCodeInFilterScreen("")
                eventViewModel.setSearchQuery("")

                eventViewModel.setIsAllSelected(false)
                eventViewModel.setIsFreeSelected(false)
                eventViewModel.setIsPaidSelected(false)

                postEventViewModel.setSelectedEventCategory(
                    EventCategoryResponseItem(
                        false,
                        0,
                        0,
                        ""
                    )
                )

                eventViewModel.setEventCityList(mutableListOf())
            }
            eventViewModel.setClearAllFilter(false)
        }

        return eventScreenBinding?.root
    }

    private fun callEventApi(searchQuery: String = "") {
        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        eventViewModel.getAllEvent(
            AllEventRequest(
                category = "",
                city = "",
                from = "",
                isPaid = "",
                keyword = searchQuery.ifEmpty { "" },
                maxEntryFee = 0,
                minEntryFee = 0,
                myEventsOnly = false,
                userId = if (email?.isNotEmpty() == true) email else "",
                zip = ""
            )
        )
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

    private fun setFilterVisibility() {
        noOfSelection = 0
        if (eventViewModel.zipCodeInFilterScreen.isNotEmpty() || eventViewModel.cityFilter.isNotEmpty() || eventViewModel.isFreeSelected || eventViewModel.isPaidSelected || eventViewModel.isAllSelected || eventViewModel.categoryFilter.isNotEmpty()) {
            eventScreenBinding?.selectedFilters?.visibility = View.VISIBLE

            if (eventViewModel.categoryFilter.isNotEmpty()) {
                eventScreenBinding?.filterCv4?.visibility = View.VISIBLE
                eventScreenBinding?.filterText4?.text =
                    "Category: ${eventViewModel.categoryFilter}"
                noOfSelection++
            } else {
                eventScreenBinding?.filterCv4?.visibility = View.GONE
            }

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