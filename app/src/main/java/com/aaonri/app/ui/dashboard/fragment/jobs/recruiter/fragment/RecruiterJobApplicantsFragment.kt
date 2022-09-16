package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentRecruiterJobApplicantsBinding

class RecruiterJobApplicantsFragment : Fragment() {
  var binding:FragmentRecruiterJobApplicantsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentRecruiterJobApplicantsBinding.inflate(inflater, container, false)
        return binding?.root
    }

}