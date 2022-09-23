package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.ui.dashboard.fragment.event.adapter.AllEventAdapter
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentAllEventBinding
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllEventFragment : Fragment() {
    var binding: FragmentAllEventBinding? = null
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var allEventAdapter: AllEventAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentAllEventBinding.inflate(inflater, container, false)

        allEventAdapter = AllEventAdapter {
            postEventViewModel.setSendDataToEventDetailsScreen(it.id)
            postEventViewModel.setNavigateToEventDetailScreen(
                value = true
            )
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

            recyclerViewEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewEvent.adapter = allEventAdapter

            adsGenericAdapter1?.items = ActiveAdvertiseStaticData.getEventTopBannerAds()

            adsGenericAdapter2?.items = ActiveAdvertiseStaticData.getEventBottomAds()

            topAdvertiseRv.adapter = adsGenericAdapter1
            topAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            bottomAdvertiseRv.adapter = adsGenericAdapter2
            bottomAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }

        eventViewModel.allEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    val listOfCity = mutableListOf<String>()
                    if (response.data?.eventList?.isNotEmpty() == true) {
                        if (eventViewModel.isAllSelected) {
                            allEventAdapter?.setData(response.data.eventList)
                        } else if (eventViewModel.isFreeSelected) {
                            allEventAdapter?.setData(response.data.eventList.filter { it.fee <= 0 })
                        } else if (eventViewModel.isPaidSelected) {
                            allEventAdapter?.setData(response.data.eventList.filter { it.fee > 0 })
                        } else {
                            allEventAdapter?.setData(response.data.eventList)
                        }
                        response.data.eventList.forEach {
                            if (!listOfCity.contains(it.city) && !it.city.isNullOrEmpty()) {
                                listOfCity.add(it.city)
                            }
                        }
                        if (eventViewModel.eventCityList.isEmpty()) {
                            eventViewModel.setEventCityList(listOfCity)
                        }
                        binding?.recyclerViewEvent?.visibility = View.VISIBLE
                        binding?.topAdvertiseRv?.visibility = View.VISIBLE
                        binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                    } else {
                        binding?.recyclerViewEvent?.visibility = View.GONE
                        binding?.topAdvertiseRv?.visibility = View.GONE
                        binding?.bottomAdvertiseRv?.visibility = View.GONE
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "No result found", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        eventViewModel.keyClassifiedKeyboardListener.observe(viewLifecycleOwner) {
            if (it) {
                binding?.recyclerViewEvent?.visibility = View.VISIBLE
                binding?.topAdvertiseRv?.visibility = View.VISIBLE
                binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                //allEventAdapter?.setData(eventViewModel.allEventList)
            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}