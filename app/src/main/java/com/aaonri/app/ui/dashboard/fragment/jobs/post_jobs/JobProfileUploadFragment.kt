package com.aaonri.app.ui.dashboard.fragment.jobs.post_jobs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentUploadJobProfileBinding
import com.aaonri.app.utils.Validator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class JobProfileUploadFragment : Fragment() {
    var binding: FragmentUploadJobProfileBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val pdfUri = data.data
                    Toast.makeText(context, "uri $pdfUri", Toast.LENGTH_SHORT).show()
                    val path = pdfUri?.path
                    Toast.makeText(context, "path $path", Toast.LENGTH_SHORT).show()
                }
            }
        }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            resultLauncher.launch(intent)
        } else {
            showAlert("Please allow storage permission")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadJobProfileBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            uploadResumeLl.gravity = Gravity.CENTER

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            selectExperienceTv.setOnClickListener {
                val action =
                    JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobGenericBottomSheet(
                        "experienceSelection"
                    )
                findNavController().navigate(action)
            }

            selectVisaStatusTv.setOnClickListener {
                val action =
                    JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobGenericBottomSheet(
                        "visaStateSelection"
                    )
                findNavController().navigate(action)
            }

            selectAvailabilityTv.setOnClickListener {
                val action =
                    JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobGenericBottomSheet(
                        "availabilitySelection"
                    )
                findNavController().navigate(action)
            }

            uploadResumeLl.setOnClickListener {
                if (checkPermission()) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT);
                    intent.type = "application/pdf";
                    resultLauncher.launch(intent)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            uploadProfileNextBtn.setOnClickListener {
                if (firstNameEt.text.toString().length >= 3) {
                    if (lastNameEt.text.toString().length >= 3) {
                        if (currentTitleEt.text.toString().length >= 3) {
                            if (Validator.emailValidation(contactEmailEt.text.toString().trim())) {
                                if (phoneNumberEt.text.toString().length == 10) {
                                    if (locationEt.text.toString().length >= 3) {
                                        if (selectExperienceTv.text.toString().isNotEmpty()) {
                                            if (selectVisaStatusTv.text.toString().isNotEmpty()) {
                                                if (selectAvailabilityTv.text.toString()
                                                        .isNotEmpty()
                                                ) {
                                                    if (skillSetDescEt.text.toString().length >= 3) {
                                                        if (coverLetterDescEt.text.toString().length >= 3) {

                                                        } else {
                                                            showAlert("Please enter valid Cover Letter Description")
                                                        }
                                                    } else {
                                                        showAlert("Please enter valid Skill Set")
                                                    }
                                                } else {
                                                    showAlert("Please choose valid Availability")
                                                }
                                            } else {
                                                showAlert("Please choose valid Visa Status")
                                            }
                                        } else {
                                            showAlert("Please choose valid Experience")
                                        }
                                    } else {
                                        showAlert("Please enter valid Location")
                                    }
                                } else {
                                    showAlert("Please enter valid Phone Number")
                                }
                            } else {
                                showAlert("Please enter valid Email")
                            }
                        } else {
                            showAlert("Please enter valid Title")
                        }
                    } else {
                        showAlert("Please enter valid Last Name")
                    }
                } else {
                    showAlert("Please enter valid First Name")
                }
            }

        }

        jobSeekerViewModel.getAllActiveJobApplicability()
        jobSeekerViewModel.getAllActiveExperienceLevel()
        jobSeekerViewModel.getAllActiveAvailability()

        jobSeekerViewModel.selectedExperienceLevel.observe(viewLifecycleOwner) {
            if (it != null) {
                binding?.selectExperienceTv?.text = it.experienceLevel
            }
        }
        jobSeekerViewModel.selectedJobApplicability.observe(viewLifecycleOwner) {
            if (it != null) {
                binding?.selectVisaStatusTv?.text = it.applicability
            }
        }
        jobSeekerViewModel.selectedJobAvailability.observe(viewLifecycleOwner) {
            if (it != null) {
                binding?.selectAvailabilityTv?.text = it.availability
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

    private fun checkPermission(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        jobSeekerViewModel.selectedExperienceLevel.postValue(null)
        jobSeekerViewModel.selectedJobApplicability.postValue(null)
        jobSeekerViewModel.selectedJobAvailability.postValue(null)
    }
}