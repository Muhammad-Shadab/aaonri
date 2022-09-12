package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var myEventBinding: FragmentMyEventBinding? = null
    var allEventAdapter: AllEventAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myEventBinding = FragmentMyEventBinding.inflate(inflater, container, false)

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

        myEventBinding?.apply {

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
                    myEventBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    myEventBinding?.progressBar?.visibility = View.GONE
                    if (response.data?.eventList?.isEmpty() == true) {
                        eventViewModel.setHideFloatingBtn(true)
                        if (keyword?.isNotEmpty() == true) {
                            myEventBinding?.nestedScrollView?.visibility = View.GONE
                            myEventBinding?.recyclerViewMyEvent?.visibility = View.GONE
                            myEventBinding?.topAdvertiseRv?.visibility = View.GONE
                            myEventBinding?.bottomAdvertiseRv?.visibility = View.GONE
                            myEventBinding?.noResultFound?.visibility = View.VISIBLE
                            /* activity?.let { it1 ->
                                 Snackbar.make(
                                     it1.findViewById(android.R.id.content),
                                     "No result found", Snackbar.LENGTH_LONG
                                 ).show()
                             }*/
                        } else {
                            myEventBinding?.noResultFound?.visibility = View.GONE
                            myEventBinding?.nestedScrollView?.visibility = View.VISIBLE
                            myEventBinding?.recyclerViewMyEvent?.visibility = View.GONE
                            myEventBinding?.topAdvertiseRv?.visibility = View.GONE
                            myEventBinding?.bottomAdvertiseRv?.visibility = View.GONE
                        }
                    } else {
                        myEventBinding?.noResultFound?.visibility = View.GONE
                        myEventBinding?.recyclerViewMyEvent?.visibility = View.VISIBLE
                        myEventBinding?.topAdvertiseRv?.visibility = View.VISIBLE
                        myEventBinding?.bottomAdvertiseRv?.visibility = View.VISIBLE
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