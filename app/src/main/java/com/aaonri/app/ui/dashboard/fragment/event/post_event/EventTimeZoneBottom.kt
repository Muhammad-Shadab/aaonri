package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventTimeZoneBottomBinding
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventCategoryAdapter
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventTimeZoneAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventTimeZoneBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var eventTimeZoneAdapter: EventTimeZoneAdapter? = null
    var eventBinding: FragmentEventTimeZoneBottomBinding? = null

    var timeZoneList = listOf(
        "GMT",
        "UTC",
        "ECT",
        "EET",
        "ART",
        "EAT",
        "MET",
        "NET",
        "PLT",
        "IST",
        "BST",
        "VST",
        "CTT",
        "JST",
        "ACT",
        "AET",
        "SST",
        "NST",
        "MIT",
        "HST",
        "AST",
        "PST",
        "PNT",
        "MST",
        "CST",
        "EST",
        "IET",
        "PRT",
        "CNT",
        "AGT",
        "BET",
        "CAT"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventBinding = FragmentEventTimeZoneBottomBinding.inflate(inflater, container, false)

        eventTimeZoneAdapter = EventTimeZoneAdapter {
            postEventViewModel.setEventTimeZone(it)
            dismiss()
        }

        eventBinding?.apply {
            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            categoriesRv.layoutManager = LinearLayoutManager(context)
            eventTimeZoneAdapter?.setData(timeZoneList)
            categoriesRv.adapter = eventTimeZoneAdapter
        }

        return eventBinding?.root
    }

}