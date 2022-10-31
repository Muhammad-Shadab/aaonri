package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaonri.app.databinding.FragmentRecruiterJobApplicantsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterJobApplicantsFragment : Fragment() {
    var binding: FragmentRecruiterJobApplicantsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecruiterJobApplicantsBinding.inflate(inflater, container, false)
        return binding?.root
    }

}