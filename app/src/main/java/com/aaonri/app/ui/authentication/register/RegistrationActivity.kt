package com.aaonri.app.ui.authentication.register

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aaonri.app.R
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.databinding.ActivityRegistrationBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : BaseActivity() {
    var registrationBinding: ActivityRegistrationBinding? = null
    val authCommonViewModel: AuthCommonViewModel by viewModels()
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

        val newUserRegister = intent.getBooleanExtra("newUserRegister", false)

        authCommonViewModel.setIsNewUserRegisterUsingGmail(newUserRegister)

        val firstName =
            applicationContext?.let { PreferenceManager<String>(it)[Constant.GMAIL_FIRST_NAME, ""] }

        val lastName =
            applicationContext?.let { PreferenceManager<String>(it)[Constant.GMAIL_LAST_NAME, ""] }

        val userEmail =
            applicationContext?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        if (newUserRegister) {
            if (firstName != null) {
                lastName?.let {
                    userEmail?.let { it1 ->
                        authCommonViewModel.addBasicDetails(
                            firstName = firstName,
                            lastName = it,
                            emailAddress = it1,
                            password = "987654321"
                        )
                    }
                }
            }
        }

        registrationBinding?.apply {

            navigateBack.setOnClickListener {
                onBackPressed()
            }

            authCommonViewModel.navigationForStepper.observe(this@RegistrationActivity) { route ->
                when (route) {
                    AuthConstant.BASIC_DETAILS_SCREEN -> {
                        introSubText.text = "Please register to get started & explore more.."
                        stepView.go(0, true)
                    }
                    AuthConstant.ADDRESS_DETAILS_SCREEN -> {
                        introSubText.text = "We need few more details"
                        stepView.go(1, true)
                    }
                    AuthConstant.LOCATION_DETAILS_SCREEN -> {
                        introSubText.text = "Please tell us about your place of origin.."
                        stepView.go(2, true)
                    }
                    AuthConstant.SERVICE_DETAILS_SCREEN -> {
                        introSubText.text = "What modules would you like to explore \n on aaonri ?"
                        stepView.go(3, true)
                    }
                }
            }
            authCommonViewModel.stepViewLastTick.observe(this@RegistrationActivity) {
                stepView.done(it)
            }
        }
    }
}