package com.aaonri.app.ui.dashboard.fragment.jobs.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.model.AllJobsResponseItem
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentAllJobBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.JobScreenFragmentDirections
import com.aaonri.app.ui.dashboard.fragment.jobs.adapter.JobSeekerAdapter
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllJobFragment : Fragment() {
    var binding: FragmentAllJobBinding? = null
    var jobAdapter: JobSeekerAdapter? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllJobBinding.inflate(inflater, container, false)
        jobAdapter = JobSeekerAdapter()

        jobAdapter?.itemClickListener = { view, item, position ->

            if (item is AllJobsResponseItem) {
                if (view.id == R.id.jobCv) {
                    /** Clicked on Job Card View **/
                    jobSeekerViewModel.setNavigateAllJobToDetailsJobScreen(item.jobId)
                } else {
                    /** Clicked on Apply btn **/
                    val action =
                        JobScreenFragmentDirections.actionJobScreenFragmentToJobApplyFragment(item.jobId)
                    findNavController().navigate(action)
                }
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