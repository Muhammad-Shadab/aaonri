package com.aaonri.app.ui.dashboard.fragment.advertise

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.aaonri.app.R
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.ActivityAdvertiseScreenBinding
import com.aaonri.app.utils.PreferenceManager

class AdvertiseScreenActivity : BaseActivity() {
    var binding: ActivityAdvertiseScreenBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertiseScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.postAdvertiseNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        val isRenewAdvertise = intent.getBooleanExtra("isRenewAdvertise", false)
        val isUpdateAdvertise = intent.getBooleanExtra("isUpdateAdvertise", false)
        val advertiseId = intent.getIntExtra("advertiseId", 0)

        postAdvertiseViewModel.setIsUpdateOrRenewAdvertise(
            renewAdvertise = isRenewAdvertise,
            updateAdvertise = isUpdateAdvertise
        )

        postAdvertiseViewModel.setAdvertiseId(advertiseId)

        postAdvertiseViewModel.getAllActiveAdvertisePage()
        postAdvertiseViewModel.getActiveTemplateForSpinner()
        onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController.currentDestination?.id == R.id.postAdvertiseCheckout) {
                        if (postAdvertiseViewModel.isRenewAdvertise) {
                            finish()
                        } else {
                            navController.navigateUp()
                        }
                    } else if (navController.currentDestination?.id == R.id.postAdvertiseTermConditionFragment2) {
                        finish()
                    } else if (navController.currentDestination?.id == R.id.postAdvertiseCompanyDetailsFrgament) {
                        if (isUpdateAdvertise) {
                            finish()
                        } else {
                            navController.navigateUp()
                        }
                    } else {
                        navController.navigateUp()
                    }
                }
            })


        binding?.apply {

            if (isUpdateAdvertise) {
                registrationText.text = "Update Advertisement"
            }

            navigateBack.setOnClickListener {
                onBackPressed()
            }

            postAdvertiseViewModel.navigationForStepper.observe(this@AdvertiseScreenActivity) { route ->
                when (route) {
                    AdvertiseConstant.ADVERTISE_TEMPLATE -> {
                        stepView.visibility = View.VISIBLE
                        stepView.go(0, true)
                    }
                    AdvertiseConstant.ADVERTISE_TEMPLATE_LOCATION -> {
                        stepView.visibility = View.VISIBLE
                        stepView.go(1, true)
                    }
                    AdvertiseConstant.ADVERTISE_BASIC_DETAILS -> {
                        stepView.visibility = View.VISIBLE
                        stepView.go(2, true)
                    }
                    AdvertiseConstant.ADVERTISE_CHECKOUT -> {
                        stepView.visibility = View.VISIBLE
                        stepView.go(3, true)
                    }
                    AdvertiseConstant.ADVERTISE_TERMS_AND_CONDITION -> {
                        stepView.visibility = View.GONE
                    }
                    AdvertiseConstant.ADVERTISE_COMPANY_DETAILS -> {
                        stepView.visibility = View.GONE
                    }
                }
            }

            postAdvertiseViewModel.stepViewLastTick.observe(this@AdvertiseScreenActivity) {
                stepView.done(it)
            }
        }

        postAdvertiseViewModel.selectedTemplatePageName.observe(this) { advertiseActivePage ->
            advertiseActivePage.pageId.let {
                postAdvertiseViewModel.getAdvertisePageLocationById(
                    it
                )
            }
        }

        postAdvertiseViewModel.selectedTemplateLocation.observe(this) { advertiseTemplateLocation ->
            advertiseTemplateLocation.locationCode.let {
                postAdvertiseViewModel.getAdvertiseActiveVas(
                    it
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext?.let { it1 -> PreferenceManager<Int>(it1) }
            ?.set("selectedTemplatePage", -1)

        applicationContext?.let { it1 -> PreferenceManager<Int>(it1) }
            ?.set("selectedTemplateLocation", -1)

        applicationContext?.let { it1 -> PreferenceManager<Int>(it1) }
            ?.set("selectedTemplateSpinnerItem", 0)

    }

}

