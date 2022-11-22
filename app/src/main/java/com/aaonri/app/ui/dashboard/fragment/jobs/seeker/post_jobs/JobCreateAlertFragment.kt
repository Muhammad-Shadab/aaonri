package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.post_jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.data.jobs.recruiter.model.JobType
import com.aaonri.app.data.jobs.seeker.model.CreateAlertRequest
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobCreateAlertBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.SelectedJobAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobCreateAlertFragment : Fragment() {
    var binding: FragmentJobCreateAlertBinding? = null
    val args: JobCreateAlertFragmentArgs by navArgs()
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var selectedJobAdapter: SelectedJobAdapter? = null
    var workStatus = ""
    var userJobProfileId = 0
    var jobAlertId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobCreateAlertBinding.inflate(layoutInflater, container, false)

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        selectedJobAdapter = SelectedJobAdapter {
            jobSeekerViewModel.setSelectJobListMutableValue(it)
        }

        binding?.apply {

            workStatusRv.layoutManager = FlexboxLayoutManager(context)
            workStatusRv.adapter = selectedJobAdapter

            navigateBack.setOnClickListener {
                jobSeekerViewModel.navigateToUpdateJobAlert.postValue(null)
                findNavController().navigateUp()
            }

            selectWorkExperienceTv.setOnClickListener {
                val action =
                    JobCreateAlertFragmentDirections.actionJobCreateAlertFragmentToJobGenericBottomSheet(
                        "experienceSelection"
                    )
                findNavController().navigate(action)
            }

            workStatusCl.setOnClickListener {
                val action =
                    JobCreateAlertFragmentDirections.actionJobCreateAlertFragmentToSelectWorkStatusBottomSheet()
                findNavController().navigate(action)
            }

            submitBtn.setOnClickListener {

                val salary =
                    if (expectedSalaryEt.text.isNotEmpty()) expectedSalaryEt.text.toString()
                        .toDouble() else 0.0

                if (jobAlertNameEt.text.toString().length >= 3) {
                    if (keywordEt.text.toString().length >= 3) {
                        if (jobRoleEt.text.toString().length >= 3) {
                            if (selectWorkExperienceTv.text.toString().isNotEmpty()) {
                                if (salary >= 1.0) {
                                    if (locationEt.text.toString().length >= 3) {
                                        if (locationEt.text.toString().length >= 3) {
                                            if (workStatus.isNotEmpty()) {
                                                if (args.isUpdateJobAlert) {
                                                    jobSeekerViewModel.updateJobAlert(
                                                        jobAlertId, CreateAlertRequest(
                                                            email = "$email",
                                                            expectedSalary = salary.toString(),
                                                            jobAlertName = jobAlertNameEt.text.toString(),
                                                            jobProfileId = userJobProfileId,
                                                            keyword = keywordEt.text.toString(),
                                                            location = locationEt.text.toString(),
                                                            role = jobRoleEt.text.toString(),
                                                            workExp = selectWorkExperienceTv.text.toString(),
                                                            workStatus = workStatus
                                                        )
                                                    )
                                                } else {
                                                    jobSeekerViewModel.createJobAlert(
                                                        CreateAlertRequest(
                                                            email = "$email",
                                                            expectedSalary = salary.toString(),
                                                            jobAlertName = jobAlertNameEt.text.toString(),
                                                            jobProfileId = userJobProfileId,
                                                            keyword = keywordEt.text.toString(),
                                                            location = locationEt.text.toString(),
                                                            role = jobRoleEt.text.toString(),
                                                            workExp = selectWorkExperienceTv.text.toString(),
                                                            workStatus = workStatus
                                                        )
                                                    )
                                                }
                                            } else {
                                                showAlert("Please enter valid work status")
                                            }
                                        } else {
                                            showAlert("Please enter valid location")
                                        }
                                    } else {
                                        showAlert("Please enter valid location")
                                    }
                                } else {
                                    showAlert("Please enter valid expected salary")
                                }
                            } else {
                                showAlert("Please enter valid work experience")
                            }
                        } else {
                            showAlert("Please enter valid job role")
                        }
                    } else {
                        showAlert("Please enter valid keyword")
                    }
                } else {
                    showAlert("Please enter valid job alert name")
                }
            }

            jobSeekerViewModel.selectedExperienceLevel.observe(viewLifecycleOwner) {
                if (it != null) {
                    selectWorkExperienceTv.text = it.experienceLevel
                }
            }

            jobSeekerViewModel.selectedJobList.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        selectedJobAdapter?.setData(it)
                        val index = it.indexOfFirst { it.isSelected }
                        if (index == -1) {
                            workStatus = ""
                            workStatusTv.visibility = View.VISIBLE
                            workStatusRv.visibility = View.GONE
                        } else {
                            workStatusTv.visibility = View.GONE
                            workStatusRv.visibility = View.VISIBLE
                        }
                        workStatus = ""
                        it.forEach { item ->
                            if (item.isSelected) {
                                if (!workStatus.contains(item.name)) {
                                    workStatus += item.name + ","
                                }
                            }
                        }
                        workStatus = workStatus.dropLast(1)
                    }
                }
            }

            jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            if (it.jobProfile.isNotEmpty()) {
                                userJobProfileId = it.jobProfile[0].id
                            }
                        }
                    }
                    is Resource.Error -> {
                    }
                }
            }

            jobSeekerViewModel.createJobAlertData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        val action =
                            JobCreateAlertFragmentDirections.actionJobCreateAlertFragmentToJobProfileUploadSuccessFragment(
                                "CreateJobAlert", false
                            )
                        findNavController().navigate(action)
                        jobSeekerViewModel.createJobAlertData.postValue(null)
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            jobSeekerViewModel.updateJobAlertData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        val action =
                            JobCreateAlertFragmentDirections.actionJobCreateAlertFragmentToJobProfileUploadSuccessFragment(
                                "CreateJobAlert", false
                            )
                        findNavController().navigate(action)
                        jobSeekerViewModel.updateJobAlertData.postValue(null)
                        jobSeekerViewModel.navigateToUpdateJobAlert.postValue(null)
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            if (args.isUpdateJobAlert) {

                createJobAlertDesc.text =
                    "Update your job alerts by filling below details\nand you can see alerts in your job alerts page in tags"
                appbarTextTv.text = "Update Job Alert"
                submitBtn.text = "UPDATE"

                jobSeekerViewModel.navigateToUpdateJobAlert.observe(viewLifecycleOwner) {
                    if (it != null) {
                        jobAlertId = it.id
                        jobAlertNameEt.setText(it.jobAlertName)
                        keywordEt.setText(it.keyword)
                        jobRoleEt.setText(it.role)
                        selectWorkExperienceTv.text = it.workExp
                        expectedSalaryEt.setText(it.expectedSalary)
                        locationEt.setText(it.location)

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

                        val jobTypeList = it.workStatus.split(",").toTypedArray()

                        tempJobList.forEachIndexed { index, jobType ->
                            if (jobTypeList.contains(jobType.name)) {
                                tempJobList[index].isSelected = true
                            }
                        }

                        jobSeekerViewModel.setSelectJobListMutableValue(tempJobList)

                    }
                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    jobSeekerViewModel.navigateToUpdateJobAlert.postValue(null)
                    findNavController().navigateUp()
                }
            })



        return binding?.root

    }


    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobSeekerViewModel.selectedExperienceLevel.postValue(null)
        jobSeekerViewModel.selectedJobList.postValue(null)
        jobSeekerViewModel.navigateToUpdateJobAlert.postValue(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}