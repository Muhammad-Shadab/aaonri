package com.aaonri.app.ui.dashboard.fragment.classified.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentAllClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllClassifiedFragment : Fragment() {
    var allClassifiedBinding: FragmentAllClassifiedBinding? = null
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allClassifiedBinding =
            FragmentAllClassifiedBinding.inflate(inflater, container, false)
        allClassifiedAdapter = AllClassifiedAdapter {
            postClassifiedViewModel.setSendDataToClassifiedDetailsScreen(it.id)
            postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(
                value = true,
                isMyClassifiedScreen = false
            )
        }

        allClassifiedBinding?.apply {
            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))
        }

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    allClassifiedBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    allClassifiedBinding?.progressBar?.visibility = View.GONE

                    response.data?.userAdsList?.let { adsList ->
                        if (postClassifiedViewModel.changeSortTriplet.first) {
                            allClassifiedAdapter?.setData(adsList)
                        } else if (postClassifiedViewModel.changeSortTriplet.second) {
                            allClassifiedAdapter?.setData(adsList.sortedBy { it.askingPrice })
                        } else if (postClassifiedViewModel.changeSortTriplet.third) {
                            allClassifiedAdapter?.setData(adsList.sortedByDescending { it.askingPrice })
                        } else {
                            allClassifiedAdapter?.setData(adsList)
                        }
                        //classifiedViewModel.setClassifiedForHomeScreen(adsList)
                        allClassifiedBinding?.recyclerViewClassified?.visibility = View.VISIBLE
                    }
                    allClassifiedBinding?.recyclerViewClassified?.adapter = allClassifiedAdapter
                    if (response.data?.userAdsList?.isEmpty() == true) {
                        allClassifiedBinding?.recyclerViewClassified?.visibility = View.GONE
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "No result found", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                is Resource.Error -> {
                    allClassifiedBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        postClassifiedViewModel.keyClassifiedKeyboardListener.observe(viewLifecycleOwner) {
            if (it) {
                allClassifiedBinding?.recyclerViewClassified?.visibility = View.VISIBLE
                allClassifiedAdapter?.setData(classifiedViewModel.allClassifiedList)
            }
        }

        return allClassifiedBinding?.root
    }

    /*override fun onResume() {
        super.onResume()

        arguments?.let { bundle ->
            val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
            dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
                if (bundle.getBoolean("filterEnabled")) {
                    var minValue =
                        context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MIN_VALUE_FILTER, "0"] }
                    var maxValue =
                        context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MAX_VALUE_FILTER, "0"] }
                    val zipCodeFilter =
                        context?.let { PreferenceManager<String>(it)[ClassifiedConstant.ZIPCODE_FILTER, ""] }
                    val keyword =
                        context?.let { PreferenceManager<String>(it)[ClassifiedConstant.SEARCH_KEYWORD_FILTER, ""] }
                    if (minValue?.isEmpty() == true) {
                        minValue = "0"
                    }

                    if (maxValue?.isEmpty() == true) {
                        maxValue = "0"
                    }

                    if (it) {
                        classifiedViewModel.getClassifiedByUser(
                            GetClassifiedByUserRequest(
                                category = "",
                                email = "",
                                fetchCatSubCat = true,
                                keywords = keyword,
                                location = "",
                                maxPrice = maxValue?.toInt(),
                                minPrice = minValue?.toInt(),
                                myAdsOnly = false,
                                popularOnAoonri = null,
                                subCategory = "",
                                zipCode = zipCodeFilter
                            )
                        )
                    } else {
                        classifiedViewModel.getClassifiedByUser(
                            GetClassifiedByUserRequest(
                                category = "",
                                email = if (email?.isNotEmpty() == true) email else "",
                                fetchCatSubCat = true,
                                keywords = keyword,
                                location = "",
                                maxPrice = maxValue?.toInt(),
                                minPrice = minValue?.toInt(),
                                myAdsOnly = false,
                                popularOnAoonri = null,
                                subCategory = "",
                                zipCode = zipCodeFilter
                            )
                        )
                    }
                } else {
                    *//*if (it) {
                        classifiedViewModel.getClassifiedByUser(
                            GetClassifiedByUserRequest(
                                category = "",
                                email = "",
                                fetchCatSubCat = true,
                                keywords = "",
                                location = "",
                                maxPrice = 0,
                                minPrice = 0,
                                myAdsOnly = false,
                                popularOnAoonri = null,
                                subCategory = "",
                                zipCode = ""
                            )
                        )
                    } else {
                        classifiedViewModel.getClassifiedByUser(
                            GetClassifiedByUserRequest(
                                category = "",
                                email = if (email?.isNotEmpty() == true) email else "",
                                fetchCatSubCat = true,
                                keywords = "",
                                location = "",
                                maxPrice = 0,
                                minPrice = 0,
                                myAdsOnly = false,
                                popularOnAoonri = null,
                                subCategory = "",
                                zipCode = ""
                            )
                        )
                    }*//*
                }
            }
        }
    }*/
}

