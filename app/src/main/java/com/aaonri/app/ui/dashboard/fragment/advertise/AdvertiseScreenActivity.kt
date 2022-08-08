package com.aaonri.app.ui.dashboard.fragment.advertise

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityAdvertiseScreenBinding

class AdvertiseScreenActivity : BaseActivity() {
    var binding: ActivityAdvertiseScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertiseScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        binding?.apply{

            navigateBack.setOnClickListener {
                onBackPressed()
            }

        }

    }
}