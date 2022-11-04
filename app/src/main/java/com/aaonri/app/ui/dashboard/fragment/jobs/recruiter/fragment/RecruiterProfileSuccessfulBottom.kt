package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterProfileSuccessfulBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterProfileSuccessfulBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentRecruiterProfileSuccessfulBottomBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRecruiterProfileSuccessfulBottomBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding?.apply {

            if (jobRecruiterViewModel.isUpdateConsultantProfile) {
                successfulTv.text = "You have successfully updated your consultant profile."
            } else {
                successfulTv.text = "You have successfully uploaded your consultant profile."
            }

            viewYourAdvertiseBtn.setOnClickListener {
                val action =
                    RecruiterProfileSuccessfulBottomDirections.actionRecruiterProfileSuccessfulBottom2ToJobRecruiterScreenFragment()
                findNavController().navigate(action)
            }


        }

        return binding?.root
    }
}