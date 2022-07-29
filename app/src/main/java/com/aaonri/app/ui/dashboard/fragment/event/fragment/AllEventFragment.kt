package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllEventFragment : Fragment() {
    val eventViewModel: EventViewModel by activityViewModels()
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
            postEventViewModel.setNavigateToEventDetailScreen(
                value = true
            )
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
                    val listOfCity = mutableListOf<String>()
                    if (response.data?.eventList?.isNotEmpty() == true) {

                        if (eventViewModel.isAllSelected) {
                            allEventAdapter?.setData(response.data.eventList)
                        } else if (eventViewModel.isFreeSelected) {
                            allEventAdapter?.setData(response.data.eventList.sortedBy { it.isFree == true })
                        } else if (eventViewModel.isPaidSelected) {
                            allEventAdapter?.setData(response.data.eventList.sortedByDescending { it.isFree != true })
                        } else {
                            allEventAdapter?.setData(response.data.eventList)
                        }
                        if (eventViewModel.eventCityList.isEmpty()) {
                            response.data?.eventList.forEach {
                                if (!listOfCity.contains(it.city) && !it.city.isNullOrEmpty()) {
                                    listOfCity.add(it.city)
                                }
                            }
                            eventViewModel.setEventCityList(listOfCity)
                        }
                        allEventBinding?.recyclerViewEvent?.visibility = View.VISIBLE
                    } else {
                        allEventBinding?.recyclerViewEvent?.visibility = View.GONE
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "No result found", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                is Resource.Error -> {
                    allEventBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }

        }

        eventViewModel.keyClassifiedKeyboardListener.observe(viewLifecycleOwner) {
            if (it) {
                allEventAdapter?.setData(eventViewModel.allEventList)
            }
        }

        return allEventBinding?.root
    }


}