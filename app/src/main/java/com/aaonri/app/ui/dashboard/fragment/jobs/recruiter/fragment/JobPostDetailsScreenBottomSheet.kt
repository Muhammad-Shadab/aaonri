package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaonri.app.databinding.FragmentJobPostDetailsScreenBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobPostDetailsScreenBottomSheet : BottomSheetDialogFragment() {
    var binding: FragmentJobPostDetailsScreenBottomSheetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentJobPostDetailsScreenBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding?.apply {

        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}