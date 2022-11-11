package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.data.jobs.recruiter.JobRecruiterStaticData
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterJobDetailsBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.RecruiterJobKeySkillsAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job.RecruiterPostJobActivity
import com.aaonri.app.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class RecruiterJobDetailsFragment : Fragment() {
    var binding: FragmentRecruiterJobDetailsBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    val args: RecruiterJobDetailsFragmentArgs by navArgs()
    var recruiterJobKeySkillsAdapter: RecruiterJobKeySkillsAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecruiterJobDetailsBinding.inflate(inflater, container, false)

        jobRecruiterViewModel.findJobDetailsById(args.jobId)

        recruiterJobKeySkillsAdapter = RecruiterJobKeySkillsAdapter()

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data?.getBooleanExtra("updateJobData", false)
                    if (data == true) {
                        jobRecruiterViewModel.findJobDetailsById(args.jobId)
                    }
                }
            }

        binding?.apply {

            skillsRv.layoutManager = FlexboxLayoutManager(context)
            skillsRv.adapter = recruiterJobKeySkillsAdapter

            activateJobBtn.setOnClickListener {
                jobRecruiterViewModel.changeJobActiveStatus(args.jobId, true)
            }

            deactivateBtn.setOnClickListener {
                jobRecruiterViewModel.changeJobActiveStatus(args.jobId, false)
            }

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            jobApplicantLl.setOnClickListener {
                val action =
                    RecruiterJobDetailsFragmentDirections.actionRecruiterJobDetailsFragmentToRecruiterJobApplicantsFragment(
                        it.id
                    )
                findNavController().navigate(action)
            }

            editJobIv.setOnClickListener {
                val intent = Intent(context, RecruiterPostJobActivity::class.java)
                intent.putExtra("isUpdateJob", true)
                resultLauncher.launch(intent)
            }

            jobRecruiterViewModel.jobDetailsByIdData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        response.data?.let {

                            JobRecruiterStaticData.setJobDetailsData(it)

                            jobTitle.text = it.title
                            companyNameTv.text = it.company
                            addressTv.text = it.state + ", " + it.country
                            salaryTv.text = it.salaryRange
                            experienceTv.text = it.experienceLevel
                            jobCategoriesTv.text = it.jobType
                            jobDescTv.text = Html.fromHtml(it.description)
                            dateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                                .format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        .parse(it.createdOn.split("T")[0])
                                )
                            recruiterJobKeySkillsAdapter?.setData(
                                it.skillSet.split(",").toTypedArray().toList()
                            )
                            if (it.applicability.isNotEmpty()) {
                                jobRequirementTv.text = it.applicability.toString()
                            }
                            jobViewCountTv.text = it.viewCount.toString()
                            jobApplicantCountTv.text = it.applyCount.toString()

                            if (it.isActive) {
                                deactivateBtn.visibility = View.VISIBLE
                                activateJobBtn.visibility = View.GONE
                                inactiveTv.visibility = View.GONE
                            } else {
                                activateJobBtn.visibility = View.VISIBLE
                                deactivateBtn.visibility = View.GONE
                                inactiveTv.visibility = View.VISIBLE
                            }

                            linearLayout.visibility = View.VISIBLE
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

                            jobRecruiterViewModel.findJobDetailsById(args.jobId)

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

    override fun onDestroy() {
        super.onDestroy()
        jobRecruiterViewModel.jobDetailsByIdData.postValue(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}