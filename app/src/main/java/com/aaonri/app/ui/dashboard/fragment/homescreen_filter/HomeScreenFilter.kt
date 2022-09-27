package com.aaonri.app.ui.dashboard.fragment.homescreen_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
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
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.home_filter.viewmodel.HomeFilterViewModel
import com.aaonri.app.data.immigration.model.GetAllImmigrationRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentHomeScreenFilterBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
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

    //var homeFilterAdapter: HomeFilterAdapter? = null
    var classifiedAdapter: AllClassifiedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenFilterBinding.inflate(layoutInflater, container, false)

        //homeFilterAdapter = HomeFilterAdapter()

        classifiedAdapter = AllClassifiedAdapter {

        }

        binding?.apply {

            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (selectedCategoryFilterData != null) {
                        when (selectedCategoryFilterData?.id) {
                            2 -> {
                                /** Classified **/
                                classifiedViewModel.getClassifiedByUser(
                                    GetClassifiedByUserRequest(
                                        category = "",
                                        email = "",
                                        fetchCatSubCat = true,
                                        keywords = searchView.text.toString(),
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
                                        keywords = searchView.text.toString()
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
                                        keyword = searchView.text.toString(),
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
                                "Please select category first", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                false
            }

            searchResultRv.layoutManager = GridLayoutManager(context, 2)
            searchResultRv.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))

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
                }
            }
        }
        homeFilterViewModel.getServices()

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.searchResultRv?.adapter = classifiedAdapter
                    response.data?.userAdsList?.let { classifiedAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        eventViewModel.allEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    //response.data?.eventList?.let { homeFilterAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        immigrationViewModel.allImmigrationDiscussionListData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    //response.data?.discussionList?.let { homeFilterAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        homeFilterViewModel.service.postValue(null)
        homeFilterViewModel.selectedCategoryFilter.postValue(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}