package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.databinding.FragmentRecruiterUpdateProfileBinding


class RecruiterUpdateProfileFragment : Fragment() {
    var binding:FragmentRecruiterUpdateProfileBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecruiterUpdateProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

}