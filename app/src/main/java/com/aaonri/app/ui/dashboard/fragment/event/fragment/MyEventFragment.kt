package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.event.adapter.AllEventAdapter
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentAllEventBinding
import com.aaonri.app.databinding.FragmentMyEventBinding
import com.aaonri.app.ui.dashboard.fragment.event.EventScreenActivity
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEventFragment : Fragment() {
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var myEventBinding: FragmentMyEventBinding? = null
    var allEventAdapter: AllEventAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myEventBinding = FragmentMyEventBinding.inflate(inflater, container, false)

        allEventAdapter = AllEventAdapter {
            postEventViewModel.setSendDataToClassifiedDetailsScreen(it.id)
            postEventViewModel.setNavigateToEventDetailScreen(value = true)
        }

        myEventBinding?.apply {

            postEventBtn.setOnClickListener {
                context?.let { PreferenceManager<String>(it) }
                    ?.set("description", "")

                val intent = Intent(requireContext(), EventScreenActivity::class.java)
                startActivityForResult(intent, 2)
            }

            recyclerViewMyEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewMyEvent.adapter = allEventAdapter

        }

        eventViewModel.myEvent.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    myEventBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    myEventBinding?.progressBar?.visibility = View.GONE
                    if (response.data?.eventList?.isEmpty() == true) {
                        eventViewModel.setHideFloatingBtn(true)
                        myEventBinding?.nestedScrollView?.visibility = View.VISIBLE
                        myEventBinding?.recyclerViewMyEvent?.visibility = View.GONE
                    } else {
                        myEventBinding?.recyclerViewMyEvent?.visibility = View.VISIBLE
                        eventViewModel.setHideFloatingBtn(false)
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
}