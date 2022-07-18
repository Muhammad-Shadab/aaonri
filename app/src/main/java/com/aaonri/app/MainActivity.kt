package com.aaonri.app

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.databinding.ActivityMainBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    var mainActivityBinding: ActivityMainBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by viewModels()
    val homeViewModel: HomeViewModel by viewModels()
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding?.root)
        supportActionBar?.hide()

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
            }
        }

        /*if (FirebaseAuth.getInstance().currentUser != null) {
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
                    userId = if (gmailEmail?.isNotEmpty() == true) gmailEmail else "",
                    zip = ""
                )
            )

            if (gmailEmail != null) {
                eventViewModel.getRecentEvent(
                    gmailEmail
                )
            }
        }*/

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
                        userId = "",
                        zip = ""
                    )
                )

            }
        }


        homeViewModel.getAllInterest()
        homeViewModel.getHomeEvent()
        homeViewModel.getPopularClassified()

        dashboardCommonViewModel.isSeeAllClassifiedClicked.observe(this) {
            if (it) {
                mainActivityBinding?.bottomNavigation?.selectedItemId =
                    R.id.classifiedScreenFragment
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val email =
            applicationContext?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val callClassifiedApi = data?.getBooleanExtra("callClassifiedApi", false)
            val callEventApi = data?.getBooleanExtra("callEventApi", false)

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
            }
        }
    }
}