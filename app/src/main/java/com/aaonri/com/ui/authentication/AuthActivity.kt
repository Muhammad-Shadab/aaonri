package com.aaonri.com.ui.authentication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.aaonri.com.base.BaseActivity
import com.aaonri.com.databinding.ActivityAuthBinding


class AuthActivity : BaseActivity() {
    var authBinding: ActivityAuthBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(authBinding?.root)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

    }
}