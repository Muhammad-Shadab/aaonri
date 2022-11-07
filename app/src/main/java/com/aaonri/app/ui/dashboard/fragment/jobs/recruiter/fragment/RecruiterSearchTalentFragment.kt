package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterSearchBinding
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecruiterSearchTalentFragment : Fragment() {
    var binding: FragmentRecruiterSearchBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()


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


            //chipsInput.addChip("shadab","")


            edit.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    addNewChip(textView.text.toString(), flex)
                    edit.setText("")

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


        }

        return binding?.root
    }

    private fun addNewChip(person: String, chipGroup: FlexboxLayout) {
        val chip = Chip(context)
        chip.text = person
        //chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.mipmap.ic_launcher_round)
        chip.isCloseIconEnabled = true
        chip.isClickable = true
        chip.isCheckable = false
        chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
        chipGroup.addView(chip as View, chipGroup.childCount - 1)
        chip.setOnCloseIconClickListener { chipGroup.removeView(chip as View) }
    }
}