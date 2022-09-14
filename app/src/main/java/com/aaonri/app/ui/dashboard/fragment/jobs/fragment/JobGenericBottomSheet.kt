package com.aaonri.app.ui.dashboard.fragment.jobs.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobGenericBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.adapter.JobAdapter
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobGenericBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentJobGenericBottomSheetBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    val args: JobGenericBottomSheetArgs by navArgs()
    var jobAdapter: JobAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobGenericBottomSheetBinding.inflate(layoutInflater, container, false)

        jobAdapter = JobAdapter()

        binding?.apply {

            categoriesRv.layoutManager = LinearLayoutManager(context)
            categoriesRv.adapter = jobAdapter

            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            when (args.selectionName) {
                "experienceSelection" -> {
                    selectCategoryTv.text = "Select Experience"
                    jobSeekerViewModel.allExperienceLevelData.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                response.data?.let { jobAdapter?.setData(it) }
                            }
                            is Resource.Error -> {

                            }
                        }
                    }
                }
                "visaStateSelection" -> {
                    selectCategoryTv.text = "Select Visa Status"
                    jobSeekerViewModel.allActiveJobApplicabilityData.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                response.data?.let { jobAdapter?.setData(it) }
                            }
                            is Resource.Error -> {

                            }
                        }
                    }
                }
                "availabilitySelection" -> {
                    selectCategoryTv.text = "Select Availability"
                    jobSeekerViewModel.allActiveAvailabilityData.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                response.data?.let { jobAdapter?.setData(it) }
                            }
                            is Resource.Error -> {

                            }
                        }
                    }
                }
            }
        }

        return binding?.root
    }
}