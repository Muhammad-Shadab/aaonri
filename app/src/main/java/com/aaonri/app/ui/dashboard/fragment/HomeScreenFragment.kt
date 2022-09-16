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
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.home.model.InterestResponseItem
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.data.immigration.model.ImmigrationCenterModelItem
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.ClassifiedGenericAdapter
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventGenericAdapter
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobSeekerAdapter
import com.aaonri.app.ui.dashboard.home.adapter.HomeInterestsServiceAdapter
import com.aaonri.app.ui.dashboard.home.adapter.InterestAdapter
import com.aaonri.app.ui.dashboard.home.adapter.PoplarClassifiedAdapter
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
    var binding: FragmentHomeScreenBinding? = null
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
    var jobAdapter: JobSeekerAdapter? = null
    var interestAdapter: InterestAdapter? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()

    //var homeEventAdapter: HomeEventAdapter? = null
    var genericAdapterForClassified: ClassifiedGenericAdapter? = null
    var genericAdapterForEvent: EventGenericAdapter? = null
    val eventId = mutableListOf<Int>()
    var priorityService = ""
    var navigationFromHorizontalSeeAll = ""
    var userInterestedService: MutableList<String>? = mutableListOf()
    var guestUser = false
    var homeClassifiedWithAdList = mutableListOf<Any>()
    //var homeEventWithAdList = mutableListOf<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        val userCity = context?.let { PreferenceManager<String>(it)[Constant.USER_CITY, ""] }

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val list =
            context?.let { PreferenceManager<String>(it)[Constant.USER_INTERESTED_SERVICES, ""] }
                .toString()

        if (list.isNotEmpty()) {
            userInterestedService = list.split(",") as MutableList<String>?
        }


        if (userInterestedService?.size != null) {
            if (userInterestedService?.contains("22") == true) {
                userInterestedService?.remove("22")
            }
            if (userInterestedService?.contains("27") == true) {
                userInterestedService?.remove("27")
            }

            if (userInterestedService?.size == 1) {
                if (userInterestedService?.contains("2") == false) {
                    userInterestedService?.add("2")
                }
            }
            if (userInterestedService?.size == 1) {
                if (userInterestedService?.contains("8") == false) {
                    userInterestedService?.add("8")
                }
            }
        }

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
        adsGenericAdapter2?.itemClickListener = { view, item, position ->
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

        jobAdapter = JobSeekerAdapter()

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
                    val action =
                        HomeScreenFragmentDirections.actionHomeScreenFragmentToImmigrationCenterDetails()
                    findNavController().navigate(action)
                    immigrationViewModel.setSelectedImmigrationCenterItem(item)
                }
            }

        jobAdapter?.setData(listOf("Test 1", "Test 2", "Test 3", "Test 4"))

        homeInterestsServiceAdapter =
            HomeInterestsServiceAdapter {
                classifiedViewModel.setSelectedServiceRow(it)
                /*if (it == "Shop With Us") {
                    homeScreenBinding?.availableServiceHorizontalClassifiedRv?.visibility =
                        View.GONE
                    homeScreenBinding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                    dashboardCommonViewModel.setIsShopWithUsClicked(true)
                } else {
                    homeScreenBinding?.eventTv?.text = it
                }*/
                navigationFromHorizontalSeeAll = it
                when (it) {
                    "Classifieds" -> {
                        var itemDecoration: RecyclerView.ItemDecoration? = null
                        while (binding?.availableServiceHorizontalClassifiedRv?.itemDecorationCount!! > 0 && (binding?.availableServiceHorizontalClassifiedRv?.getItemDecorationAt(
                                0
                            )?.let { itemDecoration = it }) != null
                        ) {
                            itemDecoration?.let { it1 ->
                                binding?.availableServiceHorizontalClassifiedRv?.removeItemDecoration(
                                    it1
                                )
                            }
                        }
                        binding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.VISIBLE
                        binding?.availableServiceHorizontalRv?.visibility = View.GONE
                        binding?.availableServiceHorizontalClassifiedRv?.layoutManager =
                            GridLayoutManager(context, 2)
                        binding?.availableServiceHorizontalClassifiedRv?.addItemDecoration(
                            GridSpacingItemDecoration(
                                2,
                                32,
                                40
                            )
                        )
                        /*homeScreenBinding?.availableServiceHorizontalClassifiedRv?.adapter =
                            allClassifiedAdapterForHorizontal*/
                        binding?.availableServiceHorizontalClassifiedRv?.adapter =
                            genericAdapterForClassified

                    }
                    "Events" -> {
                        binding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        binding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        binding?.availableServiceHorizontalRv?.margin(0F, 0f, 0F, 0F)
                        binding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        //homeScreenBinding?.availableServiceHorizontalRv?.adapter = homeEventAdapter
                        binding?.availableServiceHorizontalRv?.adapter =
                            genericAdapterForEvent
                    }
                    "Jobs" -> {
                        binding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        binding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        binding?.availableServiceHorizontalRv?.margin(0F, 0f, 0F, 0F)
                        binding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        binding?.availableServiceHorizontalRv?.adapter = jobAdapter
                    }
                    "Immigration" -> {
                        binding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        binding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        binding?.availableServiceHorizontalRv?.margin(0F, 0f, 0F, 4F)
                        val userArray =
                            JSONObject(loadJSONFromAsset()).getJSONArray("immigrationcenterlist")
                        val gson = Gson()

                        for (i in 0 until userArray.length()) {
                            if (!immigartinList.contains(
                                    gson.fromJson(
                                        userArray.getString(i),
                                        ImmigrationCenterModelItem::class.java
                                    )
                                )
                            ) {
                                immigartinList.add(
                                    gson.fromJson(
                                        userArray.getString(i),
                                        ImmigrationCenterModelItem::class.java
                                    )
                                )
                            }

                        }
                        immigrationAdapter?.setData(immigartinList)
                        binding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        binding?.availableServiceHorizontalRv?.adapter =
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
                    "Travel and Stay" -> {

                    }
                    "Home Needs" -> {

                    }
                    "Business Needs" -> {

                    }
                }
            }


        homeInterestsServiceAdapter?.setSelectedTab(classifiedViewModel.selectedServiceRow)

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

        binding?.apply {

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
                navigateToTheSpecificScreen(userInterestedService?.get(0))
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
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE

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
                    binding?.progressBar?.visibility = View.GONE
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
                    if (list?.contains("22") == true) {
                        list.remove("22")
                    }
                    if (list?.contains("27") == true) {
                        list.remove("27")
                    }

                    if (list != null) {
                        if (list.size == 1) {
                            if (!list.contains("2")) {
                                list.add("2")
                            }
                        }
                        if (list.size == 1) {
                            if (!list.contains("8")) {
                                list.add("8")
                            }
                        }
                    }

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
                binding?.classifiedTv?.visibility = View.VISIBLE
                setUserInterestedServiceRow()
            }
        }

        homeViewModel.popularClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.homeConstraintLayout?.visibility = View.GONE
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.isNotEmpty() == true) {
                        popularClassifiedAdapter?.setData(response.data)
                    }
                    binding?.homeConstraintLayout?.visibility = View.VISIBLE
                    binding?.popularItemsRv?.adapter = popularClassifiedAdapter
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.homeConstraintLayout?.visibility = View.GONE
                    binding?.progressBar?.visibility = View.GONE
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
                    binding?.progressBar?.visibility = View.GONE
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

        return binding?.root
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
                        binding?.interestRecyclerView?.visibility = View.VISIBLE
                        binding?.interestBorder?.visibility = View.VISIBLE
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
            if (interests == "27" || interests == "Advertise With Us") {
                //Advertise With Us
                dashboardCommonViewModel.setIsAdvertiseClicked(true)
            } else if (interests == "2" || interests == "Classifieds") {
                //Classifieds
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
            } else if (interests == "8" || interests == "Events") {
                //Events
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            } else if (interests == "3" || interests == "Immigration") {
                //Immigration
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToImmigrationScreenFragment()
                findNavController().navigate(action)
            } else if (interests == "17" || interests == "Jobs") {
                //Jobs
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToJobScreenFragment()
                findNavController().navigate(action)
            } else if (interests == "22" || interests == "Shop With Us") {
                //Shop With Us
                dashboardCommonViewModel.setIsShopWithUsClicked(true)
            } else if (interests == "4" || interests == "Astrology") {
                //Astrology

            } else if (interests == "26" || interests == "Business Needs") {
                //Business Needs

            } else if (interests == "10" || interests == "Community Connect") {
                //Community Connect

            } else if (interests == "13" || interests == "Foundation & Donations") {
                //Foundation & Donations

            } else if (interests == "25" || interests == "Home Needs") {
                //Home Needs

            } else if (interests == "18" || interests == "Legal Services") {
                //Legal Services

            } else if (interests == "19" || interests == "Matrimony & Weddings") {
                //Matrimony & Weddings

            } else if (interests == "20" || interests == "Medical Care") {
                //Medical Care

            } else if (interests == "21" || interests == "Real Estate") {
                //Real Estate

            } else if (interests == "5" || interests == "Sports") {
                //Sports

            } else if (interests == "16" || interests == "Student Services") {
                //Student Services

            } else if (interests == "24" || interests == "Travel and Stay") {
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
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                binding?.priorityServiceRv?.adapter = advertiseAdapter

            } else if (interests == "2") {
                //Classifieds
                priorityService = "Classifieds"
                setHomeClassifiedData()
            } else if (interests == "8") {
                //Events
                priorityService = "Events"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                //homeScreenBinding?.priorityServiceRv?.adapter = homeEventAdapter
                binding?.priorityServiceRv?.adapter = genericAdapterForEvent

            } else if (interests == "3") {
                //Immigration
                priorityService = "Immigration"
                binding?.priorityServiceRv?.margin(left = 16f, right = 16f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                binding?.priorityServiceRv?.adapter = immigrationAdapter
            } else if (interests == "17") {
                //Jobs
                priorityService = "Jobs"
                binding?.priorityServiceRv?.margin(left = 10f, right = 10f)
                binding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)
                binding?.priorityServiceRv?.adapter = jobAdapter
            } else if (interests == "22") {
                //Shop With Us
                /*priorityService = "Shop With Us"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                binding?.priorityServiceRv?.visibility = View.GONE*/
            } else if (interests == "4") {
                //Astrology
                priorityService = "Astrology"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "26") {
                //Business Needs
                priorityService = "Business Needs"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "10") {
                //Community Connect
                priorityService = "Community Connect"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "13") {
                //Foundation & Donations
                priorityService = "Foundation & Donations"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "25") {
                //Home Needs
                priorityService = "Home Needs"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "18") {
                //Legal Services
                priorityService = "Legal Services"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "19") {
                //Matrimony & Weddings
                priorityService = "Matrimony & Weddings"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "20") {
                //Medical Care
                priorityService = "Medical Care"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "21") {
                //Real Estate
                priorityService = "Real Estate"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "5") {
                //Sports
                priorityService = "Sports"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)

            } else if (interests == "16") {
                //Student Services
                priorityService = "Student Services"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)

            } else if (interests == "24") {
                //Travel and Stay
                priorityService = "Travel and Stay"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests.isBlank()) {
                //Toast.makeText(context, "blank", Toast.LENGTH_SHORT).show()
            }
        }
        binding?.classifiedTv?.text = priorityService
        binding?.classifiedTv?.visibility = View.VISIBLE
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

        binding?.priorityServiceRv?.layoutManager =
            GridLayoutManager(context, 2)
        binding?.priorityServiceRv?.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                32,
                40
            )
        )
        //homeScreenBinding?.priorityServiceRv?.adapter = allClassifiedAdapter
        binding?.priorityServiceRv?.adapter = genericAdapterForClassified

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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}