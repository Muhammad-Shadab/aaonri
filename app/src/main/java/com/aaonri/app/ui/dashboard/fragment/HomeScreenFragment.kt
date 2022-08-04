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
import com.aaonri.app.R
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.adapter.HomeEventAdapter
import com.aaonri.app.data.home.adapter.InterestAdapter
import com.aaonri.app.data.home.adapter.PoplarClassifiedAdapter
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapterForHorizontal
import com.aaonri.app.ui.dashboard.fragment.immigration.ImmigrationAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.adapter.JobAdapter
import com.aaonri.app.ui.dashboard.home.adapter.HomeInterestsServiceAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    var homeScreenBinding: FragmentHomeScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    var allClassifiedAdapterForHorizontal: AllClassifiedAdapterForHorizontal? = null
    var popularClassifiedAdapter: PoplarClassifiedAdapter? = null
    var homeInterestsServiceAdapter: HomeInterestsServiceAdapter? = null
    var advertiseAdapter: AdvertiseAdapter? = null
    var immigrationAdapter: ImmigrationAdapter? = null
    var jobAdapter: JobAdapter? = null
    var interestAdapter: InterestAdapter? = null
    var homeEventAdapter: HomeEventAdapter? = null
    val eventId = mutableListOf<Int>()
    var priorityService = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        val userInterestedService =
            context?.let { PreferenceManager<String>(it)[Constant.USER_INTERESTED_SERVICES, ""] }
        val userCity = context?.let { PreferenceManager<String>(it)[Constant.USER_CITY, ""] }

        allClassifiedAdapter = AllClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }

        homeEventAdapter = HomeEventAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(it.id)
            findNavController().navigate(action)
        }

        advertiseAdapter = AdvertiseAdapter {

        }

        jobAdapter = JobAdapter {

        }

        allClassifiedAdapterForHorizontal = AllClassifiedAdapterForHorizontal {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }

        immigrationAdapter = ImmigrationAdapter {

        }

        immigrationAdapter?.setData(listOf("Test 1", "Test 2", "Test 3", "Test 4"))
        advertiseAdapter?.setData(listOf("Test 1", "Test 2", "Test 3", "Test 4"))
        jobAdapter?.setData(listOf("Test 1", "Test 2", "Test 3", "Test 4"))

        homeInterestsServiceAdapter = HomeInterestsServiceAdapter {
            homeScreenBinding?.eventTv?.text = it
            when (it) {
                "Classifieds" -> {

                    homeScreenBinding?.availableServiceHorizontalRv?.layoutManager =
                        GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                    homeScreenBinding?.availableServiceHorizontalRv?.adapter =
                        allClassifiedAdapterForHorizontal
                }
                "Events" -> {
                    homeScreenBinding?.availableServiceHorizontalRv?.adapter = homeEventAdapter
                }
                "Jobs" -> {
                    homeScreenBinding?.availableServiceHorizontalRv?.adapter = jobAdapter
                }
                "Immigration" -> {
                    homeScreenBinding?.availableServiceHorizontalRv?.adapter = immigrationAdapter
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
                    homeScreenBinding?.availableServiceHorizontalRv?.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    homeScreenBinding?.availableServiceHorizontalRv?.adapter = advertiseAdapter
                }
            }
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
                    dashboardCommonViewModel.setIsAdvertiseClicked(true)
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

            seeAllClassified.setOnClickListener {
                navigateToTheSpecificScreen(userInterestedService)
            }

            seeAllEvents.setOnClickListener {
                navigateToTheSpecificScreen(userInterestedService)
            }

            interestRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            interestRecyclerView.adapter = interestAdapter

            activeService.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            activeService.adapter = homeInterestsServiceAdapter

            /*availableServiceHorizontalRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)*/

            popularItemsRv.layoutManager = GridLayoutManager(context, 2)
            popularItemsRv.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))
        }

        callApiAccordingToInterest(userInterestedService)

        homeViewModel.homeEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    homeScreenBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE

                    if (response.data?.userEvent?.isNotEmpty() == true) {
                        if (response.data.userEvent.size >= 4) {
                            homeEventAdapter?.setData(response.data.userEvent.subList(0, 4))
                        } else {
                            homeEventAdapter?.setData(response.data.userEvent)
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
                    val activeServiceList = mutableListOf<String>()
                    if (response.data?.isNotEmpty() == true) {
                        if (userInterestedService != null) {
                            response.data.forEach {
                                if (!activeServiceList.contains(it.interestDesc) && it.active && userInterestedService.contains(
                                        it.id.toString()
                                    )
                                ) {
                                    activeServiceList.add(it.interestDesc)
                                }
                            }
                        }
                        homeScreenBinding?.interestRecyclerView?.visibility = View.VISIBLE
                        homeScreenBinding?.interestBorder?.visibility = View.VISIBLE
                        interestAdapter?.setData(response.data.filter { it.active && it.interestDesc.isNotEmpty() && it.interestDesc != "string" })
                        homeInterestsServiceAdapter?.setData(activeServiceList)
                    }
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        return homeScreenBinding?.root
    }

    private fun navigateToTheSpecificScreen(interests: String?) {
        if (interests?.isNotEmpty() == true) {
            if (interests.startsWith("27")) {
                //Advertise With Us
                dashboardCommonViewModel.setIsAdvertiseClicked(true)
            } else if (interests.startsWith("2")) {
                //Classifieds
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
            } else if (interests.startsWith("8")) {
                //Events
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            } else if (interests.startsWith("3")) {
                //Immigration

            } else if (interests.startsWith("17")) {
                //Jobs

            } else if (interests.startsWith("22")) {
                //Shop With Us

            } else if (interests.startsWith("4")) {
                //Astrology

            } else if (interests.startsWith("26")) {
                //Business Needs

            } else if (interests.startsWith("10")) {
                //Community Connect

            } else if (interests.startsWith("13")) {
                //Foundation & Donations

            } else if (interests.startsWith("25")) {
                //Home Needs

            } else if (interests.startsWith("18")) {
                //Legal Services

            } else if (interests.startsWith("19")) {
                //Matrimony & Weddings

            } else if (interests.startsWith("20")) {
                //Medical Care

            } else if (interests.startsWith("21")) {
                //Real Estate

            } else if (interests.startsWith("5")) {
                //Sports

            } else if (interests.startsWith("16")) {
                //Student Services

            } else if (interests.startsWith("24")) {
                //Travel and Stay

            }
        }
    }

    private fun callApiAccordingToInterest(
        interests: String? = "",
    ) {
        if (interests?.isNotEmpty() == true) {
            if (interests.startsWith("27")) {
                //Advertise With Us
                priorityService = "Advertise With Us"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                homeScreenBinding?.priorityServiceRv?.adapter = advertiseAdapter

            } else if (interests.startsWith("2")) {
                //Classifieds
                priorityService = "Classifieds"
                setHomeClassifiedData()
            } else if (interests.startsWith("8")) {
                //Events
                priorityService = "Events"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                homeScreenBinding?.priorityServiceRv?.adapter = homeEventAdapter

            } else if (interests.startsWith("3")) {
                //Immigration
                priorityService = "Immigration"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                homeScreenBinding?.priorityServiceRv?.adapter = immigrationAdapter
            } else if (interests.startsWith("17")) {
                //Jobs
                priorityService = "Jobs"
                homeScreenBinding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)
                homeScreenBinding?.priorityServiceRv?.adapter = jobAdapter

            } else if (interests.startsWith("22")) {
                //Shop With Us
                priorityService = "Shop With Us"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("4")) {
                //Astrology
                priorityService = "Astrology"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("26")) {
                //Business Needs
                priorityService = "Business Needs"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("10")) {
                //Community Connect
                priorityService = "Community Connect"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("13")) {
                //Foundation & Donations
                priorityService = "Foundation & Donations"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("25")) {
                //Home Needs
                priorityService = "Home Needs"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests.startsWith("18")) {
                //Legal Services
                priorityService = "Legal Services"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests.startsWith("19")) {
                //Matrimony & Weddings
                priorityService = "Matrimony & Weddings"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests.startsWith("20")) {
                //Medical Care
                priorityService = "Medical Care"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("21")) {
                //Real Estate
                priorityService = "Real Estate"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("5")) {
                //Sports
                priorityService = "Sports"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("16")) {
                //Student Services
                priorityService = "Student Services"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests.startsWith("24")) {
                //Travel and Stay
                priorityService = "Travel and Stay"
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            }
        }
        homeScreenBinding?.classifiedTv?.text = priorityService
        homeScreenBinding?.classifiedTv?.visibility = View.VISIBLE
    }


    private fun setHomeClassifiedData() {

        homeScreenBinding?.priorityServiceRv?.layoutManager =
            GridLayoutManager(context, 2)
        homeScreenBinding?.priorityServiceRv?.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                32,
                40
            )
        )
        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    response.data?.userAdsList?.let {
                        if (classifiedViewModel.allClassifiedList.isEmpty()) {
                            classifiedViewModel.setClassifiedForHomeScreen(it)
                        }
                    }
                    if (classifiedViewModel.allClassifiedList.size > 3) {
                        allClassifiedAdapter?.setData(
                            classifiedViewModel.allClassifiedList.subList(
                                0,
                                4
                            )
                        )
                        allClassifiedAdapterForHorizontal?.setData(
                            classifiedViewModel.allClassifiedList.subList(
                                0,
                                4
                            )
                        )
                    } else {
                        allClassifiedAdapter?.setData(classifiedViewModel.allClassifiedList)
                        allClassifiedAdapterForHorizontal?.setData(classifiedViewModel.allClassifiedList)
                    }
                    homeScreenBinding?.priorityServiceRv?.adapter = allClassifiedAdapter
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