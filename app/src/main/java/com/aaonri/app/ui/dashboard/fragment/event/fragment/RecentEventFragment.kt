package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.event.adapter.RecentEventAdapter
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.HomeRecyclerViewAdapter
import com.aaonri.app.databinding.FragmentRecentEventBinding
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentEventFragment : Fragment() {
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var recentEventBinding: FragmentRecentEventBinding? = null
    var recentAdapter: RecentEventAdapter? = null
    var homeRecyclerViewAdapter1: HomeRecyclerViewAdapter? = null
    var homeRecyclerViewAdapter2: HomeRecyclerViewAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recentEventBinding = FragmentRecentEventBinding.inflate(inflater, container, false)

        recentAdapter = RecentEventAdapter {
            postEventViewModel.setSendDataToClassifiedDetailsScreen(it.id)
            postEventViewModel.setNavigateToEventDetailScreen(value = true)
        }

        homeRecyclerViewAdapter1 = HomeRecyclerViewAdapter()
        homeRecyclerViewAdapter2 = HomeRecyclerViewAdapter()

        recentEventBinding?.apply {

            recyclerViewMyEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewMyEvent.adapter = recentAdapter

            homeRecyclerViewAdapter1?.items = ActiveAdvertiseStaticData.getEventTopBanner()

            homeRecyclerViewAdapter2?.items =
                ActiveAdvertiseStaticData.getEventJustAboveFooterImageOnly() + ActiveAdvertiseStaticData.getEventJustAboveBottomTabBOTH() + ActiveAdvertiseStaticData.getEventJustAboveFooterTextOnly()

            topAdvertiseRv.adapter = homeRecyclerViewAdapter1
            topAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            bottomAdvertiseRv.adapter = homeRecyclerViewAdapter2
            bottomAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }

        eventViewModel.recentEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    recentEventBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    recentEventBinding?.progressBar?.visibility = View.GONE
                    if (response.data?.isEmpty() == true) {
                        recentEventBinding?.topAdvertiseRv?.visibility = View.GONE
                        recentEventBinding?.bottomAdvertiseRv?.visibility = View.GONE
                        recentEventBinding?.recyclerViewMyEvent?.visibility = View.GONE
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "No result found", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        recentEventBinding?.topAdvertiseRv?.visibility = View.VISIBLE
                        recentEventBinding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                        recentEventBinding?.recyclerViewMyEvent?.visibility = View.VISIBLE
                        response.data?.let { recentAdapter?.setData(it) }
                    }
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