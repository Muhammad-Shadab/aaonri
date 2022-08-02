package com.aaonri.app.ui.dashboard.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentJobScreenBinding


class JobScreenFragment : Fragment() {
 var jobBinding : FragmentJobScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        jobBinding = FragmentJobScreenBinding.inflate(inflater, container, false)
        return jobBinding?.root
    }

}