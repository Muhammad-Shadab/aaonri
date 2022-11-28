package com.aaonri.app.ui.dashboard.fragment.classified.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentMyClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MyClassifiedFragment : Fragment() {
    var binding: FragmentMyClassifiedBinding? = null
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
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
            FragmentMyClassifiedBinding.inflate(inflater, container, false)

        allClassifiedAdapter = AllClassifiedAdapter {
            postClassifiedViewModel.setSendDataToClassifiedDetailsScreen(it.id)
            postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(
                value = true,
                isMyClassifiedScreen = true
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

            nestedScrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
                override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            })

            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 36, 40))

            adsGenericAdapter1?.items = ActiveAdvertiseStaticData.getClassifiedTopBannerAds()

            adsGenericAdapter2?.items = ActiveAdvertiseStaticData.getClassifiedBottomAds()

            topAdvertiseRv.adapter = adsGenericAdapter1
            layoutManager1 = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
            topAdvertiseRv.layoutManager = layoutManager1
            topAdvertiseRv.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    32, 0
                )
            )

            bottomAdvertiseRv.adapter = adsGenericAdapter2
            layoutManager2 = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
            bottomAdvertiseRv.layoutManager = layoutManager2
            bottomAdvertiseRv.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    32, 0
                )
            )
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

            classifiedViewModel.classifiedContentScrollToTop.observe(viewLifecycleOwner) {
                if (it) {
                    nestedScrollView.post {
                        nestedScrollView.fling(0)
                        nestedScrollView.smoothScrollTo(0, 0)
                    }
                    classifiedViewModel.setClassifiedContentScrollToTop(false)
                }
            }

        }

        classifiedViewModel.myClassified.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    CustomDialog.showLoader(requireActivity())
                }
                is Resource.Success -> {
                    CustomDialog.hideLoader()
                    if (response.data?.userAdsList?.isNotEmpty() == true) {
                        binding?.recyclerViewClassified?.visibility = View.VISIBLE
                        binding?.topAdvertiseRv?.visibility = View.VISIBLE
                        binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                        binding?.emptyClassifiedImage?.visibility = View.GONE
                        binding?.emptyTextVew?.visibility = View.GONE
                        response.data.userAdsList.let { allClassifiedAdapter?.setData(it) }
                        binding?.recyclerViewClassified?.adapter =
                            allClassifiedAdapter
                    } else {
                        binding?.emptyClassifiedImage?.visibility = View.VISIBLE
                        binding?.emptyTextVew?.visibility = View.VISIBLE
                        binding?.recyclerViewClassified?.visibility = View.GONE
                        binding?.topAdvertiseRv?.visibility = View.GONE
                        binding?.bottomAdvertiseRv?.visibility = View.GONE
                    }

                }
                is Resource.Error -> {
                    CustomDialog.hideLoader()
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }


        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        arguments?.let { bundle ->
            val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
            if (bundle.getBoolean("filterEnabled")) {
                var minValue =
                    context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MIN_VALUE_FILTER, "0"] }
                var maxValue =
                    context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MAX_VALUE_FILTER, "0"] }
                val zipCodeFilter =
                    context?.let { PreferenceManager<String>(it)[ClassifiedConstant.ZIPCODE_FILTER, ""] }

                if (minValue?.isEmpty() == true) {
                    minValue = "0"
                }

                if (maxValue?.isEmpty() == true) {
                    maxValue = "0"
                }

                classifiedViewModel.getClassifiedByUser(
                    GetClassifiedByUserRequest(
                        category = "",
                        email = if (email?.isNotEmpty() == true) email else "",
                        fetchCatSubCat = true,
                        keywords = "",
                        location = "",
                        maxPrice = maxValue?.toInt(),
                        minPrice = minValue?.toInt(),
                        myAdsOnly = true,
                        popularOnAoonri = null,
                        subCategory = "",
                        zipCode = zipCodeFilter
                    )
                )
            } else {

            }
        }
        runAutoScrollBanner2()
        runAutoScrollBanner1()
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

        if (timer1 == null && timerTask1 == null) {
            timer1 = Timer()
            timerTask1 = object : TimerTask() {

                override fun run() {

                    if (adRvposition1 == Int.MAX_VALUE) {
                        adRvposition1 = Int.MAX_VALUE / 2
                        binding?.topAdvertiseRv?.smoothScrollToPosition(adRvposition1)

                    } else {
                        adRvposition1 += 3
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
        if (timer2 == null && timerTask2 == null) {
            timer2 = Timer()
            timerTask2 = object : TimerTask() {

                override fun run() {

                    if (adRvposition2 == Int.MAX_VALUE) {
                        adRvposition2 = Int.MAX_VALUE / 2
                        binding?.bottomAdvertiseRv?.smoothScrollToPosition(adRvposition2)

                    } else {
                        adRvposition2 += 3
                        binding?.bottomAdvertiseRv?.smoothScrollToPosition(adRvposition2)
                    }
                }
            }
            timer2!!.schedule(timerTask2, 3000, 4000)
        }


    }
}

