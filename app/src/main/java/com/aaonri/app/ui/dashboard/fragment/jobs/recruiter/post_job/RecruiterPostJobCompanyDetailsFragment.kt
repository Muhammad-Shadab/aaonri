package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentRecruiterPostJobCompanyDetailsBinding

class RecruiterPostJobCompanyDetailsFragment : Fragment() {
     var binding:FragmentRecruiterPostJobCompanyDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecruiterPostJobCompanyDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }

}