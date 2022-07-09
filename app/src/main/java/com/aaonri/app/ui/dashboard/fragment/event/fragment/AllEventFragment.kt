package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.event.adapter.AllEventAdapter
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentAllEventBinding
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllEventFragment : Fragment() {
    val eventViewModel: EventViewModel by viewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var allEventBinding: FragmentAllEventBinding? = null
    var allEventAdapter: AllEventAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allEventBinding =
            FragmentAllEventBinding.inflate(inflater, container, false)

        allEventAdapter = AllEventAdapter {
            postEventViewModel.setSendDataToClassifiedDetailsScreen(it.id)
            postEventViewModel.setNavigateToClassifiedDetailsScreen(true)
        }

        allEventBinding?.apply {

            recyclerViewEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewEvent.adapter = allEventAdapter

        }

        eventViewModel.allEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    allEventBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    allEventBinding?.progressBar?.visibility = View.GONE
                    allEventAdapter?.setData(response.data?.eventList)
                }
                is Resource.Error -> {
                    allEventBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }

        }

        return allEventBinding?.root
    }

    override fun onResume() {
        super.onResume()

        eventViewModel.getAllEvent(
            AllEventRequest(
                category = "",
                city = "",
                from = "",
                isPaid = "",
                keyword = "",
                maxEntryFee = 0,
                minEntryFee = 0,
                myEventsOnly = false,
                userId = "",
                zip = ""
            )
        )
    }

}