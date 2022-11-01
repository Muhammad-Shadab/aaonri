package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.jobs.recruiter.JobRecruiterConstant
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.ActivityRecruiterPostJobBinding


class RecruiterPostJobActivity : BaseActivity() {
    var binding: ActivityRecruiterPostJobBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterPostJobBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        binding?.apply {

            navigateBack.setOnClickListener {
                onBackPressed()
            }

            jobRecruiterViewModel.navigationForStepper.observe(this@RecruiterPostJobActivity) { route ->
                when (route) {
                    JobRecruiterConstant.RECRUITER_POST_JOB_DETAILS_SCREEN -> {
                        stepView.go(0, true)
                    }
                    JobRecruiterConstant.RECRUITER_POST_JOB_REQUIREMENT_DETAILS_SCREEN -> {
                        stepView.go(1, true)
                    }
                }
            }

            jobRecruiterViewModel.stepViewLastTick.observe(this@RecruiterPostJobActivity) {
                stepView.done(it)
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}