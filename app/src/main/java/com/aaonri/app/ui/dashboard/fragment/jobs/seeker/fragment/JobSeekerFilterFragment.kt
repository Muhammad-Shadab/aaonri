package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobSeekerFilterBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobExperienceAdapter
import com.aaonri.app.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobSeekerFilterFragment : Fragment() {
    var binding: FragmentJobSeekerFilterBinding? = null
    private var companyNameList = mutableListOf<String>()
    private var industriesList = mutableListOf<String>()
    var jobExperienceAdapter: JobExperienceAdapter? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobSeekerFilterBinding.inflate(inflater, container, false)

        jobExperienceAdapter = JobExperienceAdapter {

        }

        binding?.apply {

            jobExperienceRv.layoutManager = FlexboxLayoutManager(context)
            jobExperienceRv.adapter = jobExperienceAdapter

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            companyNameEt.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.text.toString().trim().isNotEmpty()) {
                        addNewChip(textView.text.toString(), "companyNameEt", companyNameChipGroup)
                        companyNameEt.setText("")
                    }
                    return@setOnEditorActionListener true
                }
                false
            }

            industriesEt.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.text.toString().trim().isNotEmpty()) {
                        addNewChip(textView.text.toString(), "industriesEt", industriesChipGroup)
                        industriesEt.setText("")
                    }
                    return@setOnEditorActionListener true
                }
                false
            }
        }

        jobSeekerViewModel.allExperienceLevelData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    response.data?.let { jobExperienceAdapter?.setData(it) }
                }
                is Resource.Error -> {

                }
            }
        }

        return binding?.root
    }

    private fun addNewChip(chipText: String, optionName: String, chipGroup: ChipGroup) {
        val chip = Chip(context)
        chip.text = chipText
        chip.isCloseIconEnabled = true
        chip.isClickable = true
        chip.isCheckable = false
        chip.setChipBackgroundColorResource(R.color.blueBtnColor)
        chip.setTextAppearance(R.style.ChipTextStyle_Selected_Custom)
        chip.setCloseIconTintResource(R.color.white)
        chip.minimumWidth = 0
        chip.setEnsureMinTouchTargetSize(false)

        chipGroup.addView(chip as View, chipGroup.childCount - 1)

        when (optionName) {
            "companyNameEt" -> {
                if (!companyNameList.contains(chipText)) {
                    companyNameList.add(chipText)
                }
            }
            "industriesEt" -> {
                if (!industriesList.contains(chipText)) {
                    industriesList.add(chipText)
                }
            }
        }

        chip.setOnCloseIconClickListener {
            when (optionName) {
                "companyNameEt" -> {
                    if (companyNameList.contains(chip.text.toString())) {
                        companyNameList.remove(chip.text.toString())
                    }
                }
                "industriesEt" -> {
                    if (industriesList.contains(chip.text.toString())) {
                        industriesList.remove(chip.text.toString())
                    }
                }
            }
            chipGroup.removeView(chip as View)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}