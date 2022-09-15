package com.aaonri.app.ui.dashboard.fragment.jobs.post_jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentUploadJobProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobProfileUploadFragment : Fragment() {
    var binding: FragmentUploadJobProfileBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadJobProfileBinding.inflate(layoutInflater, container, false)

        binding?.apply {

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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        jobSeekerViewModel.selectedExperienceLevel.postValue(null)
        jobSeekerViewModel.selectedJobApplicability.postValue(null)
        jobSeekerViewModel.selectedJobAvailability.postValue(null)
    }
}