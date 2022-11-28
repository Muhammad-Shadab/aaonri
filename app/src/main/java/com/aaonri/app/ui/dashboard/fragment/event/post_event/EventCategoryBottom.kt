package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.event.EventStaticData
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventCategoryBottomBinding
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventCategoryAdapter
import com.aaonri.app.utils.CustomDialog
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventCategoryBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentEventCategoryBottomBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var eventCategoryAdapter: EventCategoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventCategoryBottomBinding.inflate(inflater, container, false)
        isCancelable = false

        eventCategoryAdapter = EventCategoryAdapter {
            postEventViewModel.setSelectedEventCategory(it)
            dismiss()
        }

        binding?.apply {
            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            categoriesRv.layoutManager = LinearLayoutManager(context)
            categoriesRv.adapter = eventCategoryAdapter
        }

        if (EventStaticData.getEventCategory().isNotEmpty()) {
            eventCategoryAdapter?.setData(EventStaticData.getEventCategory())
        } else {
            postEventViewModel.eventCategoryData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        eventCategoryAdapter?.setData(response.data)
                        response.data?.let { EventStaticData.updateEventCategory(it) }
                        CustomDialog.hideLoader()
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                    }
                }
            }
        }

        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}