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
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.databinding.FragmentAllEventBinding
import com.aaonri.app.databinding.FragmentMyEventBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEventFragment : Fragment() {
    val eventViewModel: EventViewModel by viewModels()
    var myEventBinding: FragmentMyEventBinding? = null
    var allEventAdapter: AllEventAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myEventBinding = FragmentMyEventBinding.inflate(inflater, container, false)

        allEventAdapter = AllEventAdapter {

        }

        myEventBinding?.apply {

            recyclerViewMyEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewMyEvent.adapter = allEventAdapter

        }

        eventViewModel.allEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    myEventBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    myEventBinding?.progressBar?.visibility = View.GONE
                    if (response.data?.eventList?.isEmpty() == true) {
                        myEventBinding?.nestedScrollView?.visibility = View.VISIBLE
                    } else {
                        myEventBinding?.nestedScrollView?.visibility = View.GONE
                        allEventAdapter?.setData(response.data?.eventList)
                    }
                }
                is Resource.Error -> {
                    myEventBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        return myEventBinding?.root

    }

    override fun onResume() {
        super.onResume()

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        eventViewModel.getAllEvent(
            AllEventRequest(
                category = "",
                city = "",
                from = "",
                isPaid = "",
                keyword = "",
                maxEntryFee = 0,
                minEntryFee = 0,
                myEventsOnly = true,
                userId = if (email?.isNotEmpty() == true) email else "",
                zip = ""
            )
        )
    }
}