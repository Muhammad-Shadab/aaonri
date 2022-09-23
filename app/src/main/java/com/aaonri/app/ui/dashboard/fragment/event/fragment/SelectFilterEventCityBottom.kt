package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventCityAdapter
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.databinding.FragmentSelectFilterEventCityBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectFilterEventCityBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentSelectFilterEventCityBottomBinding? = null
    val eventViewModel: EventViewModel by activityViewModels()
    var eventCityAdapter: EventCityAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            FragmentSelectFilterEventCityBottomBinding.inflate(inflater, container, false)

        eventCityAdapter = EventCityAdapter {
            eventViewModel.setSelectedEventLocation(it)
            dismiss()
        }

        binding?.apply {

            closeCountryBtn.setOnClickListener {
                dismiss()
            }
            selectCityTv.setOnClickListener {

            }
            cityRv.layoutManager = LinearLayoutManager(context)
            cityRv.adapter = eventCityAdapter
        }

        eventCityAdapter?.setData(eventViewModel.eventCityList)

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}