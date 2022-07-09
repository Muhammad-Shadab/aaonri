package com.aaonri.app.ui.dashboard.fragment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
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
import com.aaonri.app.data.event.model.Image
import com.aaonri.app.data.home.adapter.PoplarClassifiedAdapter
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    var homeScreenBinding: FragmentHomeScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by activityViewModels()
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    var popularClassifiedAdapter: PoplarClassifiedAdapter? = null
    val eventId = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        allClassifiedAdapter = AllClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(it.id)
            findNavController().navigate(action)
        }
        popularClassifiedAdapter = PoplarClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(it.id)
            findNavController().navigate(action)
        }

        homeScreenBinding?.apply {

            openEvent.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            }

            seeAllEvents.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            }

            seeAllClassified.setOnClickListener {
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
            }

            eventImage1.setOnClickListener {
                eventId.forEachIndexed { index, i ->
                    if (index == 0) {
                        val action =
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(
                                i
                            )
                        findNavController().navigate(action)
                    }
                }
            }

            eventImage2.setOnClickListener {
                eventId.forEachIndexed { index, i ->
                    if (index == 1) {
                        val action =
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(
                                i
                            )
                        findNavController().navigate(action)
                    }
                }
            }

            classifiedRv.layoutManager = GridLayoutManager(context, 2)
            classifiedRv.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))

            popularItemsRv.layoutManager = GridLayoutManager(context, 2)
            popularItemsRv.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))
        }

        homeViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    homeScreenBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE
                    var homeClassified = mutableListOf<UserAds>()
                    response.data?.userAdsList?.forEachIndexed { index, userAds ->
                        if (index <= 3) {
                            if (homeClassified.contains(userAds)) {
                                homeClassified.remove(userAds)
                            } else {
                                homeClassified.add(userAds)
                            }
                        }
                    }

                    response.data?.userAdsList.let {
                        allClassifiedAdapter?.setData(homeClassified)
                    }
                    homeScreenBinding?.classifiedRv?.adapter = allClassifiedAdapter
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
                    val images = mutableListOf<Image>()
                    response.data?.userEvent?.get(0)?.let { eventId.add(it.id) }
                    response.data?.userEvent?.forEach { userEvent ->
                        userEvent.images.forEach { image ->
                            if (images.contains(image)) {
                                images.remove(image)
                            } else {
                                images.add(image)
                            }
                        }
                    }

                    when (images.size) {
                        1 -> {
                            homeScreenBinding?.eventImage1?.margin(right = 20F)
                        }
                        2 -> {
                            homeScreenBinding?.eventImage2?.margin(right = 20F)
                        }
                        3 -> {
                            homeScreenBinding?.eventImage3?.margin(right = 20F)
                        }
                        4 -> {
                            homeScreenBinding?.eventImage4?.margin(right = 20F)
                        }
                    }

                    images.forEachIndexed { index, image ->
                        when (index) {
                            0 -> {
                                homeScreenBinding?.eventImage1?.visibility = View.VISIBLE
                                context?.let { it1 ->
                                    homeScreenBinding?.eventImage1?.let { it2 ->
                                        Glide.with(it1)
                                            .load("https://www.aaonri.com/api/v1/common/eventFile/${image.imagePath}")
                                            .transform(CenterInside(), RoundedCorners(24))
                                            .into(it2)
                                    }
                                }
                            }
                            1 -> {
                                homeScreenBinding?.eventImage2?.visibility = View.VISIBLE
                                context?.let { it1 ->
                                    homeScreenBinding?.eventImage2?.let { it2 ->
                                        Glide.with(it1)
                                            .load("https://www.aaonri.com/api/v1/common/eventFile/${image.imagePath}")
                                            .transform(CenterInside(), RoundedCorners(24))
                                            .into(it2)
                                    }
                                }
                            }
                            2 -> {
                                homeScreenBinding?.eventImage3?.visibility = View.VISIBLE
                                context?.let { it1 ->
                                    homeScreenBinding?.eventImage3?.let { it2 ->
                                        Glide.with(it1)
                                            .load("https://www.aaonri.com/api/v1/common/eventFile/${image.imagePath}")
                                            .transform(CenterInside(), RoundedCorners(24))
                                            .into(it2)
                                    }
                                }
                            }
                            3 -> {
                                context?.let { it1 ->
                                    homeScreenBinding?.eventImage4?.visibility = View.VISIBLE
                                    homeScreenBinding?.eventImage4?.let { it2 ->
                                        Glide.with(it1)
                                            .load("https://www.aaonri.com/api/v1/common/eventFile/${image.imagePath}")
                                            .transform(CenterInside(), RoundedCorners(24))
                                            .into(
                                                it2
                                            )
                                    }
                                }
                            }
                        }
                    }


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

        homeViewModel.popularClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    homeScreenBinding?.homeConstraintLayout?.visibility = View.GONE
                    homeScreenBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.isNotEmpty() == true) {
                        popularClassifiedAdapter?.setData(response.data)
                    }
                    homeScreenBinding?.homeConstraintLayout?.visibility = View.VISIBLE
                    homeScreenBinding?.popularItemsRv?.adapter = popularClassifiedAdapter
                    homeScreenBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    homeScreenBinding?.homeConstraintLayout?.visibility = View.GONE
                    homeScreenBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
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
        homeViewModel.getHomeEvent()
        homeViewModel.getPopularClassified()
    }

    fun View.margin(
        left: Float? = null,
        top: Float? = null,
        right: Float? = null,
        bottom: Float? = null
    ) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}