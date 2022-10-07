package com.aaonri.app

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.UserDictionary
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.data.immigration.model.ImmigrationFilterModel
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.viewmodel.MainViewModel
import com.aaonri.app.databinding.ActivityMainBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.custom.ConnectivityReceiver
import com.aaonri.app.utils.custom.UserProfileStaticData
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    var binding: ActivityMainBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by viewModels()
    val homeViewModel: HomeViewModel by viewModels()
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by viewModels()
    val eventViewModel: EventViewModel by viewModels()
    val advertiseViewModel: AdvertiseViewModel by viewModels()
    val mainViewModel: MainViewModel by viewModels()
    val immigrationViewModel: ImmigrationViewModel by viewModels()
    val registrationViewModel: RegistrationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        val connectivityReceiver = ConnectivityReceiver()
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        registrationViewModel.getServices()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.dashboardNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        val guest = intent.getBooleanExtra("guest", false)
        dashboardCommonViewModel.setGuestUser(guest)

        val email =
            applicationContext?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        applicationContext?.let { it1 -> PreferenceManager<Int>(it1) }
            ?.set("selectedHomeServiceRow", -1)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // On JellyBean & above, you can provide a shortcut and an explicit Locale
            UserDictionary.Words.addWord(this, "MadeUpWord", 10, "Mad", Locale.getDefault());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            UserDictionary.Words.addWord(
                this,
                "MadeUpWord",
                10,
                UserDictionary.Words.LOCALE_TYPE_CURRENT
            );
        }


        mainViewModel.getAllActiveAdvertise()
        immigrationViewModel.getDiscussionCategory()

        binding?.apply {

            bottomNavigation.setupWithNavController(navController)

            /** Clearing the filter data when user navigate to the non filter screens**/
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.homeScreenFragment || destination.id == R.id.classifiedScreenFragment || destination.id == R.id.advertiseScreenFragment ||
                    destination.id == R.id.shopScreenFragment || destination.id == R.id.moreScreenFragment
                ) {
                    bottomNavigation.visibility = View.VISIBLE
                } else {
                    bottomNavigation.visibility = View.GONE
                }

                if (destination.id == R.id.homeScreenFragment || destination.id == R.id.shopScreenFragment || destination.id == R.id.advertiseScreenFragment || destination.id == R.id.moreScreenFragment) {
                    if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty() ||
                        postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty() ||
                        postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty() ||
                        postClassifiedViewModel.categoryFilter.isNotEmpty() ||
                        postClassifiedViewModel.subCategoryFilter.isNotEmpty() ||
                        postClassifiedViewModel.changeSortTriplet.first ||
                        postClassifiedViewModel.changeSortTriplet.second ||
                        postClassifiedViewModel.changeSortTriplet.third
                    ) {
                        postClassifiedViewModel.setClearAllFilter(true)
                        postClassifiedViewModel.setClickOnClearAllFilterBtn(true)
                    }
                    /** clearing immigration filter **/
                    immigrationViewModel.setFilterData(
                        ImmigrationFilterModel(
                            fifteenDaysSelected = false,
                            threeMonthSelected = false,
                            oneYearSelected = false,
                            activeDiscussion = false,
                            atLeastOnDiscussion = false
                        )
                    )
                }
            }

            dashboardCommonViewModel.showBottomNavigation.observe(this@MainActivity) {
                if (it) {
                    bottomNavigation.visibility = View.VISIBLE
                } else {
                    bottomNavigation.visibility = View.GONE
                }
            }
        }


        dashboardCommonViewModel.isGuestUser.observe(this) { isGuestUser ->
            if (isGuestUser) {
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

                eventViewModel.getAllEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = false,
                        userId = "",
                        zip = ""
                    )
                )
            } else {

                classifiedViewModel.setIsLikedButtonClicked(true)

                if (email != null) {
                    registrationViewModel.findByEmail(email)
                    eventViewModel.getRecentEvent(email)
                }

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

                classifiedViewModel.getMyClassified(
                    GetClassifiedByUserRequest(
                        category = "",
                        email = if (email?.isNotEmpty() == true) email else "",
                        fetchCatSubCat = true,
                        keywords = "",
                        location = "",
                        maxPrice = 0,
                        minPrice = 0,
                        myAdsOnly = true,
                        popularOnAoonri = null,
                        subCategory = "",
                        zipCode = ""
                    )
                )

                eventViewModel.getMyEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = true,
                        userId = if (email?.isNotEmpty() == true) email else "",
                        zip = ""
                    )
                )

                eventViewModel.getAllEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = false,
                        userId = if (email?.isNotEmpty() == true) email else "",
                        zip = ""
                    )
                )

                if (email != null) {
                    advertiseViewModel.getAllAdvertise(email)
                }

            }
        }

        mainViewModel.allActiveAdvertise.observe(this@MainActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                }
                is Resource.Success -> {
                    val adsAbovePopularItem = mutableListOf<FindAllActiveAdvertiseResponseItem>()
                    val adsBelowFirstSection = mutableListOf<FindAllActiveAdvertiseResponseItem>()
                    val classifiedTopBanner = mutableListOf<FindAllActiveAdvertiseResponseItem>()
                    val classifiedAdJustAboveFooter =
                        mutableListOf<FindAllActiveAdvertiseResponseItem>()
                    val classifiedAdOnClassifiedDetails =
                        mutableListOf<FindAllActiveAdvertiseResponseItem>()
                    val eventAdJustAboveFooter = mutableListOf<FindAllActiveAdvertiseResponseItem>()
                    val eventAdOnDetailsScreen = mutableListOf<FindAllActiveAdvertiseResponseItem>()
                    val eventTopBanner = mutableListOf<FindAllActiveAdvertiseResponseItem>()
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.forEach { data ->

                        /** one ad for classified grid**/
                        /*if (data.advertisementPageLocation.locationId == 2) {
                            homeViewModel.setHomeClassifiedInlineAds(data)
                        }
                        */
                        /** one ad for event **//*
                        if (data.advertisementPageLocation.locationId == 3) {
                            homeViewModel.setHomeEventInlineAds(data)
                        }*/

                        /** below first section location id **/
                        if (data.advertisementPageLocation.locationId == 1 ||
                            data.advertisementPageLocation.locationId == 2 ||
                            data.advertisementPageLocation.locationId == 6 ||
                            data.advertisementPageLocation.locationId == 7 ||
                            data.advertisementPageLocation.locationId == 8 ||
                            data.advertisementPageLocation.locationId == 11 ||
                            data.advertisementPageLocation.locationId == 13
                        ) {
                            if (!adsBelowFirstSection.contains(data)) {
                                adsBelowFirstSection.add(data)
                            }
                        }

                        /** all advertise for above popular item**/
                        if (data.advertisementPageLocation.locationId == 9 ||
                            data.advertisementPageLocation.locationId == 3 ||
                            data.advertisementPageLocation.locationId == 4 ||
                            data.advertisementPageLocation.locationId == 5 ||
                            data.advertisementPageLocation.locationId == 10 ||
                            data.advertisementPageLocation.locationId == 12 ||
                            data.advertisementPageLocation.locationId == 14 ||
                            data.advertisementPageLocation.locationId == 15
                        ) {
                            if (!adsAbovePopularItem.contains(data)) {
                                adsAbovePopularItem.add(data)
                            }
                        }

                        /**classified top banner page data **/
                        if (data.advertisementPageLocation.locationId == 16) {
                            if (!classifiedTopBanner.contains(data)) {
                                classifiedTopBanner.add(data)
                            }
                        }

                        /**classified landing page data **/
                        if (data.advertisementPageLocation.locationId == 17 ||
                            data.advertisementPageLocation.locationId == 18 ||
                            data.advertisementPageLocation.locationId == 19
                        ) {
                            if (!classifiedAdJustAboveFooter.contains(data)) {
                                classifiedAdJustAboveFooter.add(data)
                            }
                        }

                        /** classified details screen advertise **/
                        if (data.advertisementPageLocation.locationId == 32 ||
                            data.advertisementPageLocation.locationId == 33 ||
                            data.advertisementPageLocation.locationId == 34 ||
                            data.advertisementPageLocation.locationId == 35
                        ) {
                            if (!classifiedAdOnClassifiedDetails.contains(data)) {
                                classifiedAdOnClassifiedDetails.add(data)
                            }
                        }

                        /** event landing page data **/
                        if (data.advertisementPageLocation.locationId == 21 ||
                            data.advertisementPageLocation.locationId == 22 ||
                            data.advertisementPageLocation.locationId == 23
                        ) {
                            if (!eventAdJustAboveFooter.contains(data)) {
                                eventAdJustAboveFooter.add(data)
                            }
                        }

                        if (data.advertisementPageLocation.locationId == 20) {
                            if (!eventTopBanner.contains(data)) {
                                eventTopBanner.add(data)
                            }
                        }

                        /** event details screen advertise **/
                        if (data.advertisementPageLocation.locationId == 36 ||
                            data.advertisementPageLocation.locationId == 37 ||
                            data.advertisementPageLocation.locationId == 38
                        ) {
                            if (!eventAdOnDetailsScreen.contains(data)) {
                                eventAdOnDetailsScreen.add(data)
                            }
                        }
                    }

                    homeViewModel.setAdsBelowFirstSection(adsBelowFirstSection)
                    homeViewModel.setAdsAbovePopularItem(adsAbovePopularItem)
                    ActiveAdvertiseStaticData.setClassifiedAdsData(
                        topBannerAds = classifiedTopBanner,
                        bottomAds = classifiedAdJustAboveFooter,
                        detailsScreenAds = classifiedAdOnClassifiedDetails
                    )
                    ActiveAdvertiseStaticData.setEventAdsData(
                        topBannerAds = eventTopBanner,
                        bottomAds = eventAdJustAboveFooter,
                        detailsScreenAds = eventAdOnDetailsScreen
                    )

                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        homeViewModel.getAllInterest()
        homeViewModel.getHomeEvent()
        homeViewModel.getPopularClassified()

        registrationViewModel.findByEmailData.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    //callApiAccordingToInterest(response.data?.interests)
                    /*response.data?.interests?.let {
                        PreferenceManager<String>(applicationContext)[Constant.USER_INTERESTED_SERVICES] =
                            it
                    }*/

                    response.data?.let { UserProfileStaticData.setUserProfileDataValue(it) }

                    response.data?.interests?.let {
                        applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(Constant.USER_INTERESTED_SERVICES, it)
                    }

                    response.data?.profilePic?.let {
                        applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(
                                Constant.USER_PROFILE_PIC,
                                "${BuildConfig.BASE_URL}/api/v1/common/profileFile/$it"
                            )
                    }

                    response.data?.emailId?.let {
                        applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(Constant.USER_EMAIL, it)
                    }

                    response.data?.isJobRecruiter?.let {
                        applicationContext?.let { it1 -> PreferenceManager<Boolean>(it1) }
                            ?.set(Constant.IS_JOB_RECRUITER, it)
                    }

                    response.data?.userId?.let {
                        PreferenceManager<Int>(applicationContext)[Constant.USER_ID] =
                            it
                    }

                    response.data?.city?.let {
                        PreferenceManager<String>(applicationContext)[Constant.USER_CITY] = it
                    }

                    response.data?.zipcode?.let {
                        PreferenceManager<String>(applicationContext)[Constant.USER_ZIP_CODE] = it
                    }

                    response.data?.phoneNo?.let {
                        PreferenceManager<String>(applicationContext)[Constant.USER_PHONE_NUMBER] =
                            it
                    }
                    response.data?.firstName?.let {
                        PreferenceManager<String>(applicationContext)[Constant.USER_NAME] =
                            "$it ${response.data.lastName}"
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(
                        applicationContext,
                        "Error ${response.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                }
            }
        }

        /*dashboardCommonViewModel.isFilterApplied.observe(this) {
            val keyword =
                applicationContext?.let { PreferenceManager<String>(it)[ClassifiedConstant.SEARCH_KEYWORD_FILTER, ""] }
            if (it.equals("callEventApiWithFilter")) {
                eventViewModel.getMyEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = if (keyword?.isNotEmpty() == true) keyword else "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = true,
                        userId = if (email?.isNotEmpty() == true) email else "",
                        zip = ""
                    )
                )
                eventViewModel.getAllEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = if (keyword?.isNotEmpty() == true) keyword else "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = false,
                        userId = "",
                        zip = ""
                    )
                )
            } else if (it.equals("callEventApi")) {
                eventViewModel.getMyEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = true,
                        userId = if (email?.isNotEmpty() == true) email else "",
                        zip = ""
                    )
                )
                eventViewModel.getAllEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = false,
                        userId = "",
                        zip = ""
                    )
                )
            }
        }*/

        /*homeViewModel.popularClassifiedData.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }*/

        /*classifiedViewModel.classifiedByUserData.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    mainActivityBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    mainActivityBinding?.progressBar?.visibility = View.GONE
                    response.data?.userAdsList?.let {
                        if (classifiedViewModel.allClassifiedList.isEmpty()) {
                            classifiedViewModel.setClassifiedForHomeScreen(it)
                        } else {

                        }
                    }
                }
                is Resource.Error -> {
                    mainActivityBinding?.progressBar?.visibility = View.GONE
                    *//*Toast.makeText(applicationContext, "${response.message}", Toast.LENGTH_SHORT)
                        .show()*//*
                }
                else -> {

                }
            }
        }*/

        eventViewModel.allEventData.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (response.data?.eventList?.isNotEmpty() == true) {
                        if (eventViewModel.allEventList.isEmpty()) {
                            eventViewModel.setAllEventList(response.data.eventList)
                        }
                    }
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        dashboardCommonViewModel.isSeeAllClassifiedClicked.observe(this) {
            if (it) {
                binding?.bottomNavigation?.selectedItemId =
                    R.id.classifiedScreenFragment
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(false)
            }
        }

        dashboardCommonViewModel.isAdvertiseClicked.observe(this) {
            if (it) {
                binding?.bottomNavigation?.selectedItemId =
                    R.id.advertiseScreenFragment
                dashboardCommonViewModel.setIsAdvertiseClicked(false)
            }
        }

        dashboardCommonViewModel.isShopWithUsClicked.observe(this) {
            if (it) {
                binding?.bottomNavigation?.selectedItemId = R.id.shopScreenFragment
                dashboardCommonViewModel.setIsShopWithUsClicked(false)
                applicationContext?.let { it1 -> PreferenceManager<Int>(it1) }
                    ?.set("selectedHomeServiceRow", 0)
            }
        }

        classifiedViewModel.callClassifiedApiAfterDelete.observe(this) {
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

            classifiedViewModel.getMyClassified(
                GetClassifiedByUserRequest(
                    category = "",
                    email = if (email?.isNotEmpty() == true) email else "",
                    fetchCatSubCat = true,
                    keywords = "",
                    location = "",
                    maxPrice = 0,
                    minPrice = 0,
                    myAdsOnly = true,
                    popularOnAoonri = null,
                    subCategory = "",
                    zipCode = ""
                )
            )
        }

        eventViewModel.callEventApiAfterDelete.observe(this) {
            if (email != null) {
                eventViewModel.getRecentEvent(email)
            }

            if (it) {
                eventViewModel.getMyEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = true,
                        userId = if (email?.isNotEmpty() == true) email else "",
                        zip = ""
                    )
                )
                eventViewModel.getAllEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = false,
                        userId = "",
                        zip = ""
                    )
                )
            }
        }

        advertiseViewModel.callAdvertiseApi.observe(this) {
            if (it) {
                if (email != null) {
                    advertiseViewModel.getAllAdvertise(email)
                }
            }
        }
    }

    /*private fun callApiAccordingToInterest(interests: String?) {

    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val email =
            applicationContext?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val callClassifiedApi = data?.getBooleanExtra("callClassifiedApi", false)
            val callEventApi = data?.getBooleanExtra("callEventApi", false)
            val callAdvertiseApi = data?.getBooleanExtra("callAdvertiseApi", false)

            if (callClassifiedApi == true) {

                classifiedViewModel.setCallClassifiedDetailsApiAfterUpdating(true)

                classifiedViewModel.getMyClassified(
                    GetClassifiedByUserRequest(
                        category = "",
                        email = if (email?.isNotEmpty() == true) email else "",
                        fetchCatSubCat = true,
                        keywords = "",
                        location = "",
                        maxPrice = 0,
                        minPrice = 0,
                        myAdsOnly = true,
                        popularOnAoonri = null,
                        subCategory = "",
                        zipCode = ""
                    )
                )
            } else if (callEventApi == true) {
                eventViewModel.setCallEventDetailsApiAfterUpdating(true)

                if (email != null) {
                    eventViewModel.getRecentEvent(email)
                }

                eventViewModel.getMyEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = true,
                        userId = if (email?.isNotEmpty() == true) email else "",
                        zip = ""
                    )
                )
                eventViewModel.getAllEvent(
                    AllEventRequest(
                        category = "",
                        city = "",
                        from = "",
                        isPaid = "",
                        keyword = "",
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = false,
                        userId = "",
                        zip = ""
                    )
                )
            } else if (callAdvertiseApi == true) {
                advertiseViewModel.setCallAdvertiseDetailsApiAfterUpdating(true)
                if (email != null) {
                    advertiseViewModel.getAllAdvertise(email)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext?.let { it1 -> PreferenceManager<Int>(it1) }
            ?.set("selectedHomeServiceRow", -1)
        UserProfileStaticData.setUserProfileDataValue(null)
        binding = null
    }

}