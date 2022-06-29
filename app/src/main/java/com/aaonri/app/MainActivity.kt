package com.aaonri.app

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    var mainActivityBinding: ActivityMainBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by viewModels()

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

        mainActivityBinding?.apply {

            bottomNavigation.setupWithNavController(navController)

            //

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.classifiedDetailsFragment || destination.id == R.id.eventScreenFragment) {
                    bottomNavigation.visibility = View.GONE
                } else {
                    bottomNavigation.visibility = View.VISIBLE
                }
            }
        }

        dashboardCommonViewModel.isSeeAllClassifiedClicked.observe(this) {
            if (it) {
                mainActivityBinding?.bottomNavigation?.selectedItemId =
                    R.id.classifiedScreenFragment
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(false)
            }
        }

    }

}