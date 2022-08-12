package com.aaonri.app

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.databinding.ActivityMainBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.custom.ConnectivityReceiver
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    var mainActivityBinding: ActivityMainBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by viewModels()
    val homeViewModel: HomeViewModel by viewModels()
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by viewModels()
    val eventViewModel: EventViewModel by viewModels()
    val advertiseViewModel: AdvertiseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding?.root)

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


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.dashboardNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        val guest = intent.getBooleanExtra("guest", false)
        dashboardCommonViewModel.setGuestUser(guest)

        val email =
            applicationContext?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        mainActivityBinding?.apply {

            bottomNavigation.setupWithNavController(navController)
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
                    classifiedViewModel.findByEmail(email)
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

        homeViewModel.getAllInterest()
        homeViewModel.getHomeEvent()
        homeViewModel.getPopularClassified()

        classifiedViewModel.findByEmailData.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    //callApiAccordingToInterest(response.data?.interests)
                    response.data?.interests?.let {
                        PreferenceManager<String>(applicationContext)[Constant.USER_INTERESTED_SERVICES] =
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

        homeViewModel.popularClassifiedData.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    mainActivityBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    mainActivityBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

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
                mainActivityBinding?.bottomNavigation?.selectedItemId =
                    R.id.classifiedScreenFragment
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(false)
            }
        }

        dashboardCommonViewModel.isAdvertiseClicked.observe(this) {
            if (it) {
                mainActivityBinding?.bottomNavigation?.selectedItemId =
                    R.id.advertiseScreenFragment
                dashboardCommonViewModel.setIsAdvertiseClicked(false)
            }
        }

        dashboardCommonViewModel.isShopWithUsClicked.observe(this) {
            if (it) {
                mainActivityBinding?.bottomNavigation?.selectedItemId =
                    R.id.shopScreenFragment
                dashboardCommonViewModel.setIsShopWithUsClicked(false)
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

    private fun callApiAccordingToInterest(interests: String?) {

    }

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
                if (email != null) {
                    advertiseViewModel.getAllAdvertise(email)
                }
            }
        }
    }
}