package com.aaonri.app.ui.dashboard.fragment.event

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.ActivityEventScreenBinding

class EventScreenActivity : BaseActivity() {
    var eventActivityBinding: ActivityEventScreenBinding? = null
    val postEventViewModel: PostEventViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventActivityBinding = ActivityEventScreenBinding.inflate(layoutInflater)
        setContentView(eventActivityBinding?.root)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        val isUpdateEvent = intent.getBooleanExtra("updateEvent", false)
        val updateEventId = intent.getIntExtra("eventId", 0)

        postEventViewModel.setIsUpdateEvent(isUpdateEvent)
        postEventViewModel.setUpdateEventId(updateEventId)

        if (isUpdateEvent) {
            postEventViewModel.getEventDetails(updateEventId)
            eventActivityBinding?.registrationText?.text = "Update Your Event"
        }


        eventActivityBinding?.apply {

            navigateBack.setOnClickListener {
                onBackPressed()
            }

            postEventViewModel.navigationForStepper.observe(this@EventScreenActivity) { route ->
                when (route) {
                    EventConstants.EVENT_BASIC_DETAILS -> {
                        stepView.go(0, true)
                    }
                    EventConstants.EVENT_UPLOAD_PICS -> {
                        stepView.go(1, true)
                    }
                    EventConstants.EVENT_ADDRESS_DETAILS -> {
                        stepView.go(2, true)
                    }
                }
            }
            postEventViewModel.stepViewLastTick.observe(this@EventScreenActivity) {
                stepView.done(it)
            }
        }

    }
}