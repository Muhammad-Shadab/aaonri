package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs

import android.app.AlertDialog
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
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.JobScreenFragmentDirections
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobSeekerAdapter
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllJobFragment : Fragment() {
    var binding: FragmentAllJobBinding? = null
    var jobAdapter: JobSeekerAdapter? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var selectedJobItem: AllJobsResponseItem? = null

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
                    selectedJobItem = item
                    /** This function will navigate user to job apply fragment screen if user already uploaded job seeker profile otherwise it will navigate to upload job seeker profile screen**/
                    navigateToJobApplyFragment()
                }
            }
        }

        binding?.apply {

            recyclerViewAllJob.layoutManager = LinearLayoutManager(context)
            recyclerViewAllJob.adapter = jobAdapter
        }

        jobSeekerViewModel.allActiveJobsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { jobAdapter?.setData(it.subList(0, 4)) }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }

        }

        return binding?.root
    }

    private fun navigateToJobApplyFragment() {
        jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    response.data?.let {
                        if (it.size > 0) {
                            val action =
                                selectedJobItem?.jobId?.let { it1 ->
                                    JobScreenFragmentDirections.actionJobScreenFragmentToJobApplyFragment(
                                        it1
                                    )
                                }
                            if (action != null) {
                                findNavController().navigate(action)
                            } else {

                            }
                        } else {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Confirm")
                            builder.setMessage("Seems you haven't uploaded your job profile yet.")
                            builder.setPositiveButton("Upload Profile") { dialog, which ->
                                val action =
                                    JobScreenFragmentDirections.actionJobScreenFragmentToJobProfileUploadFragment(
                                        false,
                                        0
                                    )
                                findNavController().navigate(action)
                            }
                            builder.setNegativeButton("Cancel") { dialog, which ->

                            }
                            builder.show()
                        }
                    }
                }
                is Resource.Error -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}