package com.aaonri.app

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.UserDictionary
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.classified.model.ClassifiedFilterModel
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.Executor


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
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @RequiresApi(Build.VERSION_CODES.R)
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

        val isBioMetricEnable =
            applicationContext?.let { PreferenceManager<Boolean>(it)[Constant.IS_BIOMETRIC_ENABLE, false] }

        val showBioMetricDialogForOnce =
            applicationContext?.let { PreferenceManager<Boolean>(it)[Constant.SHOW_BIOMETRIC_DIALOG_FOR_ONCE, true] }

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
            )
        }

        mainViewModel.getAllActiveAdvertise()
        immigrationViewModel.getDiscussionCategory()

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)

                    /** User clicked on cancel btn **/
                    finish()

                    /*
                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.BLOCKED_USER_ID, "")

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.USER_EMAIL, "")

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.USER_ZIP_CODE, "")

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.USER_CITY, "")

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.USER_STATE, "")

                     applicationContext?.let { it1 -> PreferenceManager<Boolean>(it1) }
                         ?.set(Constant.IS_USER_LOGIN, false)

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.USER_PROFILE_PIC, "")

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.GMAIL_FIRST_NAME, "")

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.GMAIL_LAST_NAME, "")

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.USER_INTERESTED_SERVICES, "")

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.USER_NAME, "")

                     applicationContext?.let { it1 -> PreferenceManager<Boolean>(it1) }
                         ?.set(Constant.IS_JOB_RECRUITER, false)

                     applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
                         ?.set(Constant.USER_PHONE_NUMBER, "")

                     applicationContext?.let { it1 -> PreferenceManager<Int>(it1) }
                         ?.set(Constant.USER_ID, 0)

                     applicationContext?.let { it1 -> PreferenceManager<Boolean>(it1) }
                         ?.set(Constant.IS_BIOMETRIC_ENABLE, false)

                     applicationContext?.let { it1 -> PreferenceManager<Boolean>(it1) }
                         ?.set(Constant.SHOW_BIOMETRIC_DIALOG_FOR_ONCE, true)

                     val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                         .requestIdToken(getString(R.string.gmail_client_id))
                         .requestEmail()
                         .build()

                     FirebaseAuth.getInstance().signOut()
                     LoginManager.getInstance().logOut()
                     mGoogleSignInClient =
                         applicationContext?.let { GoogleSignIn.getClient(it, gso) }!!
                     mGoogleSignInClient.signOut()*/


                    /*Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()*/
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    /*Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()*/
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for aaonri")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        binding?.apply {

            if (showBioMetricDialogForOnce == true) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Confirm")
                builder.setMessage("Do you want to use Bio-Metric?")
                builder.setPositiveButton("OK") { dialog, which ->
                    applicationContext?.let { it1 -> PreferenceManager<Boolean>(it1) }
                        ?.set(Constant.IS_BIOMETRIC_ENABLE, true)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->

                }
                builder.show()
                applicationContext?.let { it1 -> PreferenceManager<Boolean>(it1) }
                    ?.set(Constant.SHOW_BIOMETRIC_DIALOG_FOR_ONCE, false)
            }

            if (isBioMetricEnable == true) {
                val biometricManager = BiometricManager.from(applicationContext)
                when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                    BiometricManager.BIOMETRIC_SUCCESS -> {
                        biometricPrompt.authenticate(promptInfo)
                        Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                    }
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                        Log.e("MY_APP_TAG", "No biometric features available on this device.")
                    }
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                        Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                    }
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                        // Prompts the user to create credentials that your app accepts.
                        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(
                                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                            )
                        }
                        startActivityForResult(enrollIntent, 5)
                    }
                    BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                        TODO()
                    }
                    BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                        TODO()
                    }
                    BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                        TODO()
                    }
                }
            }

            bottomNavigation.setupWithNavController(navController)

            bottomNavigation.setOnItemReselectedListener {
                when (it.title) {
                    "Home" -> homeViewModel.setHomeContentScrollToTop(true)
                    "Classifieds" -> classifiedViewModel.setClassifiedContentScrollToTop(true)
                    "Advertise" -> advertiseViewModel.setAdvertiseContentScrollToTop(true)
                    "More" -> dashboardCommonViewModel.setMoreScreenContentScrollToTop(true)
                }
            }

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

                    postClassifiedViewModel.setSearchQuery("")
                    postClassifiedViewModel.classifiedFilterModel.postValue(
                        ClassifiedFilterModel(
                            selectedCategory = "",
                            selectedSubCategory = "",
                            minPriceRange = "",
                            maxPriceRange = "",
                            zipCode = "",
                            zipCodeCheckBox = false,
                            isDatePublishedSelected = false,
                            isPriceHighToLowSelected = false,
                            isPriceLowToHighSelected = false
                        )
                    )

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
                    if (navController.currentDestination?.id != R.id.eventScreenFragment && navController.currentDestination?.id != R.id.immigrationScreenFragment) {
                        bottomNavigation.visibility = View.VISIBLE
                    }
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

                classifiedViewModel.getClassifiedForHomeScreen(
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

                classifiedViewModel.getClassifiedForHomeScreen(
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
            if (it) {
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

                classifiedViewModel.getClassifiedForHomeScreen(
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
                classifiedViewModel.callClassifiedApiAfterDelete.postValue(false)
            }

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
            val isClassifiedUpdate = data?.getBooleanExtra("isClassifiedUpdate", false)
            val isEventUpdate = data?.getBooleanExtra("isEventUpdate", false)

            if (isClassifiedUpdate == true) {
                classifiedViewModel.setCallClassifiedDetailsApiAfterUpdating(true)
            }
            if (isEventUpdate == true) {
                eventViewModel.setCallEventDetailsApiAfterUpdating(true)
            }

            if (callClassifiedApi == true) {
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