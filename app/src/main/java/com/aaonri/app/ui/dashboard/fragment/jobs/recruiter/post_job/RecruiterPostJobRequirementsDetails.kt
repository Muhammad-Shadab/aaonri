package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.jobs.recruiter.JobRecruiterConstant
import com.aaonri.app.data.jobs.recruiter.JobRecruiterStaticData
import com.aaonri.app.data.jobs.recruiter.model.AllActiveJobApplicabilityResponseItem
import com.aaonri.app.data.jobs.recruiter.model.JobType
import com.aaonri.app.data.jobs.recruiter.model.PostJobRequest
import com.aaonri.app.data.jobs.recruiter.model.PostingApplicabilityModel
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterPostJobCompanyDetailsBinding
import com.aaonri.app.ui.dashboard.RichTextEditorActivity
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.SelectedJobAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.SelectedVisaStatusAdapterJobRecruiter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.CustomDialog
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar

class RecruiterPostJobRequirementsDetails : Fragment() {
    var binding: FragmentRecruiterPostJobCompanyDetailsBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var visaStatusAdapter: SelectedVisaStatusAdapterJobRecruiter? = null
    var selectedJobAdapter: SelectedJobAdapter? = null
    var description = ""
    var visaStatus = ""
    var jobType = ""
    var jobIdWhileUpdating = 0
    var jobDetailsApplicabilityList = mutableListOf<AllActiveJobApplicabilityResponseItem>()
    var selectedJobApplicabilityList = mutableListOf<PostingApplicabilityModel>()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("result")
                if (data?.isNotEmpty() == true) {
                    binding?.jobDescEt?.fromHtml(data.trim())
                    //binding?.descLength?.text = data.toString().trim().length.toString()
                    description = data.trim()
                } else {
                    binding?.jobDescEt?.text = ""
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecruiterPostJobCompanyDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        visaStatusAdapter = SelectedVisaStatusAdapterJobRecruiter {
            jobRecruiterViewModel.setSelectedVisaStatusJobApplicabilityValue(it)
        }

        selectedJobAdapter = SelectedJobAdapter {
            jobRecruiterViewModel.setSelectJobListMutableValue(it)
        }

        binding?.apply {

            jobRecruiterViewModel.addNavigationForStepper(JobRecruiterConstant.RECRUITER_POST_JOB_REQUIREMENT_DETAILS_SCREEN)

            jobDescEt.textSize = 14F

            visaStatusRv.layoutManager = FlexboxLayoutManager(context)
            visaStatusRv.adapter = visaStatusAdapter

            jobRv.layoutManager = FlexboxLayoutManager(context)
            jobRv.adapter = selectedJobAdapter

            experienceTv.setOnClickListener {
                val action =
                    RecruiterPostJobRequirementsDetailsDirections.actionRecruiterPostJobCompanyDetailsFragmentToJobRequirementScreenBottomSheet(
                        "selectExperience"
                    )
                findNavController().navigate(action)
            }

            industriesTv.setOnClickListener {
                val action =
                    RecruiterPostJobRequirementsDetailsDirections.actionRecruiterPostJobCompanyDetailsFragmentToJobRequirementScreenBottomSheet(
                        "selectIndustries"
                    )
                findNavController().navigate(action)
            }

            billingTypeTv.setOnClickListener {
                val action =
                    RecruiterPostJobRequirementsDetailsDirections.actionRecruiterPostJobCompanyDetailsFragmentToJobRequirementScreenBottomSheet(
                        "selectBillingType"
                    )
                findNavController().navigate(action)
            }

            visaStatusCl.setOnClickListener {
                val action =
                    RecruiterPostJobRequirementsDetailsDirections.actionRecruiterPostJobCompanyDetailsFragmentToSelectVisaStatusBottomSheet()
                findNavController().navigate(action)
            }

            jobCl.setOnClickListener {
                val action =
                    RecruiterPostJobRequirementsDetailsDirections.actionRecruiterPostJobCompanyDetailsFragmentToSelectJobTypeBottomSheet()
                findNavController().navigate(action)
            }

            jobDescEt.setOnClickListener {
                val intent = Intent(context, RichTextEditorActivity::class.java)
                intent.putExtra("isFromAdvertiseBasicDetails", false)
                intent.putExtra("data", description)
                intent.putExtra("placeholder", "Description*")
                resultLauncher.launch(intent)
            }

            submitBtn.setOnClickListener {

                val salary =
                    if (salaryEt.text.isNotEmpty()) salaryEt.text.toString().toDouble() else 0.0

                if (experienceTv.text.isNotEmpty()) {
                    if (industriesTv.text.isNotEmpty()) {
                        if (salary >= 1.0) {
                            if (billingTypeTv.text.isNotEmpty()) {
                                if (visaStatus.isNotEmpty()) {
                                    if (companyNameEt.text.toString().length >= 3) {
                                        if (jobType.isNotEmpty()) {
                                            if (jobDescEt.text.toString().trim().length >= 3) {
                                                if (jobRecruiterViewModel.isUpdateJob) {
                                                    updateJob()
                                                } else {
                                                    postJob()
                                                }
                                            } else {
                                                showAlert("Please enter valid job description")
                                            }
                                        } else {
                                            showAlert("Please select valid job type")
                                        }
                                    } else {
                                        showAlert("Please enter valid company name")
                                    }
                                } else {
                                    showAlert("Please select valid Visa Status")
                                }
                            } else {
                                showAlert("Please select valid billing type")
                            }
                        } else {
                            showAlert("Please enter valid salary")
                        }
                    } else {
                        showAlert("Please select valid industry")
                    }
                } else {
                    showAlert("Please select valid experience level")
                }
            }


            jobRecruiterViewModel.allActiveJobApplicabilityData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        response.data?.forEachIndexed { index, visaStatus ->
                            response.data[index].isSelected =
                                jobDetailsApplicabilityList.indexOfFirst { it.applicability == visaStatus.applicability } != -1
                        }

                        response.data?.let {
                            jobRecruiterViewModel.setSelectedVisaStatusJobApplicabilityValue(
                                it
                            )
                        }
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                    }
                }
            }

            jobRecruiterViewModel.userSelectedExperienceLevel.observe(viewLifecycleOwner) {
                if (it != null) {
                    experienceTv.text = it.experienceLevel
                }
            }

            jobRecruiterViewModel.userSelectedIndustry.observe(viewLifecycleOwner) {
                if (it != null) {
                    industriesTv.text = it.industryType
                }
            }

            jobRecruiterViewModel.userSelectedBillingType.observe(viewLifecycleOwner) {
                if (it != null) {
                    billingTypeTv.text = it.billingType
                }
            }

            jobRecruiterViewModel.selectedVisaStatusJobApplicability.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    visaStatusAdapter?.setData(it)
                    val index = it.indexOfFirst { it.isSelected }
                    if (index == -1) {
                        visaStatus = ""
                        visaStatusTv.visibility = View.VISIBLE
                        visaStatusRv.visibility = View.GONE
                    } else {
                        visaStatusTv.visibility = View.GONE
                        visaStatusRv.visibility = View.VISIBLE
                    }

                    selectedJobApplicabilityList = mutableListOf()

                    it.forEach { item ->
                        if (item.isSelected) {
                            if (!selectedJobApplicabilityList.contains(
                                    PostingApplicabilityModel(
                                        applicabilityId = item.id,
                                        applicability = item.applicability
                                    )
                                )
                            ) {
                                selectedJobApplicabilityList.add(
                                    PostingApplicabilityModel(
                                        applicabilityId = item.id,
                                        applicability = item.applicability
                                    )
                                )
                            }
                            if (!visaStatus.contains(item.applicability)) {
                                visaStatus += item.applicability + ","
                            }
                        }
                    }
                }
            }

            jobRecruiterViewModel.selectedJobList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    selectedJobAdapter?.setData(it)
                    val index = it.indexOfFirst { it.isSelected }
                    if (index == -1) {
                        jobType = ""
                        jobTv.visibility = View.VISIBLE
                        jobRv.visibility = View.GONE
                    } else {
                        jobTv.visibility = View.GONE
                        jobRv.visibility = View.VISIBLE
                    }

                    jobType = ""
                    it.forEach { item ->
                        if (item.isSelected) {
                            if (!jobType.contains(item.name)) {
                                jobType += item.name + ","
                            }
                        }
                    }
                    jobType = jobType.dropLast(1)
                }
            }

            jobRecruiterViewModel.postJobData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        val action =
                            RecruiterPostJobRequirementsDetailsDirections.actionRecruiterPostJobCompanyDetailsFragmentToRecruiterBottomSuccessFragment()
                        findNavController().navigate(action)
                        CustomDialog.hideLoader()
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                        CustomDialog.hideLoader()
                    }
                }
            }

            jobRecruiterViewModel.updateJobData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        val action =
                            RecruiterPostJobRequirementsDetailsDirections.actionRecruiterPostJobCompanyDetailsFragmentToRecruiterBottomSuccessFragment()
                        findNavController().navigate(action)
                        CustomDialog.hideLoader()
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                        CustomDialog.hideLoader()
                    }
                }
            }

            if (jobRecruiterViewModel.isUpdateJob) {

                JobRecruiterStaticData.getJobDetailsValue()?.let { jobDetails ->
                    jobDetailsApplicabilityList =
                        jobDetails.applicability as MutableList<AllActiveJobApplicabilityResponseItem>
                    jobIdWhileUpdating = jobDetails.jobId
                    experienceTv.text = jobDetails.experienceLevel
                    industriesTv.text = jobDetails.industry
                    salaryEt.setText(jobDetails.salaryRange)
                    billingTypeTv.setText(jobDetails.billingType)
                    companyNameEt.setText(jobDetails.company)
                    submitBtn.text = "UPDATE"

                    val tempJobList = mutableListOf<JobType>()

                    tempJobList.addAll(
                        listOf(
                            JobType(
                                count = 0,
                                name = "Full Time",
                                isSelected = false
                            ),
                            JobType(
                                count = 0,
                                name = "Part Time",
                                isSelected = false
                            ),
                            JobType(
                                count = 0,
                                name = "Internship",
                                isSelected = false
                            ),
                            JobType(
                                count = 0,
                                name = "Contract",
                                isSelected = false
                            ),
                            JobType(
                                count = 0,
                                name = "Contract to Hire",
                                isSelected = false
                            ),
                        )
                    )

                    val jobTypeList = jobDetails.jobType.split(",").toTypedArray()

                    tempJobList.forEachIndexed { index, jobType ->
                        if (jobTypeList.contains(jobType.name)) {
                            tempJobList[index].isSelected = true
                        }
                    }


                    jobRecruiterViewModel.setSelectJobListMutableValue(tempJobList)


                    jobDescEt.text = Html.fromHtml(jobDetails.description)
                    description = jobDetails.description
                }
            }
        }



        return binding?.root
    }

    private fun postJob() {

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        jobRecruiterViewModel.postJob(
            PostJobRequest(
                applicability = selectedJobApplicabilityList,
                applyCount = 0,
                billingType = binding?.billingTypeTv?.text.toString(),
                city = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.CITY_NAME]!!,
                company = binding?.companyNameEt?.text.toString(),
                contactPerson = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.CONTACT_PERSONAL_EMAIL]!!,
                country = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.COUNTRY_NAME]!!,
                createdBy = "$email",
                createdOn = "",
                description = if (description.isNotEmpty()) description else binding?.jobDescEt?.text.toString()
                    .trim(),
                experienceLevel = binding?.experienceTv?.text.toString(),
                industry = binding?.industriesTv?.text.toString(),
                isActive = true,
                jobId = 0,
                jobType = jobType,
                recruiter = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.RECRUITER_NAME]!!,
                salaryRange = binding?.salaryEt?.text.toString(),
                skillSet = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.SKILL_SET]!!,
                state = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.STATE_NAME]!!,
                street = "",
                title = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.JOB_TITLE]!!,
                viewCount = 0,
                zipCode = "",
            )
        )
    }

    private fun updateJob() {
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        jobRecruiterViewModel.updateJob(
            PostJobRequest(
                applicability = selectedJobApplicabilityList,
                applyCount = 0,
                billingType = binding?.billingTypeTv?.text.toString(),
                city = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.CITY_NAME]!!,
                company = binding?.companyNameEt?.text.toString(),
                contactPerson = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.CONTACT_PERSONAL_EMAIL]!!,
                country = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.COUNTRY_NAME]!!,
                createdBy = "$email",
                createdOn = "",
                description = if (description.isNotEmpty()) description else binding?.jobDescEt?.text.toString()
                    .trim(),
                experienceLevel = binding?.experienceTv?.text.toString(),
                industry = binding?.industriesTv?.text.toString(),
                isActive = true,
                jobId = jobIdWhileUpdating,
                jobType = jobType,
                recruiter = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.RECRUITER_NAME]!!,
                salaryRange = binding?.salaryEt?.text.toString(),
                skillSet = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.SKILL_SET]!!,
                state = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.STATE_NAME]!!,
                street = "",
                title = jobRecruiterViewModel.recruiterPostJobDetails[JobRecruiterConstant.JOB_TITLE]!!,
                viewCount = 0,
                zipCode = "",
            )
        )
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

}