package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.model.ActiveJobAvailabilityResponseItem
import com.aaonri.app.data.jobs.seeker.model.AllActiveJobApplicabilityResponseItem
import com.aaonri.app.data.jobs.seeker.model.ExperienceLevelResponseItem
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobGenericBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobAdapter
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobSeekerGenericBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    var binding: FragmentJobGenericBottomSheetBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    val args: JobSeekerGenericBottomSheetArgs by navArgs()

    private var jobAdapter: JobAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding = FragmentJobGenericBottomSheetBinding.inflate(layoutInflater, container, false)

        jobAdapter = JobAdapter()

        jobAdapter?.itemClickListener = { view, item, position ->
            when (item) {
                is ExperienceLevelResponseItem -> {
                    jobSeekerViewModel.setSelectedExperienceLevel(item)
                    dismiss()
                }
                is AllActiveJobApplicabilityResponseItem -> {
                    jobSeekerViewModel.setSelectedJobApplicability(item)
                    dismiss()
                }
                is ActiveJobAvailabilityResponseItem -> {
                    jobSeekerViewModel.setSelectedJobAvailability(item)
                    dismiss()
                }
            }
        }

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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}