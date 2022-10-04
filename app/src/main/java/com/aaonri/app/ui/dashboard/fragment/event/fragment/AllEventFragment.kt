package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentAllEventBinding
import com.aaonri.app.ui.dashboard.fragment.event.adapter.AllEventAdapter
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AllEventFragment : Fragment() {
    var binding: FragmentAllEventBinding? = null
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var allEventAdapter: AllEventAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
    var adRvposition1 = 0
    var adRvposition2 = 0
    var timer1: Timer? = null
    var timerTask1: TimerTask? = null
    var timer2: Timer? = null
    var timerTask2: TimerTask? = null
    private lateinit var layoutManager2: LinearLayoutManager
    private lateinit var layoutManager1: LinearLayoutManager
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
            if (ActiveAdvertiseStaticData.getEventTopBannerAds().isNotEmpty()) {
                topAdvertiseRv.adapter = adsGenericAdapter1
                layoutManager1 =
                    GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                topAdvertiseRv.layoutManager = layoutManager1
                topAdvertiseRv.addItemDecoration(
                    GridSpacingItemDecoration(
                        2,
                        32, 0
                    )
                )
            }
            if (ActiveAdvertiseStaticData.getEventBottomAds().isNotEmpty()) {

                bottomAdvertiseRv.adapter = adsGenericAdapter2
                layoutManager2 = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                bottomAdvertiseRv.layoutManager = layoutManager2
                bottomAdvertiseRv.addItemDecoration(
                    GridSpacingItemDecoration(
                        2,
                        32, 0
                    )
                )
            }
            bottomAdvertiseRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == 1) {
                        stopAutoScrollBanner2()
                    } else if (newState == 0) {

                        adRvposition2 = layoutManager2.findFirstCompletelyVisibleItemPosition()
                        runAutoScrollBanner2()
                    }
                }
            })

            nestedScrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
                override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            })

            topAdvertiseRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == 1) {
                        stopAutoScrollBanner1()
                    } else if (newState == 0) {

                        adRvposition1 = layoutManager1.findFirstCompletelyVisibleItemPosition()
                        runAutoScrollBanner1()
                    }
                }
            })

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

                        if (eventViewModel.isEventCityListIsEmpty) {
                            response.data.eventList.forEach {
                                if (!listOfCity.contains(it.city) && !it.city.isNullOrEmpty()) {
                                    listOfCity.add(it.city)
                                }
                            }
                            if (eventViewModel.eventCityList.isEmpty()) {
                                eventViewModel.setEventCityList(listOfCity)
                            }
                            eventViewModel.isEventCityListIsEmpty = false
                        }
                        binding?.recyclerViewEvent?.visibility = View.VISIBLE
                        binding?.topAdvertiseRv?.visibility = View.VISIBLE
                        binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                        binding?.resultsNotFoundLL?.visibility = View.GONE
                    } else {
                        binding?.recyclerViewEvent?.visibility = View.GONE
                        binding?.topAdvertiseRv?.visibility = View.GONE
                        binding?.bottomAdvertiseRv?.visibility = View.GONE
                        binding?.resultsNotFoundLL?.visibility = View.VISIBLE
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
                binding?.resultsNotFoundLL?.visibility = View.GONE
                //allEventAdapter?.setData(eventViewModel.allEventList)
            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoScrollBanner1()
        stopAutoScrollBanner2()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onPause() {
        super.onPause()
        stopAutoScrollBanner2()
        stopAutoScrollBanner1()
    }

    fun stopAutoScrollBanner1() {
        if (timer1 != null && timerTask1 != null) {
            timerTask1!!.cancel()
            timer1!!.cancel()
            timer1 = null
            timerTask1 = null
            adRvposition1 = layoutManager1.findFirstCompletelyVisibleItemPosition()
        }
    }

    fun runAutoScrollBanner1() {

        if (timer1 == null && timerTask1 == null && adsGenericAdapter1?.items?.size!! >= 3) {
            timer1 = Timer()
            timerTask1 = object : TimerTask() {

                override fun run() {

                    if (adRvposition1 == Int.MAX_VALUE) {
                        adRvposition1 = Int.MAX_VALUE / 2
                        binding?.topAdvertiseRv?.smoothScrollToPosition(adRvposition1)

                    } else {
                        adRvposition1 += 2
                        binding?.topAdvertiseRv?.smoothScrollToPosition(adRvposition1)
                    }
                }
            }
            timer1!!.schedule(timerTask1, 4000, 4000)
        }


    }

    fun stopAutoScrollBanner2() {
        if (timer2 != null && timerTask2 != null) {
            timerTask2!!.cancel()
            timer2!!.cancel()
            timer2 = null
            timerTask2 = null
            adRvposition2 = layoutManager2.findFirstCompletelyVisibleItemPosition()
        }
    }

    fun runAutoScrollBanner2() {
        if (timer2 == null && timerTask2 == null && adsGenericAdapter2?.items?.size!! >= 3) {
            timer2 = Timer()
            timerTask2 = object : TimerTask() {

                override fun run() {

                    if (adRvposition2 == Int.MAX_VALUE) {
                        adRvposition2 = Int.MAX_VALUE / 2
                        binding?.bottomAdvertiseRv?.smoothScrollToPosition(adRvposition2)

                    } else {
                        adRvposition2 += 2
                        binding?.bottomAdvertiseRv?.smoothScrollToPosition(adRvposition2)
                    }
                }
            }
            timer2!!.schedule(timerTask2, 3000, 4000)
        }


    }
}