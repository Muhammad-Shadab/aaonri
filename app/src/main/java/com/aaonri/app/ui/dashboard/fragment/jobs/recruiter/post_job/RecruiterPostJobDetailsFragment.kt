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
import com.aaonri.app.databinding.FragmentRecruiterPostJobDetailsBinding
import com.aaonri.app.utils.Validator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterPostJobDetailsFragment : Fragment() {
    var binding: FragmentRecruiterPostJobDetailsBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecruiterPostJobDetailsBinding.inflate(inflater, container, false)

        binding?.apply {

            jobRecruiterViewModel.addNavigationForStepper(JobRecruiterConstant.RECRUITER_POST_JOB_DETAILS_SCREEN)

            selectStateTv.setOnClickListener {
                val action =
                    RecruiterPostJobDetailsFragmentDirections.actionRecruiterPostJobDetailsFragmentToJobPostDetailsScreenBottomSheet(
                        ""
                    )
                findNavController().navigate(action)
            }

            nextBtn.setOnClickListener {

                if (jobTitleEt.text.toString().length >= 3) {
                    if (cityNameEt.text.toString().length >= 3) {
                        if (selectStateTv.text.toString().isNotEmpty()) {
                            if (selectCountryTv.text.toString().isNotEmpty()) {
                                if (Validator.emailValidation(
                                        recruiterEmailEt.text.toString().trim()
                                    )
                                ) {
                                    if (recruiterNameEt.text.toString().length >= 3) {
                                        if (skillSetDescEt.text.toString().trim().length >= 3) {
                                            jobRecruiterViewModel.setRecruiterPostJobDetails(
                                                jobTitle = jobTitleEt.text.toString(),
                                                cityName = cityNameEt.text.toString(),
                                                state = selectStateTv.text.toString(),
                                                country = selectCountryTv.text.toString(),
                                                contactPersonalEmail = recruiterEmailEt.text.toString(),
                                                recruiterName = recruiterNameEt.text.toString(),
                                                skillSet = skillSetDescEt.text.toString()
                                            )
                                            val action =
                                                RecruiterPostJobDetailsFragmentDirections.actionRecruiterPostJobDetailsFragmentToRecruiterPostJobCompanyDetailsFragment()
                                            findNavController().navigate(action)
                                        } else {
                                            showAlert("Please enter valid skill set description")
                                        }
                                    } else {
                                        showAlert("Please enter valid recruiter name")
                                    }
                                } else {
                                    showAlert("Please enter valid contact personal email")
                                }
                            } else {
                                showAlert("Please select valid city")
                            }
                        } else {
                            showAlert("Please select valid state")
                        }
                    } else {
                        showAlert("Please enter valid city name")
                    }
                } else {
                    showAlert("Please enter valid Job title")
                }
            }

            jobRecruiterViewModel.userSelectedState.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    selectStateTv.text = it
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