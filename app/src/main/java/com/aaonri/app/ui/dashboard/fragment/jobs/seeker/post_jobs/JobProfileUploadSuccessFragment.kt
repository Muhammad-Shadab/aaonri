package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.post_jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobProfileUploadSuccessBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobProfileUploadSuccessFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentJobProfileUploadSuccessBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    val args: JobProfileUploadSuccessFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding = FragmentJobProfileUploadSuccessBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            when (args.screenName) {
                "ProfileUploadScreen" -> {
                    successfulTv.text = "You have successfully uploaded your Job Profile."
                    navigateScreenBtn.text = "VIEW MY JOB PROFILE"
                    jobSeekerViewModel.setChangeJobScreenTab("VIEW MY JOB PROFILE")
                }
                "ApplyJobScreen" -> {
                    successfulTv.text = "You have successfully applied for this Job."
                    navigateScreenBtn.text = "VIEW JOBS"
                    jobSeekerViewModel.setChangeJobScreenTab("VIEW JOBS")
                }
                "UpdateProfileScreen" -> {
                    successfulTv.text = "You have successfully updated your Job Profile."
                    navigateScreenBtn.text = "VIEW MY JOB PROFILE"
                    jobSeekerViewModel.setChangeJobScreenTab("VIEW MY JOB PROFILE")
                }
                "CreateJobAlert" -> {
                    successfulTv.text = "You have successfully created your Job alert."
                    navigateScreenBtn.text = "VIEW MY JOB ALERTS"
                    jobSeekerViewModel.setChangeJobScreenTab("VIEW MY JOB ALERTS")
                }
                "UpdateJobAlert" -> {
                    successfulTv.text = "You have successfully updated your Job alert."
                    navigateScreenBtn.text = "VIEW MY JOB ALERTS"
                    jobSeekerViewModel.setChangeJobScreenTab("VIEW MY JOB ALERTS")
                }

            }

            navigateScreenBtn.setOnClickListener {
                if (args.isNavigatingFromSearchScreen) {
                    val action =
                        JobProfileUploadSuccessFragmentDirections.actionJobProfileUploadSuccessFragmentToJobSearchFragment()
                    findNavController().navigate(action)
                } else {
                    val action =
                        JobProfileUploadSuccessFragmentDirections.actionJobProfileUploadSuccessFragmentToJobScreenFragment()
                    findNavController().navigate(action)
                }
            }


        }

        return binding?.root
    }

}