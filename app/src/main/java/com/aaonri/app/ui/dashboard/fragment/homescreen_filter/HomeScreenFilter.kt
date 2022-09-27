package com.aaonri.app.ui.dashboard.fragment.homescreen_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.model.Event
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.home_filter.viewmodel.HomeFilterViewModel
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.GetAllImmigrationRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentHomeScreenFilterBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.ui.dashboard.fragment.homescreen_filter.adapter.HomeFilterAdapter
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFilter : Fragment() {
    var binding: FragmentHomeScreenFilterBinding? = null
    val homeFilterViewModel: HomeFilterViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    val eventViewModel: EventViewModel by viewModels()
    val advertiseViewModel: AdvertiseViewModel by viewModels()
    val immigrationViewModel: ImmigrationViewModel by viewModels()
    var selectedCategoryFilterData: ServicesResponseItem? = null

    var homeFilterAdapter: HomeFilterAdapter? = null
    var classifiedAdapter: AllClassifiedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenFilterBinding.inflate(layoutInflater, container, false)

        homeFilterAdapter = HomeFilterAdapter()

        classifiedAdapter = AllClassifiedAdapter {
            val action =
                HomeScreenFilterDirections.actionHomeScreenFilterToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }

        homeFilterAdapter?.itemClickListener = { view, item ->
            when (item) {
                is Event -> {
                    val action =
                        HomeScreenFilterDirections.actionHomeScreenFilterToEventDetailsScreenFragment(
                            item.id
                        )
                    findNavController().navigate(action)
                }
                is Discussion -> {
                    val action =
                        HomeScreenFilterDirections.actionHomeScreenFilterToImmigrationDetailsFragment(
                            false,
                            item.discussionId
                        )
                    findNavController().navigate(action)
                }
            }
        }

        binding?.apply {

            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    searchQuery()
                }
                false
            }

            searchResultRv.layoutManager = LinearLayoutManager(context)
            searchResultRv.adapter = homeFilterAdapter
            classifiedRv.layoutManager = GridLayoutManager(context, 2)
            classifiedRv.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))
            binding?.classifiedRv?.adapter = classifiedAdapter


            selectCategory.setOnClickListener {
                val action =
                    HomeScreenFilterDirections.actionHomeScreenFilterToFilterCategoryBottomSheet()
                findNavController().navigate(action)
            }

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            homeFilterViewModel.selectedCategoryFilter.observe(viewLifecycleOwner) {
                if (it != null) {
                    selectedCategoryFilterData = it
                    selectCategory.text = it.interestDesc
                    searchView.setText("")
                }
            }

            homeFilterViewModel.searchQuery.observe(viewLifecycleOwner) {
                if (it != null) {
                    searchView.setText(it)
                }
            }

            searchView.addTextChangedListener { editable ->
                if (editable.toString().isEmpty()) {
                    searchResultRv.visibility = View.GONE
                    classifiedRv.visibility = View.GONE
                    resultsNotFoundLL.visibility = View.GONE
                    cancelButton.visibility = View.GONE
                    searchViewIcon.visibility = View.VISIBLE
                } else {
                    cancelButton.visibility = View.VISIBLE
                    searchViewIcon.visibility = View.GONE
                }
            }

            cancelButton.setOnClickListener {
                searchView.setText("")
                homeFilterViewModel.searchQuery.postValue("")
            }

            searchViewIcon.setOnClickListener {
                searchQuery()
            }
        }

        homeFilterViewModel.getServices()

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.searchResultRv?.visibility = View.GONE
                        if (response.data?.userAdsList?.isNotEmpty() == true) {
                            classifiedAdapter?.setData(response.data.userAdsList)
                            binding?.resultsNotFoundLL?.visibility = View.GONE
                            binding?.classifiedRv?.visibility = View.VISIBLE
                        } else {
                            binding?.classifiedRv?.visibility = View.GONE
                            binding?.resultsNotFoundLL?.visibility = View.VISIBLE
                        }
                        eventViewModel.allEventData.postValue(null)
                        immigrationViewModel.allImmigrationDiscussionListData.postValue(null)
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        eventViewModel.allEventData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.classifiedRv?.visibility = View.GONE
                        if (response.data?.eventList?.isNotEmpty() == true) {
                            homeFilterAdapter?.setData(response.data.eventList)
                            binding?.resultsNotFoundLL?.visibility = View.GONE
                            binding?.searchResultRv?.visibility = View.VISIBLE
                        } else {
                            binding?.searchResultRv?.visibility = View.GONE
                            binding?.resultsNotFoundLL?.visibility = View.VISIBLE
                        }
                        classifiedViewModel.classifiedByUserData.postValue(null)
                        immigrationViewModel.allImmigrationDiscussionListData.postValue(null)
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        immigrationViewModel.allImmigrationDiscussionListData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.classifiedRv?.visibility = View.GONE
                        if (response.data?.discussionList?.isNotEmpty() == true) {
                            homeFilterAdapter?.setData(response.data.discussionList)
                            binding?.resultsNotFoundLL?.visibility = View.GONE
                            binding?.searchResultRv?.visibility = View.VISIBLE
                        } else {
                            binding?.searchResultRv?.visibility = View.GONE
                            binding?.resultsNotFoundLL?.visibility = View.VISIBLE
                        }
                        classifiedViewModel.classifiedByUserData.postValue(null)
                        eventViewModel.allEventData.postValue(null)
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }



        return binding?.root
    }

    private fun searchQuery() {
        if (selectedCategoryFilterData != null) {
            if (binding?.searchView?.text.toString().isNotEmpty()) {
                homeFilterViewModel.searchQuery.postValue(binding?.searchView?.text.toString())
                when (selectedCategoryFilterData?.id) {
                    2 -> {
                        /** Classified **/
                        classifiedViewModel.getClassifiedByUser(
                            GetClassifiedByUserRequest(
                                category = "",
                                email = "",
                                fetchCatSubCat = true,
                                keywords = binding?.searchView?.text.toString(),
                                location = "",
                                maxPrice = 0,
                                minPrice = 0,
                                myAdsOnly = false,
                                popularOnAoonri = null,
                                subCategory = "",
                                zipCode = ""
                            )
                        )
                    }
                    3 -> {
                        /** Immigration **/
                        immigrationViewModel.getAllImmigrationDiscussion(
                            GetAllImmigrationRequest(
                                categoryId = "",
                                createdById = "",
                                keywords = binding?.searchView?.text.toString()
                            )
                        )
                    }
                    8 -> {
                        /** Event **/
                        eventViewModel.getAllEvent(
                            AllEventRequest(
                                category = "",
                                city = "",
                                from = "",
                                isPaid = "",
                                keyword = binding?.searchView?.text.toString(),
                                maxEntryFee = 0,
                                minEntryFee = 0,
                                myEventsOnly = false,
                                userId = "",
                                zip = ""
                            )
                        )
                    }
                }
            } else {
                activity?.let { it1 ->
                    Snackbar.make(
                        it1.findViewById(android.R.id.content),
                        "Please enter keyword", Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            activity?.let { it1 ->
                Snackbar.make(
                    it1.findViewById(android.R.id.content),
                    "Please select category first", Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeFilterViewModel.service.postValue(null)
        homeFilterViewModel.selectedCategoryFilter.postValue(null)
        homeFilterViewModel.searchQuery.postValue(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}