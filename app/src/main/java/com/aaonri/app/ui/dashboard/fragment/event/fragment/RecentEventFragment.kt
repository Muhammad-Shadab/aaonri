package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.event.adapter.RecentEventAdapter
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentRecentEventBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentEventFragment : Fragment() {
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var recentEventBinding: FragmentRecentEventBinding? = null
    var recentAdapter: RecentEventAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recentEventBinding = FragmentRecentEventBinding.inflate(inflater, container, false)

        recentAdapter = RecentEventAdapter {
            postEventViewModel.setSendDataToClassifiedDetailsScreen(it.id)
            postEventViewModel.setNavigateToEventDetailScreen(value = true)
        }

        recentEventBinding?.apply {

            recyclerViewMyEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewMyEvent.adapter = recentAdapter

        }

        eventViewModel.recentEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    recentEventBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    recentEventBinding?.progressBar?.visibility = View.GONE
                    if (response.data?.isEmpty() == true) {
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "No result found", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    } else {
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