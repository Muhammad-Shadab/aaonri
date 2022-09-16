package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentMyJobProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyJobProfileFragment : Fragment() {
    var binding: FragmentMyJobProfileBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMyJobProfileBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            uploadYourProfileBtn.setOnClickListener {
                jobSeekerViewModel.setNavigateToUploadJobProfileScreen(true)
            }

        }

        return binding?.root

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}