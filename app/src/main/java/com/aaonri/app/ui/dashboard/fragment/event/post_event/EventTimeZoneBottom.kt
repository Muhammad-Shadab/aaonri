package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventTimeZoneBottomBinding
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventTimeZoneAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventTimeZoneBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentEventTimeZoneBottomBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var eventTimeZoneAdapter: EventTimeZoneAdapter? = null

    var timeZoneList = listOf(
        "CST",
        "EST",
        "MST",
        "PST"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventTimeZoneBottomBinding.inflate(inflater, container, false)

        eventTimeZoneAdapter = EventTimeZoneAdapter {
            postEventViewModel.setEventTimeZone(it)
            dismiss()
        }

        binding?.apply {
            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            categoriesRv.layoutManager = LinearLayoutManager(context)
            eventTimeZoneAdapter?.setData(timeZoneList)
            categoriesRv.adapter = eventTimeZoneAdapter
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}