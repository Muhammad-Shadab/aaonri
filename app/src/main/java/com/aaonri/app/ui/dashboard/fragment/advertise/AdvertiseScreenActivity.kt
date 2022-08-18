package com.aaonri.app.ui.dashboard.fragment.advertise

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.ActivityAdvertiseScreenBinding
import com.aaonri.app.utils.PreferenceManager

class AdvertiseScreenActivity : BaseActivity() {
    var binding: ActivityAdvertiseScreenBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertiseScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        val isRenewAdvertise = intent.getBooleanExtra("isRenewAdvertise", false)
        val isUpdateAdvertise = intent.getBooleanExtra("isUpdateAdvertise", false)

        postAdvertiseViewModel.setIsUpdateOrRenewAdvertise(
            renewAdvertise = isRenewAdvertise,
            updateAdvertise = isUpdateAdvertise
        )

        postAdvertiseViewModel.getAllActiveAdvertisePage()
        postAdvertiseViewModel.getActiveTemplateForSpinner()

        binding?.apply {

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
    }

}

