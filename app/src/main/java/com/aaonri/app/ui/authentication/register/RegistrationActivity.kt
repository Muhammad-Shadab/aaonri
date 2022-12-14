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
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.ActivityRegistrationBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : BaseActivity() {
    var binding: ActivityRegistrationBinding? = null
    val authCommonViewModel: AuthCommonViewModel by viewModels()
    val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

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
        registrationViewModel.getServices()


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

        binding?.apply {

            navigateBack.setOnClickListener {
                onBackPressed()
            }

            authCommonViewModel.navigationForStepper.observe(this@RegistrationActivity) { route ->
                when (route) {
                    AuthConstant.BASIC_DETAILS_SCREEN -> {
                        introSubText.text = "Connect to your community in few simple steps."
                        stepView.go(0, true)
                    }
                    AuthConstant.ADDRESS_DETAILS_SCREEN -> {
                        introSubText.text =
                            "Dear ${authCommonViewModel.basicDetailsMap[AuthConstant.FIRST_NAME]}, Where are you based?"
                        stepView.go(1, true)
                    }
                    AuthConstant.LOCATION_DETAILS_SCREEN -> {
                        introSubText.text =
                            "Dear ${authCommonViewModel.basicDetailsMap[AuthConstant.FIRST_NAME]}, Tell us about your ethnicity"
                        stepView.go(2, true)
                    }
                    AuthConstant.SERVICE_DETAILS_SCREEN -> {
                        introSubText.text =
                            "Dear ${authCommonViewModel.basicDetailsMap[AuthConstant.FIRST_NAME]}, We are almost done.\n" +
                                    "Select the services you are looking for"
                        stepView.go(3, true)
                    }
                }
            }
            authCommonViewModel.stepViewLastTick.observe(this@RegistrationActivity) {
                stepView.done(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

        applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(Constant.GMAIL_FIRST_NAME, "")
        applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(Constant.USER_PROFILE_PIC, "")
        applicationContext?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(Constant.GMAIL_LAST_NAME, "")

    }
}