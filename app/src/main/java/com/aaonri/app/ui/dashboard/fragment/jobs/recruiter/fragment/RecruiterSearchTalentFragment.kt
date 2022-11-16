package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.RecruiterJobFilterModel
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterSearchBinding
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecruiterSearchTalentFragment : Fragment() {
    var binding: FragmentRecruiterSearchBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var anyKeywordList = mutableListOf<String>()
    var allKeywordList = mutableListOf<String>()
    var skillSetList = mutableListOf<String>()

    @SuppressLint("ClickableViewAccessibility")
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

            nestedScrollView.setOnTouchListener { view, motionEvent ->
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                return@setOnTouchListener true
            }

            anyKeywordEt.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.text.toString().trim().isNotEmpty()) {
                        addNewChip(textView.text.toString(), "anyKeyword", anyKeywordChipGroup)
                        anyKeywordEt.setText("")
                    }
                    /*if (anyKeywordList.isNotEmpty()) {
                        anyKeywordEt.setHint("")
                    }*/
                    return@setOnEditorActionListener true
                }
                false
            }

            allKeywordMustEt.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.text.toString().trim().isNotEmpty()) {
                        addNewChip(textView.text.toString(), "allKeyword", allKeywordMustChipGroup)
                        allKeywordMustEt.setText("")
                    }
                    /*if (allKeywordList.isNotEmpty()) {
                        allKeywordMustEt.setHint("")
                    }*/
                    return@setOnEditorActionListener true
                }
                false
            }

            skillsEt.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.text.toString().trim().isNotEmpty()) {
                        addNewChip(textView.text.toString(), "skillSet", skillsChipGroup)
                        skillsEt.setText("")
                    }
                    /*if (skillSetList.isNotEmpty()) {
                        skillsEt.setHint("")
                    }*/
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

                anyKeywordChipGroup.removeAllViews()
                allKeywordMustChipGroup.removeAllViews()
                skillsChipGroup.removeAllViews()

                anyKeywordList = mutableListOf()
                allKeywordList = mutableListOf()
                skillSetList = mutableListOf()

                availablityTv.text = ""
                locationEt.setText("")

                jobRecruiterViewModel.setSelectedAvailability("")

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

                val commaSeparatedAnyKeywordsString =
                    if (anyKeywordList.isNotEmpty()) anyKeywordList.joinToString(separator = ",") { it -> "\'${it}\'" } else ""
                val commaSeparatedAllKeywordsString =
                    if (allKeywordList.isNotEmpty()) allKeywordList.joinToString(separator = ",") { it -> "\'${it}\'" } else ""
                val commaSeparatedSkillSetString =
                    if (skillSetList.isNotEmpty()) skillSetList.joinToString(separator = ",") { it -> "\'${it}\'" } else ""

                if (anyKeywordEt.text.toString().isNotEmpty()) {
                    showAlert("Please go to the any keyword field and hit keypad enter button.")
                }
                if (allKeywordMustEt.text.toString().isNotEmpty()) {
                    showAlert("Please go to the all keyword field and hit keypad enter button.")
                }
                if (skillsEt.text.toString().isNotEmpty()) {
                    showAlert("Please go to the skills field and hit keypad enter button.")
                }

                if (anyKeywordList.isNotEmpty() || allKeywordList.isNotEmpty() || skillSetList.isNotEmpty() || availablityTv.text.toString().isNotEmpty() || locationEt.text.toString().isNotEmpty()){
                    if (anyKeywordEt.text.toString().isEmpty() && allKeywordMustEt.text.toString().isEmpty() && skillsEt.text.toString().isEmpty()
                    ) {
                        if (locationEt.text.toString().isNotEmpty()) {
                            if (locationEt.text.toString().length >= 3) {
                                jobRecruiterViewModel.setJobRecruiterFilterValues(
                                    RecruiterJobFilterModel(
                                        anyKeywords = commaSeparatedAnyKeywordsString.replace("[", "")
                                            .replace("]", "").replace("'", ""),
                                        allKeywords = commaSeparatedAllKeywordsString.replace("[", "")
                                            .replace("]", "").replace("'", ""),
                                        availability = availablityTv.text.toString(),
                                        location = locationEt.text.toString(),
                                        skillSet = commaSeparatedSkillSetString.replace("[", "")
                                            .replace("]", "").replace("'", "")
                                    )
                                )
                            }
                            findNavController().navigateUp()
                        } else {
                            jobRecruiterViewModel.setJobRecruiterFilterValues(
                                RecruiterJobFilterModel(
                                    anyKeywords = commaSeparatedAnyKeywordsString.replace("[", "")
                                        .replace("]", "").replace("'", ""),
                                    allKeywords = commaSeparatedAllKeywordsString.replace("[", "")
                                        .replace("]", "").replace("'", ""),
                                    availability = availablityTv.text.toString(),
                                    location = locationEt.text.toString(),
                                    skillSet = commaSeparatedSkillSetString.replace("[", "")
                                        .replace("]", "").replace("'", "")
                                )
                            )
                            findNavController().navigateUp()
                        }
                    }
                }

            }

            jobRecruiterViewModel.jobRecruiterFilterValues.observe(viewLifecycleOwner) { filterData ->
                if (filterData.anyKeywords.isNotEmpty()) {
                    filterData.anyKeywords.split(",").toTypedArray().forEach {
                        addNewChip(it, "anyKeyword", anyKeywordChipGroup)
                    }
                }

                if (filterData.allKeywords.isNotEmpty()) {
                    filterData.allKeywords.split(",").toTypedArray().forEach {
                        addNewChip(it, "allKeyword", allKeywordMustChipGroup)
                    }
                }

                if (filterData.availability.isNotEmpty()) {
                    availablityTv.text = filterData.availability
                }

                if (filterData.location.isNotEmpty()) {
                    locationEt.setText(filterData.location)
                }

                if (filterData.skillSet.isNotEmpty()) {
                    filterData.skillSet.split(",").toTypedArray().forEach {
                        addNewChip(it, "skillSet", skillsChipGroup)
                    }
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
            "anyKeyword" -> {
                if (!anyKeywordList.contains(chipText)) {
                    anyKeywordList.add(chipText)
                }
            }
            "allKeyword" -> {
                if (!allKeywordList.contains(chipText)) {
                    allKeywordList.add(chipText)
                }
            }
            "skillSet" -> {
                if (!skillSetList.contains(chipText)) {
                    skillSetList.add(chipText)
                }
            }
        }

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

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

}