package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.AllActiveExperienceLevelResponseItem
import com.aaonri.app.data.jobs.recruiter.model.AllActiveIndustryResponseItem
import com.aaonri.app.data.jobs.recruiter.model.BillingTypeResponseItem
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentJobRequirementScreenBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.ExperienceIndustriesBillingTypeAdapter
import com.aaonri.app.utils.CustomDialog
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExperienceIndustriesBillingTypeBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentJobRequirementScreenBottomSheetBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    val args: ExperienceIndustriesBillingTypeBottomSheetArgs by navArgs()
    var experienceIndustriesBillingTypeAdapter: ExperienceIndustriesBillingTypeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        binding = FragmentJobRequirementScreenBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false
        )

        experienceIndustriesBillingTypeAdapter = ExperienceIndustriesBillingTypeAdapter()

        experienceIndustriesBillingTypeAdapter?.itemClickListener = { item ->
            if (item is AllActiveExperienceLevelResponseItem) {
                jobRecruiterViewModel.setUserSelectedExperienceLevel(item)
            } else if (item is AllActiveIndustryResponseItem) {
                jobRecruiterViewModel.setUserSelectedIndustry(item)
            } else if (item is BillingTypeResponseItem) {
                jobRecruiterViewModel.setUserSelectedBillingType(item)
            }
            dismiss()
        }

        binding?.apply {

            closeBtn.setOnClickListener {
                dismiss()
            }

            categoriesRv.layoutManager = LinearLayoutManager(context)
            categoriesRv.adapter = experienceIndustriesBillingTypeAdapter

            when (args.clickedOptionName) {
                "selectExperience" -> {

                    selectCategoryTv.text = "Select Experience"

                    jobRecruiterViewModel.allExperienceLevelData.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Resource.Loading -> {
                                CustomDialog.showLoader(requireActivity())
                            }
                            is Resource.Success -> {
                                CustomDialog.hideLoader()
                                response.data?.let {
                                    experienceIndustriesBillingTypeAdapter?.setData(
                                        it
                                    )
                                }
                            }
                            is Resource.Error -> {
                                CustomDialog.hideLoader()
                            }
                        }
                    }
                }
                "selectIndustries" -> {

                    selectCategoryTv.text = "Select Industry"

                    jobRecruiterViewModel.allActiveIndustryData.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Resource.Loading -> {
                                CustomDialog.showLoader(requireActivity())
                            }
                            is Resource.Success -> {
                                CustomDialog.hideLoader()
                                response.data?.let {
                                    experienceIndustriesBillingTypeAdapter?.setData(
                                        it
                                    )
                                }
                            }
                            is Resource.Error -> {
                                CustomDialog.hideLoader()
                            }
                        }
                    }
                }
                "selectBillingType" -> {

                    selectCategoryTv.text = "Select Billing Type"

                    jobRecruiterViewModel.jobBillingTypeData.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Resource.Loading -> {
                                CustomDialog.showLoader(requireActivity())
                            }
                            is Resource.Success -> {
                                CustomDialog.hideLoader()
                                response.data?.let {
                                    experienceIndustriesBillingTypeAdapter?.setData(
                                        it
                                    )
                                }
                            }
                            is Resource.Error -> {
                                CustomDialog.hideLoader()
                            }
                        }
                    }
                }
            }

        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}