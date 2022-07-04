package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.event.adapter.AllEventAdapter
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.databinding.FragmentMyEventBinding
import com.aaonri.app.databinding.FragmentRecentEventBinding
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentEventFragment : Fragment() {
    val eventViewModel: EventViewModel by viewModels()
    var recentEventBinding: FragmentRecentEventBinding? = null
    var allEventAdapter: AllEventAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recentEventBinding = FragmentRecentEventBinding.inflate(inflater, container, false)

        allEventAdapter = AllEventAdapter {

        }

        eventViewModel.getRecentEvent("saifshadab08@gmail.com")

        recentEventBinding?.apply {

            recyclerViewMyEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewMyEvent.adapter = allEventAdapter

        }

        eventViewModel.allEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    recentEventBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    recentEventBinding?.progressBar?.visibility = View.GONE
                    allEventAdapter?.setData(response.data?.eventList)
                }
                is Resource.Error -> {
                    recentEventBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        return recentEventBinding?.root

    }

}