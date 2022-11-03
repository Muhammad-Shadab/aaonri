package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.JobType
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentSelectJobTypeBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.SelectJobAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectJobTypeBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentSelectJobTypeBottomSheetBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var selectJobAdapter: SelectJobAdapter? = null
    var apiJobOptionList: MutableList<JobType> = mutableListOf()
    var userSelectedJobOptionList: MutableList<JobType> = mutableListOf()
    var setApiData = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding = FragmentSelectJobTypeBottomSheetBinding.inflate(layoutInflater, container, false)


        selectJobAdapter = SelectJobAdapter {
            jobRecruiterViewModel.setSelectJobListMutableValue(it)
        }

        jobRecruiterViewModel.selectedJobList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                it.forEach { item ->
                    if (!userSelectedJobOptionList.contains(item)) {
                        userSelectedJobOptionList.add(item)
                    }
                }
                setApiData = false
                selectJobAdapter?.setData(userSelectedJobOptionList)
            }
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
            }

        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
