package com.aaonri.app.ui.dashboard.fragment.event

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityEventScreenBinding

class EventScreenActivity : BaseActivity() {
    var eventActivityBinding: ActivityEventScreenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventActivityBinding = ActivityEventScreenBinding.inflate(layoutInflater)
        setContentView(eventActivityBinding?.root)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }
}