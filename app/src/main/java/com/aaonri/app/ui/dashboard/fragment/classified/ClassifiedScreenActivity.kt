package com.aaonri.app.ui.dashboard.fragment.classified

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aaonri.app.databinding.ActivityClassifiedScreenBinding

class ClassifiedScreenActivity : AppCompatActivity() {
    var classifiedScreenBinding: ActivityClassifiedScreenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classifiedScreenBinding = ActivityClassifiedScreenBinding.inflate(layoutInflater)
        setContentView(classifiedScreenBinding?.root)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }
}