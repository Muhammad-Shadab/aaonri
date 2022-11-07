package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.text.Spanned
import android.text.style.ImageSpan
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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
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

            val chipDrawable = context?.let { ChipDrawable.createFromResource(it, R.xml.chip) }

            chipDrawable?.setBounds(0, 0, chipDrawable.intrinsicWidth, chipDrawable.intrinsicHeight)
            val span = chipDrawable?.let { ImageSpan(it) }
            val text = locationEt.text!!

            text.setSpan(span, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

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



}