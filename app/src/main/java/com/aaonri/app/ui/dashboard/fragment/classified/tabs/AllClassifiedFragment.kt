package com.aaonri.app.ui.dashboard.fragment.classified.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.*
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentAllClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AllClassifiedFragment : Fragment() {
    var binding: FragmentAllClassifiedBinding? = null
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    private lateinit var layoutManager2: LinearLayoutManager
    private lateinit var layoutManager1: LinearLayoutManager
    var adRvposition1 = 0
    var adRvposition2 = 0
    var timer1: Timer? = null
    var timerTask1: TimerTask? = null
    var timer2: Timer? = null
    var timerTask2: TimerTask? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentAllClassifiedBinding.inflate(inflater, container, false)
        allClassifiedAdapter = AllClassifiedAdapter {
            postClassifiedViewModel.setSendDataToClassifiedDetailsScreen(it.id)
            postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(
                value = true,
                isMyClassifiedScreen = false
            )
        }

        adsGenericAdapter1 = AdsGenericAdapter()
        adsGenericAdapter2 = AdsGenericAdapter()


        adsGenericAdapter2?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {
                classifiedViewModel.setNavigateFromClassifiedScreenToAdvertiseWebView(true)
                classifiedViewModel.setClassifiedAdvertiseUrls(item.advertisementDetails.url)
            }
        }

        adsGenericAdapter1?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {
                classifiedViewModel.setNavigateFromClassifiedScreenToAdvertiseWebView(true)
                classifiedViewModel.setClassifiedAdvertiseUrls(item.advertisementDetails.url)
            }
        }


        binding?.apply {
            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))

            recyclerViewClassified.setHasFixedSize(true)
            recyclerViewClassified.isNestedScrollingEnabled = false
            topAdvertiseRv.isNestedScrollingEnabled = false
            bottomAdvertiseRv.isNestedScrollingEnabled = false

            adsGenericAdapter1?.items = ActiveAdvertiseStaticData.getClassifiedTopBannerAds()

            adsGenericAdapter2?.items = ActiveAdvertiseStaticData.getClassifiedBottomAds()

            topAdvertiseRv.adapter = adsGenericAdapter1
            layoutManager1 =  LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            topAdvertiseRv.layoutManager = layoutManager1


            bottomAdvertiseRv.adapter = adsGenericAdapter2
            layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            bottomAdvertiseRv.layoutManager = layoutManager2


//            if (ActiveAdvertiseStaticData.getClassifiedTopBannerAds() != null) {
//                position = Int.MAX_VALUE / 2-1
//                bottomAdvertiseRv.scrollToPosition(position)
//            }
//
//            val snapHelper: SnapHelper = LinearSnapHelper()
//            snapHelper.attachToRecyclerView(bottomAdvertiseRv)
//            bottomAdvertiseRv.smoothScrollBy(5, 0)

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

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE

                    response.data?.userAdsList?.let { adsList ->
                        if (postClassifiedViewModel.changeSortTriplet.first) {
                            allClassifiedAdapter?.setData(adsList)
                        } else if (postClassifiedViewModel.changeSortTriplet.second) {
                            allClassifiedAdapter?.setData(adsList.sortedBy { it.askingPrice })
                        } else if (postClassifiedViewModel.changeSortTriplet.third) {
                            allClassifiedAdapter?.setData(adsList.sortedByDescending { it.askingPrice })
                        } else {
                            allClassifiedAdapter?.setData(adsList)
                        }
                        //classifiedViewModel.setClassifiedForHomeScreen(adsList)
                        binding?.recyclerViewClassified?.visibility = View.VISIBLE
                        binding?.topAdvertiseRv?.visibility = View.VISIBLE
                        binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                    }
                    binding?.recyclerViewClassified?.adapter = allClassifiedAdapter
                    if (response.data?.userAdsList?.isEmpty() == true) {
                        binding?.recyclerViewClassified?.visibility = View.GONE
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
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        postClassifiedViewModel.keyClassifiedKeyboardListener.observe(viewLifecycleOwner) {
            if (it) {
                binding?.recyclerViewClassified?.visibility = View.VISIBLE
                binding?.topAdvertiseRv?.visibility = View.VISIBLE
                binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                allClassifiedAdapter?.setData(classifiedViewModel.allClassifiedList)
            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoScrollBanner1()
        stopAutoScrollBanner2()
        binding = null
    }




    override fun onResume() {
        super.onResume()
        runAutoScrollBanner2()
        runAutoScrollBanner1()

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
        if (timer1 == null && timerTask1 == null&& adsGenericAdapter1?.items?.size!! >=3) {
            timer1 = Timer()
            timerTask1 = object : TimerTask() {

                override fun run() {

                    if (adRvposition1 == Int.MAX_VALUE) {
                        adRvposition1 = Int.MAX_VALUE / 2
                        binding?.topAdvertiseRv?.scrollToPosition(adRvposition1)

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
        if (timer2 == null && timerTask2 == null&& adsGenericAdapter1?.items?.size!! >=3) {
            timer2 = Timer()
            timerTask2 = object : TimerTask() {

                override fun run() {

                    if (adRvposition2 == Int.MAX_VALUE) {
                        adRvposition2 = Int.MAX_VALUE / 2
                        binding?.bottomAdvertiseRv?.scrollToPosition(adRvposition2)

                    } else {
                        adRvposition2 += 2
                        binding?.bottomAdvertiseRv?.smoothScrollToPosition(adRvposition2)
                    }
                }
            }
            timer2!!.schedule(timerTask2, 4000, 4000)
        }



    }
}

