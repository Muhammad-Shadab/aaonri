package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentAvailabilityBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.AllAvailabilityAdapter
import com.aaonri.app.utils.CustomDialog
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailabilityBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentAvailabilityBottomSheetBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var allAvailabilityAdapter: AllAvailabilityAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAvailabilityBottomSheetBinding.inflate(layoutInflater, container, false)


        allAvailabilityAdapter = AllAvailabilityAdapter {
            jobRecruiterViewModel.setSelectedAvailability(it.availability)
            dismiss()
        }

        binding?.apply {

            categoriesRv.layoutManager = LinearLayoutManager(context)
            categoriesRv.adapter = allAvailabilityAdapter

            closeBtn.setOnClickListener {
                dismiss()
            }

            jobRecruiterViewModel.allAvailabilityData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        response.data?.let { allAvailabilityAdapter?.setData(it) }
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                    }
                }
            }
        }

        return binding?.root
    }

}