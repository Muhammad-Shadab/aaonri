package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.adapter.AllEventAdapter
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentMyEventBinding
import com.aaonri.app.ui.dashboard.fragment.event.EventScreenActivity
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEventFragment : Fragment() {
    var binding: FragmentMyEventBinding? = null
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var allEventAdapter: AllEventAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyEventBinding.inflate(inflater, container, false)

        val keyword =
            context?.let { PreferenceManager<String>(it)[EventConstants.SEARCH_KEYWORD_FILTER, ""] }

        allEventAdapter = AllEventAdapter {
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

            postEventBtn.setOnClickListener {
                val intent = Intent(requireContext(), EventScreenActivity::class.java)
                startActivityForResult(intent, 2)
            }

            recyclerViewMyEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewMyEvent.adapter = allEventAdapter

            adsGenericAdapter1?.items = ActiveAdvertiseStaticData.getEventTopBannerAds()

            adsGenericAdapter2?.items = ActiveAdvertiseStaticData.getEventBottomAds()

            topAdvertiseRv.adapter = adsGenericAdapter1
            topAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            bottomAdvertiseRv.adapter = adsGenericAdapter2
            bottomAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }


        eventViewModel.myEvent.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    if (response.data?.eventList?.isEmpty() == true) {
                        eventViewModel.setHideFloatingBtn(true)
                        if (keyword?.isNotEmpty() == true) {
                            binding?.nestedScrollView?.visibility = View.GONE
                            binding?.recyclerViewMyEvent?.visibility = View.GONE
                            binding?.topAdvertiseRv?.visibility = View.GONE
                            binding?.bottomAdvertiseRv?.visibility = View.GONE
                            binding?.noResultFound?.visibility = View.VISIBLE
                            /* activity?.let { it1 ->
                                 Snackbar.make(
                                     it1.findViewById(android.R.id.content),
                                     "No result found", Snackbar.LENGTH_LONG
                                 ).show()
                             }*/
                        } else {
                            binding?.noResultFound?.visibility = View.GONE
                            binding?.nestedScrollView?.visibility = View.VISIBLE
                            binding?.recyclerViewMyEvent?.visibility = View.GONE
                            binding?.topAdvertiseRv?.visibility = View.GONE
                            binding?.bottomAdvertiseRv?.visibility = View.GONE
                        }
                    } else {
                        binding?.noResultFound?.visibility = View.GONE
                        binding?.recyclerViewMyEvent?.visibility = View.VISIBLE
                        binding?.topAdvertiseRv?.visibility = View.VISIBLE
                        binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                        eventViewModel.setHideFloatingBtn(false)
                        binding?.nestedScrollView?.visibility = View.GONE
                        allEventAdapter?.setData(response.data?.eventList)
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