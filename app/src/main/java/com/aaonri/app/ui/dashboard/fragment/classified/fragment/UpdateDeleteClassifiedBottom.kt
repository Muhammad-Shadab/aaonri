package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentUpdateDeleteClassifiedBottomBinding
import com.aaonri.app.ui.dashboard.fragment.classified.ClassifiedActivity
import com.aaonri.app.ui.dashboard.fragment.event.EventScreenActivity
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UpdateDeleteClassifiedBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var updateDeleteBinding: FragmentUpdateDeleteClassifiedBottomBinding? = null
    val args: UpdateDeleteClassifiedBottomArgs by navArgs()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateDeleteBinding =
            FragmentUpdateDeleteClassifiedBottomBinding.inflate(inflater, container, false)

        updateDeleteBinding?.apply {

            updateClassified.setOnClickListener {

                if (args.isClassifiedUpdate) {
                    val intent = Intent(requireContext(), ClassifiedActivity::class.java)
                    intent.putExtra("updateClassified", true)
                    intent.putExtra("addId", args.addId)
                    startActivityForResult(intent, 1)
                } else {
                    val intent = Intent(requireContext(), EventScreenActivity::class.java)
                    intent.putExtra("updateEvent", true)
                    intent.putExtra("eventId", args.addId)
                    startActivityForResult(intent, 2)
                }
                dismiss()
            }

            deleteClassified.setOnClickListener {
                if (args.isClassifiedUpdate) {
                    //postClassifiedViewModel.deleteClassified(args.addId)
                } else {
                    postEventViewModel.deleteEvent(args.addId)
                }
            }
        }

        postClassifiedViewModel.classifiedDeleteData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    /*dismiss()
                    findNavController().navigateUp()*/
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        postEventViewModel.deleteEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    dismiss()
                    findNavController().navigateUp()
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        return updateDeleteBinding?.root
    }
}