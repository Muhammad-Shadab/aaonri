package com.aaonri.app.ui.dashboard.fragment.jobs.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.data.jobs.seeker.model.SaveJobViewRequest
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobDetailsFragment : Fragment() {
    var binding: FragmentJobDetailsBinding? = null
    val args: JobDetailsFragmentArgs by navArgs()
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var jobId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val userId =
            context?.let { PreferenceManager<Int>(it)[Constant.USER_ID, 0] }

        binding?.apply {

            jobDescTv.textSize = 14F

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            jobSeekerViewModel.getJobDetails(args.jobId)
            jobSeekerViewModel.saveJobView(
                SaveJobViewRequest(
                    id = userId ?: 0,
                    jobId = args.jobId,
                    isApplied = false,
                    lastViewedOn = "",
                    noOfTimesViewed = 0,
                    userId = email ?: "",
                    viewerName = "",
                )
            )

            applyBtn.setOnClickListener {
                jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Resource.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            response.data?.let {
                                if (it.size > 0) {
                                    /** Profile Uploaded **/
                                    val action =
                                        JobDetailsFragmentDirections.actionJobDetailsFragmentToJobApplyFragment(
                                            jobId
                                        )
                                    findNavController().navigate(action)
                                } else {

                                }
                            }

                        }
                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }

            jobSeekerViewModel.jobDetailData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        var applicability = ""
                        progressBar.visibility = View.GONE
                        response.data?.let {
                            jobId = it.jobId
                            jobNameTv.text = it.title
                            companyNameTv.text = it.company
                            experienceTv.text = it.experienceLevel
                            addressTv.text =
                                "${it.country}, ${it.state}, ${it.city}"
                            moneyTv.text = it.salaryRange
                            jobCategoriesTv.text = it.jobType
                            dateTv.text = it.createdOn
                            jobViewTv.text = it.viewCount.toString()
                            jobApplicationTv.text = it.applyCount.toString()
                            jobDescTv.fromHtml(it.description)
                            jobKeySkillsTv.text = it.skillSet
                            it.applicability.forEach { apl ->
                                applicability += "${apl.applicability}, "
                            }
                            jobRequirementTv.text = applicability
                        }
                        linearLayout.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

        }


        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        jobSeekerViewModel.jobDetailData.postValue(null)
    }


}