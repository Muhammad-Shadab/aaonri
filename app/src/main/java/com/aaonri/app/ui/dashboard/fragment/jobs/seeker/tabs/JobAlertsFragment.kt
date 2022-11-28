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
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobAlertsBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.JobScreenFragmentDirections
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.AlertAdapter
import com.aaonri.app.utils.CustomDialog
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobAlertsFragment : Fragment() {
    var binding: FragmentJobAlertsBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var alertAdapter: AlertAdapter? = null
    var jobProfileId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobAlertsBinding.inflate(layoutInflater, container, false)

        alertAdapter = AlertAdapter { isUpdateBtnClicked, isDeleteBtnClicked, value ->
            if (isUpdateBtnClicked) {
                jobSeekerViewModel.setNavigateToUpdateJobAlert(value)
            } else if (isDeleteBtnClicked) {
                //delete job alert
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure you want to delete this job alert?")
                builder.setPositiveButton("Delete") { dialog, which ->
                    jobSeekerViewModel.deleteJobAlert(value.id)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->

                }
                builder.show()
            }
        }

        binding?.apply {

            recyclerViewJobAlert.layoutManager = LinearLayoutManager(context)
            recyclerViewJobAlert.adapter = alertAdapter

            createJobAlertBtn.setOnClickListener {
                navigateToJobApplyFragment()
            }

            jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        response.data?.let {
                            if (it.jobProfile.isNotEmpty()) {
                                jobProfileId = it.jobProfile[0].id
                                jobSeekerViewModel.getJobAlertsByJobProfileId(it.jobProfile[0].id)
                            } else {
                                resultsNotFoundLL.visibility = View.VISIBLE
                            }
                        }
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                    }
                }
            }

            jobSeekerViewModel.userJobAlertData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        response.data?.let {
                            if (it.jobAlerts != null) {
                                if (it.jobAlerts.isNotEmpty()) {
                                    alertAdapter?.setData(it.jobAlerts)
                                    resultsNotFoundLL.visibility = View.GONE
                                    nestedScrollView.visibility = View.VISIBLE
                                } else {
                                    resultsNotFoundLL.visibility = View.VISIBLE
                                    nestedScrollView.visibility = View.GONE
                                }
                            } else {
                                resultsNotFoundLL.visibility = View.VISIBLE
                                nestedScrollView.visibility = View.GONE
                            }
                        }
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                    }
                }
            }

            jobSeekerViewModel.deleteJobAlertData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        response.data?.let {
                            activity?.let { it1 ->
                                Snackbar.make(
                                    it1.findViewById(android.R.id.content),
                                    it.message, Snackbar.LENGTH_LONG
                                ).show()
                            }
                            jobSeekerViewModel.getJobAlertsByJobProfileId(jobProfileId)
                        }
                        jobSeekerViewModel.deleteJobAlertData.postValue(null)
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                    }
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
                        if (it.jobProfile.isNotEmpty()) {
                            jobSeekerViewModel.setNavigateToCreateJobAlert(true)
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