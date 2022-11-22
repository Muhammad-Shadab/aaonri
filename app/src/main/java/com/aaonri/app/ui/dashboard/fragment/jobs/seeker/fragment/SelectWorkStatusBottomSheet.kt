package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.JobType
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentSelectJobTypeBottomSheetBinding
import com.aaonri.app.databinding.FragmentSelectWorkStatusBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.SelectJobAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectWorkStatusBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentSelectWorkStatusBottomSheetBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var selectJobAdapter: SelectJobAdapter? = null
    var apiJobOptionList: MutableList<JobType> = mutableListOf()
    var userSelectedJobOptionList: MutableList<JobType> = mutableListOf()
    var setApiData = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectWorkStatusBottomSheetBinding.inflate(layoutInflater, container, false)

        selectJobAdapter = SelectJobAdapter {
            jobSeekerViewModel.setSelectJobListMutableValue(it)
        }

        binding?.apply {

            closeBtn.setOnClickListener {
                dismiss()
            }

            submitBtn.setOnClickListener {
                dismiss()
            }

            rvBottomFragment.layoutManager = FlexboxLayoutManager(context)
            rvBottomFragment.adapter = selectJobAdapter

            jobSeekerViewModel.selectedJobListTemp.forEach { item ->
                if (!userSelectedJobOptionList.contains(item)) {
                    userSelectedJobOptionList.add(item)
                }
                setApiData = false
            }

            apiJobOptionList.addAll(
                listOf(
                    JobType(
                        count = 0,
                        name = "Full Time",
                        isSelected = false
                    ),
                    JobType(
                        count = 0,
                        name = "Part Time",
                        isSelected = false
                    ),
                    JobType(
                        count = 0,
                        name = "Internship",
                        isSelected = false
                    ),
                    JobType(
                        count = 0,
                        name = "Contract",
                        isSelected = false
                    ),
                    JobType(
                        count = 0,
                        name = "Contract to Hire",
                        isSelected = false
                    ),
                )
            )

            if (setApiData) {
                selectJobAdapter?.setData(apiJobOptionList)
            } else {
                selectJobAdapter?.setData(userSelectedJobOptionList)
            }

        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}