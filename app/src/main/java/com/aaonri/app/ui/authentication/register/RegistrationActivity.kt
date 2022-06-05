package com.aaonri.app.ui.authentication.register

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.ActivityRegistrationBinding
import com.aaonri.app.util.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {
    var registrationBinding: ActivityRegistrationBinding? = null
    val commonViewModel: CommonViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(registrationBinding?.root)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.onBoardingNavHost) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        registrationBinding?.apply {

            navigateBack.setOnClickListener {
                onBackPressed()
            }

            commonViewModel.navigationForStepper.observe(this@RegistrationActivity) { route ->
                when (route) {
                    Constant.BASIC_DETAILS_SCREEN -> {
                        stepView.go(0, true)
                    }
                    Constant.ADDRESS_DETAILS_SCREEN -> {
                        stepView.go(1, true)
                    }
                    Constant.LOCATION_DETAILS_SCREEN -> {
                        stepView.go(2, true)
                    }
                    Constant.SERVICE_DETAILS_SCREEN -> {
                        stepView.go(3, true)
                    }
                }
            }
        }
    }
}