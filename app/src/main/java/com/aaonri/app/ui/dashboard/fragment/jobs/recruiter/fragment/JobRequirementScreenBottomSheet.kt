package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaonri.app.databinding.FragmentJobRequirementScreenBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobRequirementScreenBottomSheet : BottomSheetDialogFragment() {
    var binding: FragmentJobRequirementScreenBottomSheetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobRequirementScreenBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding?.apply {

        }

        return binding?.root
    }
}