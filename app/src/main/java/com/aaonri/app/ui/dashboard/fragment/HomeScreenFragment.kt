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
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.classified.adapter.ClassifiedGenericAdapter
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.adapter.EventGenericAdapter
import com.aaonri.app.data.home.adapter.InterestAdapter
import com.aaonri.app.data.home.adapter.PoplarClassifiedAdapter
import com.aaonri.app.data.home.model.InterestResponseItem
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.data.immigration.model.ImmigrationCenterModelItem
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.adapter.JobAdapter
import com.aaonri.app.ui.dashboard.home.adapter.HomeInterestsServiceAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset


@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    var homeScreenBinding: FragmentHomeScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
    var immigartinList = mutableListOf<ImmigrationCenterModelItem>()
    //var allClassifiedAdapter: AllClassifiedAdapter? = null
    //var allClassifiedAdapterForHorizontal: AllClassifiedAdapter? = null
    var popularClassifiedAdapter: PoplarClassifiedAdapter? = null
    var homeInterestsServiceAdapter: HomeInterestsServiceAdapter? = null
    var advertiseAdapter: AdvertiseAdapter? = null
    var immigrationAdapter: ImmigrationAdapter? = null
    var jobAdapter: JobAdapter? = null
    var interestAdapter: InterestAdapter? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    //var homeEventAdapter: HomeEventAdapter? = null
    var genericAdapterForClassified: ClassifiedGenericAdapter? = null
    var genericAdapterForEvent: EventGenericAdapter? = null
    val eventId = mutableListOf<Int>()
    var priorityService = ""
    var navigationFromHorizontalSeeAll = ""
    var userInterestedService = ""
    var guestUser = false
    var homeClassifiedWithAdList = mutableListOf<Any>()
    //var homeEventWithAdList = mutableListOf<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        val userCity = context?.let { PreferenceManager<String>(it)[Constant.USER_CITY, ""] }

        userInterestedService =
            context?.let { PreferenceManager<String>(it)[Constant.USER_INTERESTED_SERVICES, ""] }
                .toString()

        /*allClassifiedAdapter = AllClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }*/

        genericAdapterForClassified = ClassifiedGenericAdapter()
        genericAdapterForEvent = EventGenericAdapter()
        adsGenericAdapter1 = AdsGenericAdapter()
        adsGenericAdapter2 = AdsGenericAdapter()

        adsGenericAdapter1?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToAdvertiseWebviewFragment(
                        item.advertisementDetails.url
                    )
                findNavController().navigate(action)
            }
        }

        genericAdapterForClassified?.itemClickListener = { view, item, position ->
            val action =

                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    item.id,
                    false
                )
            findNavController().navigate(action)
        }

        genericAdapterForEvent?.itemClickListenerEvent = { view, item, position ->
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(
                    item.id
                )
            findNavController().navigate(action)
        }

        /*homeEventAdapter = HomeEventAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(it.id)
            findNavController().navigate(action)
        }*/

        advertiseAdapter = AdvertiseAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToAdvertisementDetailsFragment(
                    it.advertisementId
                )
            findNavController().navigate(action)
        }

        jobAdapter = JobAdapter {

        }

        /*allClassifiedAdapterForHorizontal = AllClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }*/

        immigrationAdapter = ImmigrationAdapter()
        immigrationAdapter?.itemClickListener =
            { view, item, position, updateImmigration, deleteImmigration ->
                if (item is ImmigrationCenterModelItem) {
                    val action =HomeScreenFragmentDirections.actionHomeScreenFragmentToImmigrationCenterDetails()
                    findNavController().navigate(action)
                    immigrationViewModel.setSelectedImmigrationCenterItem(item)
                }
            }

        //immigrationAdapter?.setData(listOf("Test 1", "Test 2", "Test 3", "Test 4"))
        jobAdapter?.setData(listOf("Test 1", "Test 2", "Test 3", "Test 4"))

        homeInterestsServiceAdapter =
            HomeInterestsServiceAdapter {
                classifiedViewModel.setSelectedServiceRow(it)
                if (it == "Shop With Us") {
                    homeScreenBinding?.availableServiceHorizontalClassifiedRv?.visibility =
                        View.GONE
                    homeScreenBinding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                    dashboardCommonViewModel.setIsShopWithUsClicked(true)
                } else {
                    homeScreenBinding?.eventTv?.text = it
                }
                navigationFromHorizontalSeeAll = it
                when (it) {
                    "Classifieds" -> {
                        var itemDecoration: RecyclerView.ItemDecoration? = null
                        while (homeScreenBinding?.availableServiceHorizontalClassifiedRv?.itemDecorationCount!! > 0 && (homeScreenBinding?.availableServiceHorizontalClassifiedRv?.getItemDecorationAt(
                                0
                            )?.let { itemDecoration = it }) != null
                        ) {
                            itemDecoration?.let { it1 ->
                                homeScreenBinding?.availableServiceHorizontalClassifiedRv?.removeItemDecoration(
                                    it1
                                )
                            }
                        }
                        homeScreenBinding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.VISIBLE
                        homeScreenBinding?.availableServiceHorizontalRv?.visibility = View.GONE
                        homeScreenBinding?.availableServiceHorizontalClassifiedRv?.layoutManager =
                            GridLayoutManager(context, 2)
                        homeScreenBinding?.availableServiceHorizontalClassifiedRv?.addItemDecoration(
                            GridSpacingItemDecoration(
                                2,
                                32,
                                40
                            )
                        )
                        /*homeScreenBinding?.availableServiceHorizontalClassifiedRv?.adapter =
                            allClassifiedAdapterForHorizontal*/
                        homeScreenBinding?.availableServiceHorizontalClassifiedRv?.adapter =
                            genericAdapterForClassified

                    }
                    "Events" -> {
                        homeScreenBinding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        homeScreenBinding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        homeScreenBinding?.availableServiceHorizontalRv?.margin(0F,0f,0F,0F)
                        homeScreenBinding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        //homeScreenBinding?.availableServiceHorizontalRv?.adapter = homeEventAdapter
                        homeScreenBinding?.availableServiceHorizontalRv?.adapter =
                            genericAdapterForEvent
                    }
                    "Jobs" -> {
                        homeScreenBinding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        homeScreenBinding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        homeScreenBinding?.availableServiceHorizontalRv?.margin(0F,0f,0F,0F)
                        homeScreenBinding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        homeScreenBinding?.availableServiceHorizontalRv?.adapter = jobAdapter
                    }
                    "Immigration" -> {
                        homeScreenBinding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        homeScreenBinding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        homeScreenBinding?.availableServiceHorizontalRv?.margin(0F,0f,0F,4F)
                        val userArray = JSONObject(loadJSONFromAsset()).getJSONArray("immigrationcenterlist")
                        val gson = Gson()

                        for (i in 0 until userArray.length()) {
                            if(!immigartinList.contains( gson.fromJson(
                                    userArray.getString(i),
                                    ImmigrationCenterModelItem::class.java
                                ))) {
                                immigartinList.add(

                                    gson.fromJson(
                                        userArray.getString(i),
                                        ImmigrationCenterModelItem::class.java
                                    )
                                )
                            }

                        }
                        immigrationAdapter?.setData(immigartinList)
                        homeScreenBinding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        homeScreenBinding?.availableServiceHorizontalRv?.adapter =
                            immigrationAdapter
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
                        /*homeScreenBinding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        homeScreenBinding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        homeScreenBinding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        homeScreenBinding?.availableServiceHorizontalRv?.adapter = advertiseAdapter*/
                    }
                }
            }


        homeInterestsServiceAdapter?.setSelectedTab(classifiedViewModel.selectedServiceRow)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.PROFILE_USER, ""] }

        interestAdapter = InterestAdapter {
            navigateToTheSpecificScreen(it.interestDesc)
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
                navigateToTheSpecificScreen(navigationFromHorizontalSeeAll)
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

            adsBelowFirstSectionRv.adapter = adsGenericAdapter1
            adsBelowFirstSectionRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            adsAbovePopularSectionRv.adapter = adsGenericAdapter2
            adsAbovePopularSectionRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        homeViewModel.homeEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    homeScreenBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    homeScreenBinding?.progressBar?.visibility = View.GONE

                    if (response.data?.userEvent?.isNotEmpty() == true) {
                        if (response.data.userEvent.size >= 4) {
                            genericAdapterForEvent?.items = response.data.userEvent.subList(0, 4)
                            //homeEventAdapter?.setData(response.data.userEvent.subList(0, 4))
                        } else {
                            genericAdapterForEvent?.items = response.data.userEvent
                            //homeEventAdapter?.setData(response.data.userEvent)
                        }
                        //homeEventWithAdList = response.data.userEvent.subList(0, 4).toMutableList()
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

        classifiedViewModel.findByEmailData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val list: MutableList<String>? =
                        response.data?.interests?.split(",") as MutableList<String>?
                    /*if (list != null) {
                        if (list[0] != "22") {
                            list.removeAt(0)
                        }
                    }*/
                    callApiAccordingToInterest(list?.get(0))
                    list?.removeAt(0)
                    setUserInterestedServiceRow(list)
                }
                is Resource.Error -> {

                }
                else -> {
                }
            }
        }
        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            guestUser = it
            if (it) {
                setHomeClassifiedData()
                homeScreenBinding?.classifiedTv?.visibility = View.VISIBLE
                setUserInterestedServiceRow()
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

                    /*if (classifiedViewModel.allClassifiedList.size > 3) {
                        *//*allClassifiedAdapter?.setData(
                            classifiedViewModel.allClassifiedList.subList(
                                0,
                                4
                            )
                        )*//*
                        homeClassifiedWithAdList =
                            classifiedViewModel.allClassifiedList.subList(0, 4).toMutableList()

                        //genericAdapter?.items = classifiedViewModel.allClassifiedList.subList(0, 4)
                        *//*allClassifiedAdapterForHorizontal?.setData(
                            classifiedViewModel.allClassifiedList.subList(
                                0,
                                4
                            )
                        )*//*
                    } else {

                        //genericAdapter?.items = classifiedViewModel.allClassifiedList
                        //allClassifiedAdapter?.setData(classifiedViewModel.allClassifiedList)
                        //allClassifiedAdapterForHorizontal?.setData(classifiedViewModel.allClassifiedList)
                    }*/
                    homeClassifiedWithAdList = classifiedViewModel.allClassifiedList.toMutableList()
                    genericAdapterForClassified?.items = homeClassifiedWithAdList

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

        homeViewModel.homeClassifiedInlineAds.observe(viewLifecycleOwner) {
            if (homeClassifiedWithAdList.size >= 4) {
                homeClassifiedWithAdList.add(index = 2, it)
            } else {
                homeClassifiedWithAdList.add(it)
            }
            genericAdapterForClassified?.items = homeClassifiedWithAdList
        }

        /*homeViewModel.homeEventInlineAds.observe(viewLifecycleOwner) {
            if (homeEventWithAdList.size >= 4) {
                homeEventWithAdList.add(index = 2, it)
            } else {
                homeEventWithAdList.add(it)
            }
            genericAdapterForEvent?.items = homeEventWithAdList
        }*/

        homeViewModel.adsBelowFirstSection.observe(viewLifecycleOwner) {
            adsGenericAdapter1?.items = it
        }

        homeViewModel.adsAbovePopularItem.observe(viewLifecycleOwner) {
            adsGenericAdapter2?.items = it
        }


        /*advertiseViewModel.allAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (response.data?.isNotEmpty() == true) {
                        if (response.data.size >= 4) {
                            advertiseAdapter?.setData(response.data.subList(0, 4))
                        } else {
                            advertiseAdapter?.setData(response.data)
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        "Error ${response.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                }
            }
        }*/

        return homeScreenBinding?.root
    }

    private fun setUserInterestedServiceRow(interests: MutableList<String>? = null) {
        homeViewModel.allInterestData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val activeServiceList = mutableListOf<InterestResponseItem>()
                    if (response.data?.isNotEmpty() == true) {
                        if (interests?.isNotEmpty() == true) {
                            response.data.forEach {
                                if (!activeServiceList.contains(it) && it.active && interests.contains(
                                        it.id.toString()
                                    ) && it.interestDesc != "Advertise With Us"
                                ) {
                                    activeServiceList.add(it)
                                }
                            }
                        }
                        homeScreenBinding?.interestRecyclerView?.visibility = View.VISIBLE
                        homeScreenBinding?.interestBorder?.visibility = View.VISIBLE
                        interestAdapter?.setData(response.data.filter { it.active && it.interestDesc.isNotEmpty() && it.interestDesc != "string" })
                        if (interests.isNullOrEmpty()) {
                            homeInterestsServiceAdapter?.setData(response.data.filter { it.active && it.interestDesc.isNotEmpty() && it.interestDesc != "string" && it.interestDesc != "Advertise With Us" } as MutableList<InterestResponseItem>)
                        } else {
                            homeInterestsServiceAdapter?.setData(activeServiceList)
                        }
                    }
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }
    }

    private fun navigateToTheSpecificScreen(interests: String?) {
        if (interests?.isNotEmpty() == true) {
            if (interests.startsWith("27") || interests == "Advertise With Us") {
                //Advertise With Us
                dashboardCommonViewModel.setIsAdvertiseClicked(true)
            } else if (interests.startsWith("2") || interests == "Classifieds") {
                //Classifieds
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
            } else if (interests.startsWith("8") || interests == "Events") {
                //Events
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            } else if (interests.startsWith("3") || interests == "Immigration") {
                //Immigration
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToImmigrationScreenFragment()
                findNavController().navigate(action)
            } else if (interests.startsWith("17") || interests == "Jobs") {
                //Jobs

            } else if (interests.startsWith("22") || interests == "Shop With Us") {
                //Shop With Us
                dashboardCommonViewModel.setIsShopWithUsClicked(true)
            } else if (interests.startsWith("4") || interests == "Astrology") {
                //Astrology

            } else if (interests.startsWith("26") || interests == "Business Needs") {
                //Business Needs

            } else if (interests.startsWith("10") || interests == "Community Connect") {
                //Community Connect

            } else if (interests.startsWith("13") || interests == "Foundation & Donations") {
                //Foundation & Donations

            } else if (interests.startsWith("25") || interests == "Home Needs") {
                //Home Needs

            } else if (interests.startsWith("18") || interests == "Legal Services") {
                //Legal Services

            } else if (interests.startsWith("19") || interests == "Matrimony & Weddings") {
                //Matrimony & Weddings

            } else if (interests.startsWith("20") || interests == "Medical Care") {
                //Medical Care

            } else if (interests.startsWith("21") || interests == "Real Estate") {
                //Real Estate

            } else if (interests.startsWith("5") || interests == "Sports") {
                //Sports

            } else if (interests.startsWith("16") || interests == "Student Services") {
                //Student Services

            } else if (interests.startsWith("24") || interests == "Travel and Stay") {
                //Travel and Stay

            }
        }
    }

    private fun callApiAccordingToInterest(
        interests: String? = "",
    ) {
        if (interests?.isNotEmpty() == true) {
            if (interests == "27") {
                //Advertise With Us
                priorityService = "Advertise With Us"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                homeScreenBinding?.priorityServiceRv?.adapter = advertiseAdapter

            } else if (interests == "2") {
                //Classifieds
                priorityService = "Classifieds"
                setHomeClassifiedData()
            } else if (interests == "8") {
                //Events
                priorityService = "Events"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                //homeScreenBinding?.priorityServiceRv?.adapter = homeEventAdapter
                homeScreenBinding?.priorityServiceRv?.adapter = genericAdapterForEvent

            } else if (interests == "3") {
                //Immigration
                priorityService = "Immigration"
                homeScreenBinding?.priorityServiceRv?.margin(left = 16f, right = 16f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                homeScreenBinding?.priorityServiceRv?.adapter = immigrationAdapter
            } else if (interests == "17") {
                //Jobs
                priorityService = "Jobs"
                homeScreenBinding?.priorityServiceRv?.margin(left = 16f, right = 16f)
                homeScreenBinding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)
                homeScreenBinding?.priorityServiceRv?.adapter = jobAdapter
                Toast.makeText(context, "17", Toast.LENGTH_SHORT).show()
            } else if (interests == "22") {
                //Shop With Us
                priorityService = "Shop With Us"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                homeScreenBinding?.priorityServiceRv?.visibility = View.GONE
            } else if (interests == "4") {
                //Astrology
                priorityService = "Astrology"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "26") {
                //Business Needs
                priorityService = "Business Needs"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "10") {
                //Community Connect
                priorityService = "Community Connect"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "13") {
                //Foundation & Donations
                priorityService = "Foundation & Donations"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "25") {
                //Home Needs
                priorityService = "Home Needs"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "18") {
                //Legal Services
                priorityService = "Legal Services"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "19") {
                //Matrimony & Weddings
                priorityService = "Matrimony & Weddings"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "20") {
                //Medical Care
                priorityService = "Medical Care"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "21") {
                //Real Estate
                priorityService = "Real Estate"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "5") {
                //Sports
                priorityService = "Sports"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)

            } else if (interests == "16") {
                //Student Services
                priorityService = "Student Services"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)

            } else if (interests == "24") {
                //Travel and Stay
                priorityService = "Travel and Stay"
                homeScreenBinding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                homeScreenBinding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests.isBlank()) {
                //Toast.makeText(context, "blank", Toast.LENGTH_SHORT).show()
            }
        }
        homeScreenBinding?.classifiedTv?.text = priorityService
        homeScreenBinding?.classifiedTv?.visibility = View.VISIBLE
        /*if (interests?.isNotEmpty() == true && interests.contains("22")) {
            homeScreenBinding?.classifiedTv?.visibility = View.GONE
            homeScreenBinding?.seeAllClassified?.visibility = View.GONE
            homeScreenBinding?.adsBelowFirstSectionRv?.margin(top = 8f)
        } else {
            homeScreenBinding?.seeAllClassified?.visibility = View.VISIBLE
            homeScreenBinding?.classifiedTv?.text = priorityService
            homeScreenBinding?.classifiedTv?.visibility = View.VISIBLE
        }*/
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
        //homeScreenBinding?.priorityServiceRv?.adapter = allClassifiedAdapter
        homeScreenBinding?.priorityServiceRv?.adapter = genericAdapterForClassified

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


    private fun loadJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream = context?.assets?.open("informationcenter.json")
            val size = inputStream?.available()
            val buffer = size?.let { ByteArray(it) }
            val charset: Charset = Charsets.UTF_8
            inputStream?.read(buffer)
            inputStream?.close()
            json = buffer?.let { String(it, charset) }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }
}