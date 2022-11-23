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
import com.aaonri.app.data.jobs.recruiter.model.JobType
import com.aaonri.app.data.jobs.seeker.model.JobSearchFilterModel
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobSeekerFilterBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobExperienceAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobTypeAdapter
import com.aaonri.app.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobSeekerFilterFragment : Fragment() {
    var binding: FragmentJobSeekerFilterBinding? = null
    private var companyNameList = mutableListOf<String>()
    private var industriesList = mutableListOf<String>()
    var jobExperienceAdapter: JobExperienceAdapter? = null
    var jobTypeAdapter: JobTypeAdapter? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var selectedYearsOfExperience = ""
    var selectedJobType = ""
    val tempJobList = mutableListOf<JobType>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobSeekerFilterBinding.inflate(inflater, container, false)

        jobExperienceAdapter = JobExperienceAdapter {
            selectedYearsOfExperience = it
        }

        jobTypeAdapter = JobTypeAdapter {
            jobSeekerViewModel.setSelectJobListMutableValue(it)
        }

        tempJobList.addAll(
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

        binding?.apply {

            jobExperienceRv.layoutManager = FlexboxLayoutManager(context)
            jobExperienceRv.adapter = jobExperienceAdapter

            jobTypeRv.layoutManager = FlexboxLayoutManager(context)
            jobTypeRv.adapter = jobTypeAdapter

            navigateBack.setOnClickListener {
                findNavController().navigateUp()

            }

            applyBtn.setOnClickListener {

                val commaSeparatedCompanyNameString =
                    if (companyNameList.isNotEmpty()) companyNameList.joinToString(separator = ",") { it -> "\'${it}\'" } else ""
                val commaSeparatedIndustryString =
                    if (industriesList.isNotEmpty()) industriesList.joinToString(separator = ",") { it -> "\'${it}\'" } else ""

                if (companyNameEt.text.toString().isNotEmpty()) {
                    showAlert("Please go to the any keyword field and hit keypad enter button.")
                }
                if (industriesEt.text.toString().isNotEmpty()) {
                    showAlert("Please go to the all keyword field and hit keypad enter button.")
                }

                if (companyNameList.isNotEmpty() || locationEt.text.toString()
                        .isNotEmpty() || selectedYearsOfExperience.isNotEmpty() || selectedJobType.isNotEmpty() || industriesList.isNotEmpty()
                ) {
                    if (companyNameEt.text.toString().isEmpty() && industriesEt.text.toString()
                            .isEmpty()
                    ) {
                        if (locationEt.text.toString().isNotEmpty()) {
                            if (locationEt.text.toString().length >= 3) {
                                jobSeekerViewModel.setJobSearchFilterData(
                                    JobSearchFilterModel(
                                        companyName = commaSeparatedCompanyNameString.replace(
                                            "[",
                                            ""
                                        )
                                            .replace("]", "").replace("'", ""),
                                        location = locationEt.text.toString(),
                                        yearsOfExperience = selectedYearsOfExperience,
                                        jobType = selectedJobType,
                                        industries = commaSeparatedIndustryString.replace("[", "")
                                            .replace("]", "").replace("'", "")
                                    )
                                )
                                findNavController().navigateUp()
                            } else {
                                showAlert("Please enter valid location")
                            }
                        } else {
                            jobSeekerViewModel.setJobSearchFilterData(
                                JobSearchFilterModel(
                                    companyName = commaSeparatedCompanyNameString.replace(
                                        "[",
                                        ""
                                    )
                                        .replace("]", "").replace("'", ""),
                                    location = "",
                                    yearsOfExperience = selectedYearsOfExperience,
                                    jobType = selectedJobType,
                                    industries = commaSeparatedIndustryString.replace("[", "")
                                        .replace("]", "").replace("'", "")
                                )
                            )
                            findNavController().navigateUp()
                        }
                    }
                }
            }

            resetTv.setOnClickListener {
                companyNameChipGroup.removeAllViews()
                industriesChipGroup.removeAllViews()

                companyNameList = mutableListOf()
                industriesList = mutableListOf()

                locationEt.setText("")

                jobExperienceAdapter?.setSelectedExperienceData("")

                jobTypeAdapter?.setData(
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

                selectedJobType = ""
                selectedYearsOfExperience = ""

                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "",
                        location = "",
                        yearsOfExperience = "",
                        jobType = "",
                        industries = ""
                    )
                )
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

            jobSeekerViewModel.selectedJobList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    selectedJobType = ""
                    it.forEach { item ->
                        if (item.isSelected) {
                            if (!selectedJobType.contains(item.name)) {
                                selectedJobType += item.name + ","
                            }
                        }
                    }
                    selectedJobType = selectedJobType.dropLast(1)
                }
            }


            jobSeekerViewModel.jobSearchFilterData.observe(viewLifecycleOwner) { filterData ->
                if (filterData.companyName.isNotEmpty()) {
                    filterData.companyName.split(",").toTypedArray().forEach {
                        addNewChip(it, "companyNameEt", companyNameChipGroup)
                    }
                }

                if (filterData.industries.isNotEmpty()) {
                    filterData.industries.split(",").toTypedArray().forEach {
                        addNewChip(it, "industriesEt", industriesChipGroup)
                    }
                }

                if (filterData.location.isNotEmpty()) {
                    locationEt.setText(filterData.location)
                }

                if (filterData.yearsOfExperience.isNotEmpty()) {
                    selectedYearsOfExperience = filterData.yearsOfExperience
                    jobExperienceAdapter?.setSelectedExperienceData(filterData.yearsOfExperience)
                }

                if (filterData.jobType.isNotEmpty()) {
                    selectedJobType = filterData.jobType

                    val jobTypeList = filterData.jobType.split(",").toTypedArray()

                    tempJobList.forEachIndexed { index, jobType ->
                        if (jobTypeList.contains(jobType.name)) {
                            tempJobList[index].isSelected = true
                        }
                    }

                    jobSeekerViewModel.setSelectJobListMutableValue(tempJobList)
                }
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

            jobTypeAdapter?.setData(tempJobList)


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

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}