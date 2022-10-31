package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityRecruiterPostJobBinding


class RecruiterPostJobActivity : BaseActivity() {
    var binding: ActivityRecruiterPostJobBinding? = null

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

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}