package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.jobs.recruiter.JobRecruiterConstant
import com.aaonri.app.data.jobs.recruiter.model.AllActiveJobApplicabilityResponseItem
import com.aaonri.app.data.jobs.recruiter.model.JobType
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterPostJobCompanyDetailsBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.SelectedJobAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.SelectedVisaStatusAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar

class RecruiterPostJobRequirementsDetails : Fragment() {
    var binding: FragmentRecruiterPostJobCompanyDetailsBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var visaStatusAdapter: SelectedVisaStatusAdapter? = null
    var selectedJobAdapter: SelectedJobAdapter? = null

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

        visaStatusAdapter = SelectedVisaStatusAdapter {
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

            submitBtn.setOnClickListener {
                val salary =
                    if (salaryEt.text.isNotEmpty()) salaryEt.text.toString().toDouble() else 0.0

                if (experienceTv.text.isNotEmpty()) {
                    if (industriesTv.text.isNotEmpty()) {
                        if (salary > 1.0) {
                            if (billingTypeTv.text.isNotEmpty()) {
                                if (visaStatusTv.text.isNotEmpty()) {
                                    if (companyNameEt.text.toString().length >= 3) {
                                        if (jobTv.text.isNotEmpty()) {
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
                        visaStatusTv.visibility = View.VISIBLE
                        visaStatusRv.visibility = View.GONE
                    } else {
                        visaStatusTv.visibility = View.GONE
                        visaStatusRv.visibility = View.VISIBLE
                    }
                }
            }

            jobRecruiterViewModel.selectedJobList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    selectedJobAdapter?.setData(it)
                    val index = it.indexOfFirst { it.isSelected }
                    if (index == -1) {
                        jobTv.visibility = View.VISIBLE
                        jobRv.visibility = View.GONE
                    } else {
                        jobTv.visibility = View.GONE
                        jobRv.visibility = View.VISIBLE
                    }
                }
            }


        }

        return binding?.root
    }

    private fun postJob() {
        jobRecruiterViewModel
    }

    private fun updateJob() {

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