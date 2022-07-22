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
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.model.Image
import com.aaonri.app.data.home.adapter.InterestAdapter
import com.aaonri.app.data.home.adapter.PoplarClassifiedAdapter
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    var homeScreenBinding: FragmentHomeScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    var popularClassifiedAdapter: PoplarClassifiedAdapter? = null
    var interestAdapter: InterestAdapter? = null
    val eventId = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        val userCity = context?.let { PreferenceManager<String>(it)[Constant.USER_CITY, ""] }

        allClassifiedAdapter = AllClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.PROFILE_USER, ""] }

        interestAdapter = InterestAdapter {
            when (it.interestDesc) {
                "Classifieds" -> {
                    dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
                }
                "Events" -> {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
                }
                "Jobs" -> {
                }
                "Immigration" -> {
                }
                "Astrology" -> {
                }
                "Sports" -> {
                }
                "Community Connect" -> {

                }
                "Foundation & Donations" -> {

                }
                "Student Services" -> {

                }
                "Legal Services" -> {

                }
                "Matrimony & Weddings" -> {

                }
                "Medical Care" -> {

                }
                "Real Estate" -> {

                }
                "Shop With Us" -> {

                }
                "Travel and Stay" -> {

                }
                "Home Needs" -> {

                }
                "Business Needs" -> {

                }
                "Advertise With Us" -> {

                }
            }
        }

        popularClassifiedAdapter = PoplarClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }

        homeScreenBinding?.apply {

            if (userCity != null) {
                if (userCity.isNotEmpty()) {
                    locationTv.text = userCity
                    locationTv.visibility = View.VISIBLE
                    locationIcon.visibility = View.VISIBLE
                } else {
                    locationTv.visibility = View.GONE
                    locationIcon.visibility = View.GONE
                }
            }


            context?.let { Glide.with(it).load(profile).into(profilePicIv) }

            /*openEvent.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            }*/

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
                                i,
                                false
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
                                i,
                                false
                            )
                        findNavController().navigate(action)
                    }
                }
            }

            eventImage3.setOnClickListener {
                eventId.forEachIndexed { index, i ->
                    if (index == 2) {
                        val action =
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(
                                i,
                                false
                            )
                        findNavController().navigate(action)
                    }
                }
            }

            eventImage4.setOnClickListener {
                eventId.forEachIndexed { index, i ->
                    if (index == 3) {
                        val action =
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(
                                i,
                                false
                            )
                        findNavController().navigate(action)
                    }
                }
            }

            interestRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            interestRecyclerView.adapter = interestAdapter

            classifiedRv.layoutManager = GridLayoutManager(context, 2)
            classifiedRv.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))

            popularItemsRv.layoutManager = GridLayoutManager(context, 2)
            popularItemsRv.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))
        }

        /*classifiedViewModel.classifiedListForHomeScreen.observe(viewLifecycleOwner) {
            var homeClassified = mutableListOf<UserAds>()
            it.forEachIndexed { index, userAds ->
                if (index <= 3) {
                    if (homeClassified.contains(userAds)) {
                        homeClassified.remove(userAds)
                    } else {
                        homeClassified.add(userAds)
                    }
                }
            }
            it.let {
                allClassifiedAdapter?.setData(homeClassified)
            }
            homeScreenBinding?.classifiedRv?.adapter = allClassifiedAdapter
        }*/

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    homeScreenBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE
                    response.data?.userAdsList?.let {
                        if (classifiedViewModel.classifiedListForHomeScreen.isEmpty()) {
                            classifiedViewModel.setClassifiedForHomeScreen(it)
                            setHomeClassifiedData()
                        } else {
                            setHomeClassifiedData()
                        }
                    }
                    homeScreenBinding?.classifiedRv?.adapter = allClassifiedAdapter
                    if (response.data?.userAdsList?.isEmpty() == true) {
                        /*activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "No result found", Snackbar.LENGTH_LONG
                            ).show()
                        }*/
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

        homeViewModel.homeEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    homeScreenBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE
                    var images = mutableListOf<Image>()
                    response.data?.userEvent?.forEachIndexed { index, userEvent ->
                        when (index) {
                            0 -> {
                                eventId.add((userEvent.id))
                            }
                            1 -> {
                                eventId.add((userEvent.id))
                            }
                            2 -> {
                                eventId.add((userEvent.id))
                            }
                            3 -> {
                                eventId.add((userEvent.id))
                            }
                        }
                    }

                    response.data?.userEvent?.forEach { userEvent ->
                        userEvent.images.forEach { image ->
                            if (images.contains(image)) {
                                images.remove(image)
                            } else {
                                images.add(image)
                            }
                        }
                    }

                    if (images.size > 3) {
                        images = images.subList(0, 3)
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
                    } else {
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
                    }


                    images.forEachIndexed { index, image ->
                        when (index) {
                            0 -> {
                                homeScreenBinding?.eventImage1?.visibility = View.VISIBLE
                                context?.let { it1 ->
                                    homeScreenBinding?.eventImage1?.let { it2 ->
                                        Glide.with(it1)
                                            .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${image.imagePath}")
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
                                            .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${image.imagePath}")
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
                                            .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${image.imagePath}")
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
                                            .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${image.imagePath}")
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

        homeViewModel.allInterestData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (response.data?.isNotEmpty() == true) {
                        homeScreenBinding?.interestRecyclerView?.visibility = View.VISIBLE
                        homeScreenBinding?.interestBorder?.visibility = View.VISIBLE
                        interestAdapter?.setData(response.data.filter { it.active && it.interestDesc.isNotEmpty() && it.interestDesc != "string" })
                    }
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }


        return homeScreenBinding?.root
    }

    private fun setHomeClassifiedData() {

        if (classifiedViewModel.classifiedListForHomeScreen.size > 3) {
            allClassifiedAdapter?.setData(
                classifiedViewModel.classifiedListForHomeScreen.subList(
                    0,
                    4
                )
            )
        } else {
            allClassifiedAdapter?.setData(classifiedViewModel.classifiedListForHomeScreen)
        }


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