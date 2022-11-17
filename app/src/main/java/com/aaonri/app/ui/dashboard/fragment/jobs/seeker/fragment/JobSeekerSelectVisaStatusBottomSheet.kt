package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.model.AllActiveJobApplicabilityResponseItem
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobSeekerSelectVisaStatusBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.VisaStatusAdapterJobSeeker
import com.aaonri.app.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobSeekerSelectVisaStatusBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentJobSeekerSelectVisaStatusBottomSheetBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var visaStatusAdapter: VisaStatusAdapterJobSeeker? = null
    var apiVisaStatusList: MutableList<AllActiveJobApplicabilityResponseItem> = mutableListOf()
    var userSelectedVisaStatusList: MutableList<AllActiveJobApplicabilityResponseItem> =
        mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        binding = FragmentJobSeekerSelectVisaStatusBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false
        )

        visaStatusAdapter = VisaStatusAdapterJobSeeker {
            jobSeekerViewModel.setSelectedVisaStatusJobApplicabilityValue(it)
        }

        binding?.apply {

            rvBottomFragment.layoutManager = FlexboxLayoutManager(context)
            rvBottomFragment.adapter = visaStatusAdapter

            submitBtn.setOnClickListener {
                dismiss()
            }

            closeBtn.setOnClickListener {
                dismiss()
            }

            jobSeekerViewModel.allActiveJobApplicabilityData.observe(viewLifecycleOwner) { response ->
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

            jobSeekerViewModel.selectedVisaStatusJobApplicability.observe(viewLifecycleOwner) {
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