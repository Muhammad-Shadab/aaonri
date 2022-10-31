package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaonri.app.databinding.FragmentRecruiterAllTalentsFragmetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterAllTalentsFragment : Fragment() {

    var binding: FragmentRecruiterAllTalentsFragmetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecruiterAllTalentsFragmetBinding.inflate(inflater, container, false)


        binding?.apply {

        }



        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}