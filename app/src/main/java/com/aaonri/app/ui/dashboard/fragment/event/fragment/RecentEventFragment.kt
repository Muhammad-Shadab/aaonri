package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.event.adapter.RecentEventAdapter
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentRecentEventBinding
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentEventFragment : Fragment() {
    var binding: FragmentRecentEventBinding? = null
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var recentAdapter: RecentEventAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentEventBinding.inflate(inflater, container, false)

        recentAdapter = RecentEventAdapter {
            postEventViewModel.setSendDataToEventDetailsScreen(it.id)
            postEventViewModel.setNavigateToEventDetailScreen(value = true)
        }

        adsGenericAdapter1 = AdsGenericAdapter()
        adsGenericAdapter2 = AdsGenericAdapter()

        adsGenericAdapter2?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {
                eventViewModel.setNavigateFromEventScreenToAdvertiseWebView(true)
                eventViewModel.setEventAdvertiseUrls(item.advertisementDetails.url)
            }
        }

        adsGenericAdapter1?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {
                eventViewModel.setNavigateFromEventScreenToAdvertiseWebView(true)
                eventViewModel.setEventAdvertiseUrls(item.advertisementDetails.url)
            }
        }

        binding?.apply {

            recyclerViewMyEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewMyEvent.adapter = recentAdapter

            adsGenericAdapter1?.items = ActiveAdvertiseStaticData.getEventTopBannerAds()

            adsGenericAdapter2?.items = ActiveAdvertiseStaticData.getEventBottomAds()

            topAdvertiseRv.adapter = adsGenericAdapter1
            topAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            bottomAdvertiseRv.adapter = adsGenericAdapter2
            bottomAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }

        eventViewModel.recentEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    if (response.data?.isEmpty() == true) {
                        binding?.topAdvertiseRv?.visibility = View.GONE
                        binding?.bottomAdvertiseRv?.visibility = View.GONE
                        binding?.recyclerViewMyEvent?.visibility = View.GONE
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "No result found", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        binding?.topAdvertiseRv?.visibility = View.VISIBLE
                        binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                        binding?.recyclerViewMyEvent?.visibility = View.VISIBLE
                        response.data?.let { recentAdapter?.setData(it) }
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        return binding?.root

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}