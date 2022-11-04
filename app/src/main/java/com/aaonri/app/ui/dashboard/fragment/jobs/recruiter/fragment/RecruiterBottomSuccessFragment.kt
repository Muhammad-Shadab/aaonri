package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterBottomSuccessBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterBottomSuccessFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentRecruiterBottomSuccessBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRecruiterBottomSuccessBinding.inflate(inflater, container, false)

        jobRecruiterViewModel.stepViewLastTick.postValue(true)

        binding?.apply {

            if (jobRecruiterViewModel.isUpdateJob) {
                successfulTv.text = "You have successfully updated a Job"
            } else {
                successfulTv.text = "You have successfully posted a Job"
            }


            viewYourAdvertiseBtn.setOnClickListener {
                val data = Intent()
                data.putExtra("updateJobData", true)
                activity?.setResult(AppCompatActivity.RESULT_OK, data)
                activity?.finish()
            }

        }

        return binding?.root
    }


}