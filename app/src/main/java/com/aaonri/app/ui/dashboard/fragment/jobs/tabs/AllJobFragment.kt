package com.aaonri.app.ui.dashboard.fragment.jobs.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.jobs.seeker.model.AllJobsResponseItem
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentAllJobBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.adapter.JobAdapter
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllJobFragment : Fragment() {
    var binding: FragmentAllJobBinding? = null
    var jobAdapter: JobAdapter? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllJobBinding.inflate(inflater, container, false)
        jobAdapter = JobAdapter()

        jobAdapter?.itemClickListener = { view, item, position ->
            if (item is AllJobsResponseItem) {
                jobSeekerViewModel.setNavigateAllJobToDetailsJobScreen(item.jobId)
            }
        }

        binding?.apply {

            recyclerViewAllJob.layoutManager = LinearLayoutManager(context)
            recyclerViewAllJob.adapter = jobAdapter
        }

        jobSeekerViewModel.getAllActiveJobs()

        jobSeekerViewModel.allActiveJobsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { jobAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }

        }


        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}