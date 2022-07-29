package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.classified.adapter.ClassifiedSubCategoryAdapter
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.event.adapter.EventCityAdapter
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.ActivityAuthBinding.inflate
import com.aaonri.app.databinding.FragmentSelectClassifiedSubCategoryBottomBinding
import com.aaonri.app.databinding.FragmentSelectFilterEventCityBottomBinding
import com.aaonri.app.ui.dashboard.fragment.classified.fragment.ClassifiedFilterFragmentBottomDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectFilterEventCityBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var selectFilterEventCityBottom: FragmentSelectFilterEventCityBottomBinding? = null
    val eventViewModel: EventViewModel by activityViewModels()
    var eventCityAdapter: EventCityAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        selectFilterEventCityBottom =
            FragmentSelectFilterEventCityBottomBinding.inflate(inflater, container, false)

        eventCityAdapter = EventCityAdapter {
            eventViewModel.setSelectedEventLocation(it)
            dismiss()
        }

        selectFilterEventCityBottom?.apply {

            closeCountryBtn.setOnClickListener {
                dismiss()
            }
            selectCityTv.setOnClickListener {

            }
            cityRv.layoutManager = LinearLayoutManager(context)
            cityRv.adapter = eventCityAdapter
        }


        eventCityAdapter?.setData(eventViewModel.eventCityList)

        return selectFilterEventCityBottom?.root
    }
}