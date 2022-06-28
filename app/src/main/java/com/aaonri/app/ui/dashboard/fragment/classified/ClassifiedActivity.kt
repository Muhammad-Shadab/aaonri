package com.aaonri.app.ui.dashboard.fragment.classified

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.ActivityClassifiedScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifiedActivity : AppCompatActivity() {
    var classifiedScreenBinding: ActivityClassifiedScreenBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classifiedScreenBinding = ActivityClassifiedScreenBinding.inflate(layoutInflater)
        setContentView(classifiedScreenBinding?.root)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        classifiedScreenBinding?.apply {

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
}