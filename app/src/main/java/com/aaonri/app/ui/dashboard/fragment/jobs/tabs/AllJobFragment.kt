package com.aaonri.app.ui.dashboard.fragment.jobs.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentAllJobBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.adapter.JobAdapter

class AllJobFragment : Fragment() {
    var allJobBinding:FragmentAllJobBinding? = null
    var jobAdapter: JobAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        allJobBinding =  FragmentAllJobBinding.inflate(inflater, container, false)
        jobAdapter = JobAdapter {}
        allJobBinding?.apply {
            jobAdapter?.setData(listOf("job 1", "job 2", "job 3", "job 4"))
            recyclerViewAllJob.layoutManager = LinearLayoutManager(context)
            recyclerViewAllJob.adapter = jobAdapter
        }
        return allJobBinding?.root
    }

}