package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.AllActiveJobApplicabilityResponseItem
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentSelectVisaStatusBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.VisaStatusAdapter
import com.aaonri.app.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectVisaStatusBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentSelectVisaStatusBottomSheetBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var visaStatusAdapter: VisaStatusAdapter? = null
    var userSelectedVisaStatusList: MutableList<AllActiveJobApplicabilityResponseItem> =
        mutableListOf()
    var apiVisaStatusList: MutableList<AllActiveJobApplicabilityResponseItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        visaStatusAdapter = VisaStatusAdapter {
            jobRecruiterViewModel.setSelectedVisaStatusJobApplicabilityValue(it)
        }

        binding =
            FragmentSelectVisaStatusBottomSheetBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            rvBottomFragment.layoutManager = FlexboxLayoutManager(context)
            rvBottomFragment.adapter = visaStatusAdapter

            submitBtn.setOnClickListener {
                dismiss()
            }

            closeBtn.setOnClickListener {
                dismiss()
            }


            jobRecruiterViewModel.allActiveJobApplicabilityData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        response.data?.forEach {
                            if (!apiVisaStatusList.contains(it)) {
                                apiVisaStatusList.add(it)
                            }
                        }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            jobRecruiterViewModel.selectedVisaStatusJobApplicability.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    it.forEach { item ->
                        if (!userSelectedVisaStatusList.contains(item)) {
                            userSelectedVisaStatusList.add(item)
                        }
                    }
                }
            }

            if (userSelectedVisaStatusList.isNotEmpty()) {
                visaStatusAdapter?.setData(userSelectedVisaStatusList)
            } else {
                visaStatusAdapter?.setData(apiVisaStatusList)
            }


        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}