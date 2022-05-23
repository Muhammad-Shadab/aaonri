package com.aaonri.app.ui.authentication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.aaonri.app.R
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityAuthBinding


class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

    }
}