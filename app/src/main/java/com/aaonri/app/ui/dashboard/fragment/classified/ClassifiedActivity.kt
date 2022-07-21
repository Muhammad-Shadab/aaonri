package com.aaonri.app.ui.dashboard.fragment.classified

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.ActivityClassifiedScreenBinding
import com.aaonri.app.utils.ClassifiedCategoriesList
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifiedActivity : AppCompatActivity() {
    var classifiedScreenBinding: ActivityClassifiedScreenBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by viewModels()
    var title: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classifiedScreenBinding = ActivityClassifiedScreenBinding.inflate(layoutInflater)
        setContentView(classifiedScreenBinding?.root)

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
            classifiedScreenBinding?.registrationText?.text = "Update Your Classified"
        } else {
            if (ClassifiedCategoriesList.getCategoryList().isEmpty()) {
                postClassifiedViewModel.getClassifiedCategory()
            }
        }

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