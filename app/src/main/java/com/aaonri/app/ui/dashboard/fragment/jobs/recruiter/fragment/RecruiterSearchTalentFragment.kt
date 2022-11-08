package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.RecruiterJobFilterModel
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterSearchBinding
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecruiterSearchTalentFragment : Fragment() {
    var binding: FragmentRecruiterSearchBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    val anyKeywordList = mutableListOf<String>()
    val allKeywordList = mutableListOf<String>()
    val skillSetList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecruiterSearchBinding.inflate(inflater, container, false)

        binding?.apply {

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            anyKeywordEt.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    addNewChip(textView.text.toString(), "anyKeyword", anyKeywordFlexBox)
                    if (anyKeywordList.isNotEmpty()) {
                        anyKeywordEt.setHint("")
                    }
                    anyKeywordEt.setText("")

                    return@setOnEditorActionListener true
                }
                false
            }

            allKeywordMustEt.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    addNewChip(textView.text.toString(), "allKeyword", allKeywordMustFlexBox)
                    allKeywordMustEt.setText("")
                    if (allKeywordList.isNotEmpty()) {
                        allKeywordMustEt.setHint("")
                    }
                    return@setOnEditorActionListener true
                }
                false
            }

            skillsEt.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    addNewChip(textView.text.toString(), "skillSet", skillsFlexBox)
                    skillsEt.setText("")
                    if (skillSetList.isNotEmpty()) {
                        skillsEt.setHint("")
                    }
                    return@setOnEditorActionListener true
                }
                false
            }

            availablityTv.setOnClickListener {
                val action =
                    RecruiterSearchTalentFragmentDirections.actionRecruiterSearchTalentFragmentToAvailabilityBottomSheet()
                findNavController().navigate(action)
            }

            jobRecruiterViewModel.selectedAvailability.observe(viewLifecycleOwner) {
                if (it != null) {
                    availablityTv.text = it
                }
            }

            resetBtn.setOnClickListener {
                jobRecruiterViewModel.setJobRecruiterFilterValues(
                    RecruiterJobFilterModel(
                        anyKeywords = "",
                        allKeywords = "",
                        availability = "",
                        location = "",
                        skillSet = ""
                    )
                )
            }

            searchBtn.setOnClickListener {
                if (locationEt.text.toString().isNotEmpty()) {
                    if (locationEt.text.toString().length >= 3) {
                        jobRecruiterViewModel.setJobRecruiterFilterValues(
                            RecruiterJobFilterModel(
                                anyKeywords = anyKeywordList.toString(),
                                allKeywords = allKeywordList.toString(),
                                availability = availablityTv.text.toString(),
                                location = locationEt.text.toString(),
                                skillSet = skillSetList.toString()
                            )
                        )
                    }
                    findNavController().navigateUp()
                } else {
                    jobRecruiterViewModel.setJobRecruiterFilterValues(
                        RecruiterJobFilterModel(
                            anyKeywords = anyKeywordList.toString(),
                            allKeywords = allKeywordList.toString(),
                            availability = availablityTv.text.toString(),
                            location = locationEt.text.toString(),
                            skillSet = skillSetList.toString()
                        )
                    )
                    findNavController().navigateUp()
                }

            }

        }

        return binding?.root
    }

    private fun addNewChip(person: String, optionName: String, chipGroup: FlexboxLayout) {
        val chip = Chip(context)
        chip.text = person
        chip.isCloseIconEnabled = true
        chip.isClickable = true
        chip.isCheckable = false
        chip.setChipBackgroundColorResource(R.color.blueBtnColor)
        chip.setTextAppearance(R.style.ChipTextStyle_Selected_Custom)
        chip.setCloseIconTintResource(R.color.white)
        chip.minimumWidth = 0
        //chip.setEnsureMinTouchTargetSize(false)
        chipGroup.addView(chip as View, chipGroup.childCount - 1)

        when (optionName) {
            "anyKeyword" -> {
                if (!anyKeywordList.contains(person)) {
                    anyKeywordList.add(person)
                }
            }
            "allKeyword" -> {
                if (!allKeywordList.contains(person)) {
                    allKeywordList.add(person)
                }
            }
            "skillSet" -> {
                if (!skillSetList.contains(person)) {
                    skillSetList.add(person)
                }
            }
        }

        //val commaSeparatedString = anyKeywordList.joinToString (separator = ",") { it -> "\'${it.nameOfStringVariable}\'" }

        chip.setOnCloseIconClickListener {
            when (optionName) {
                "anyKeyword" -> {
                    if (anyKeywordList.contains(chip.text.toString())) {
                        anyKeywordList.remove(chip.text.toString())
                    }
                    if (anyKeywordList.isEmpty()) {
                        binding?.anyKeywordEt?.setHint("Any Keywords")
                    }
                }
                "allKeyword" -> {
                    if (allKeywordList.contains(chip.text.toString())) {
                        allKeywordList.remove(chip.text.toString())
                    }
                    if (allKeywordList.isEmpty()) {
                        binding?.allKeywordMustEt?.setHint("All Keywords (must have)")
                    }
                }
                "skillSet" -> {
                    if (skillSetList.contains(chip.text.toString())) {
                        skillSetList.remove(chip.text.toString())
                    }
                    if (skillSetList.isEmpty()) {
                        binding?.skillsEt?.setHint("Skill set")
                    }
                }
            }
            chipGroup.removeView(chip as View)
        }
    }
}