package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.jobs.recruiter.JobRecruiterConstant
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterPostJobCompanyDetailsBinding
import com.google.android.material.snackbar.Snackbar

class RecruiterPostJobRequirementsDetails : Fragment() {
    var binding: FragmentRecruiterPostJobCompanyDetailsBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()

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

        binding?.apply {

            jobRecruiterViewModel.addNavigationForStepper(JobRecruiterConstant.RECRUITER_POST_JOB_REQUIREMENT_DETAILS_SCREEN)

            jobDescEt.textSize = 14F

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

            submitBtn.setOnClickListener {
                val salary =
                    if (salaryEt.text.isNotEmpty()) salaryEt.text.toString().toDouble() else 0.0

                if (experienceTv.text.isNotEmpty()) {
                    if (industriesTv.text.isNotEmpty()) {
                        if (salary > 0.0) {
                            if (billingTypeTv.text.isNotEmpty()) {
                                if (visaStatusTv.text.isNotEmpty()) {
                                    if (companyNameEt.text.toString().length >= 3) {
                                        if (jobTypeTv.text.isNotEmpty()) {
                                            if (jobDescEt.text.toString().trim().length >= 3) {

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

        }

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

}