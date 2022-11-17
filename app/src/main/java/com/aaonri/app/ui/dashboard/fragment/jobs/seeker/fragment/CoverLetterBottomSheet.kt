package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentCoverLetterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoverLetterBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentCoverLetterBottomSheetBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        binding = FragmentCoverLetterBottomSheetBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            jobSeekerViewModel.userJobProfileCoverLetterValue.observe(viewLifecycleOwner) {
                if (it != null) {
                    coverLetterTv.text = Html.fromHtml(it)
                }
            }

        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        jobSeekerViewModel.userJobProfileCoverLetterValue.postValue(null)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}