package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobAlertsBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.AlertAdapter
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobAlertsFragment : Fragment() {
    var binding: FragmentJobAlertsBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var alertAdapter: AlertAdapter? = null
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
            }
        }

        binding?.apply {

            recyclerViewJobAlert.layoutManager = LinearLayoutManager(context)
            recyclerViewJobAlert.adapter = alertAdapter

            createJobAlertBtn.setOnClickListener {
                jobSeekerViewModel.setNavigateToCreateJobAlert(true)
            }

            jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        response.data?.let {
                            if (it.jobProfile.isNotEmpty()) {
                                jobSeekerViewModel.getJobAlertsByJobProfileId(it.jobProfile[0].id)
                            }
                        }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            jobSeekerViewModel.userJobAlertData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        response.data?.let {
                            if (it.jobAlerts.isNotEmpty()) {
                                alertAdapter?.setData(it.jobAlerts)
                                resultsNotFoundLL.visibility = View.GONE
                                nestedScrollView.visibility = View.VISIBLE
                            } else {
                                resultsNotFoundLL.visibility = View.VISIBLE
                                nestedScrollView.visibility = View.GONE
                            }
                        }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

        }

        return binding?.root
    }
}