package com.aaonri.app.ui.dashboard.fragment.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aaonri.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_screen)
    }
}