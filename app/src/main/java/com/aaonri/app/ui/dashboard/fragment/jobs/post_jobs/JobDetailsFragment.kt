package com.aaonri.app.ui.dashboard.fragment.jobs.post_jobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentJobDetailsBinding

class JobDetailsFragment : Fragment() {
    var jobDetailsBinding : FragmentJobDetailsBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        jobDetailsBinding = FragmentJobDetailsBinding.inflate(inflater, container, false)
        return jobDetailsBinding?.root
    }


}