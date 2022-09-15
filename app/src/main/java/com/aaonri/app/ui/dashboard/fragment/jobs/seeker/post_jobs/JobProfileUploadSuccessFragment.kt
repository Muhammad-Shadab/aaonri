package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.post_jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class JobProfileUploadSuccessFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_profile_upload_success, container, false)
    }

}