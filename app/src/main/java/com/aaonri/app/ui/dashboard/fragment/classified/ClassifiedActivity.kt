package com.aaonri.app.ui.dashboard.fragment.classified

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.ClassifiedStaticData
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.ActivityClassifiedScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifiedActivity : AppCompatActivity() {
    var binding: ActivityClassifiedScreenBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by viewModels()
    var title: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassifiedScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val isUpdateClassified = intent.getBooleanExtra("updateClassified", false)
        val updateClassifiedId = intent.getIntExtra("addId", 0)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        postClassifiedViewModel.setIsUpdateClassified(isUpdateClassified)
        postClassifiedViewModel.setUpdateClassifiedId(updateClassifiedId)

        if (isUpdateClassified) {
            binding?.registrationText?.text = "Update Classified"
        } else {
            if (ClassifiedStaticData.getCategoryList().isEmpty()) {
                postClassifiedViewModel.getClassifiedCategory()
            }
        }

        binding?.apply {

            navigateBack.setOnClickListener {
                onBackPressed()
            }

            postClassifiedViewModel.navigationForStepper.observe(this@ClassifiedActivity) { route ->
                when (route) {
                    ClassifiedConstant.BASIC_DETAILS_SCREEN -> {
                        stepView.go(0, true)
                    }
                    ClassifiedConstant.UPLOAD_PIC_SCREEN -> {
                        stepView.go(1, true)
                    }
                    ClassifiedConstant.ADDRESS_DETAILS_SCREEN -> {
                        stepView.go(2, true)
                    }
                }
            }
            postClassifiedViewModel.stepViewLastTick.observe(this@ClassifiedActivity) {
                stepView.done(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}