package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.jobs.recruiter.JobRecruiterStaticData
import com.aaonri.app.data.jobs.recruiter.model.JobSearchRequest
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterMyPostedJobBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.MyPostedJobAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job.RecruiterPostJobActivity
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterMyPostedJobFragment : Fragment() {
    var binding: FragmentRecruiterMyPostedJobBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var myPostedJobAdapter: MyPostedJobAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRecruiterMyPostedJobBinding.inflate(inflater, container, false)

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data?.getBooleanExtra("updateJobData", false)
                    if (data == true) {
                        jobRecruiterViewModel.getMyPostedJobs(
                            JobSearchRequest(
                                city = "",
                                company = "",
                                createdByMe = true,
                                experience = "",
                                industry = "",
                                jobType = "",
                                keyWord = "",
                                skill = "",
                                userEmail = "$email"
                            )
                        )
                    }
                }
            }

        myPostedJobAdapter =
            MyPostedJobAdapter { isEditBtnClicked, isActivateBtnClicked, isDeactivateBtnClicked, isJobApplicantClicked, isJobCardClicked, value ->
                if (isEditBtnClicked) {

                    JobRecruiterStaticData.setJobDetailsData(value)
                    val intent = Intent(context, RecruiterPostJobActivity::class.java)
                    intent.putExtra("isUpdateJob", true)
                    resultLauncher.launch(intent)

                } else if (isActivateBtnClicked) {
                    jobRecruiterViewModel.changeJobActiveStatus(value.jobId, true)
                } else if (isDeactivateBtnClicked) {
                    jobRecruiterViewModel.changeJobActiveStatus(value.jobId, false)
                } else if (isJobApplicantClicked) {
                    jobRecruiterViewModel.setNavigateFromMyPostedJobToJobApplicantScreen(value.jobId)
                } else if (isJobCardClicked) {
                    jobRecruiterViewModel.setNavigateFromMyPostedJobToJobDetailsScreen(value.jobId)
                }
            }

        binding?.apply {

            recyclerViewMyPostedJob.layoutManager = LinearLayoutManager(context)
            recyclerViewMyPostedJob.adapter = myPostedJobAdapter

            jobRecruiterViewModel.myPostedJobsData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        if (response.data?.jobDetailsList?.isNotEmpty() == true) {
                            emptyScreenImage.visibility = View.GONE
                            emptyTextVew.visibility = View.GONE
                            nestedScrollView.visibility = View.VISIBLE
                            response.data.jobDetailsList.let { myPostedJobAdapter?.setData(it) }
                        } else {
                            emptyScreenImage.visibility = View.VISIBLE
                            emptyTextVew.visibility = View.VISIBLE
                            nestedScrollView.visibility = View.GONE
                        }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            jobRecruiterViewModel.changeJobStatusData.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            /** calling api for my posted job screen **/
                            jobRecruiterViewModel.getMyPostedJobs(
                                JobSearchRequest(
                                    city = "",
                                    company = "",
                                    createdByMe = true,
                                    experience = "",
                                    industry = "",
                                    jobType = "",
                                    keyWord = "",
                                    skill = "",
                                    userEmail = "$email"
                                )
                            )
                            jobRecruiterViewModel.changeJobStatusData.postValue(null)
                        }
                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }


        return binding?.root
    }


}