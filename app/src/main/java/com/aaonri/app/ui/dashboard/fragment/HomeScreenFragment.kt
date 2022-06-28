package com.aaonri.app.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    var homeScreenBinding: FragmentHomeScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by activityViewModels()
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    var popularClassifiedAdapter: AllClassifiedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        allClassifiedAdapter = AllClassifiedAdapter {
            homeViewModel.setSendDataToClassifiedDetailsScreen(it)
            //findNavController().navigate(R.id.action_homeScreenFragment_to_classifiedDetailsFragment)
        }
        popularClassifiedAdapter = AllClassifiedAdapter {
            //homeViewModel.setSendDataToClassifiedDetailsScreen(it)
            //findNavController().navigate(R.id.action_homeScreenFragment_to_classifiedDetailsFragment)
        }

        homeScreenBinding?.apply {

            homeTv.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            }

            seeAllClassified.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreenFragment_to_classifiedScreenFragment)
            }

            classifiedRv.layoutManager = GridLayoutManager(context, 2)
            classifiedRv.addItemDecoration(GridSpacingItemDecoration(2, 42, 40))

            popularItemsRv.layoutManager = GridLayoutManager(context, 2)
            popularItemsRv.addItemDecoration(GridSpacingItemDecoration(2, 42, 40))
        }

        homeViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    homeScreenBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE
                    val poplarClassified = mutableListOf<UserAds>()
                    val homeClassified = mutableListOf<UserAds>()
                    homeClassified.addAll(response.data?.userAdsList?.subList(0, 4)!!)
                    response.data.userAdsList.forEach {
                        if (it.popularOnAaonri) {
                            poplarClassified.add(it)
                        }
                    }
                    response.data.userAdsList.let {
                        allClassifiedAdapter?.setData(homeClassified)
                        popularClassifiedAdapter?.setClassifiedHotData(
                            poplarClassified.subList(
                                0,
                                4
                            )
                        )
                    }
                    homeScreenBinding?.classifiedRv?.adapter = allClassifiedAdapter
                    homeScreenBinding?.popularItemsRv?.adapter = popularClassifiedAdapter
                }
                is Resource.Error -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        homeViewModel.homeEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    homeScreenBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE

                }
                is Resource.Error -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }

        }


        return homeScreenBinding?.root
    }

    override fun onResume() {
        super.onResume()

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) { isGuestUser ->
            if (isGuestUser) {
                homeViewModel.getClassifiedByUser(
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
                homeViewModel.getClassifiedByUser(
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
            }
        }

        //homeViewModel.getHomeEvent()
    }
}