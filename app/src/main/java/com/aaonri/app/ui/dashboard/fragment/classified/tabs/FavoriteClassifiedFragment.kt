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
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentFavoriteClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.FavoriteClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class
FavoriteClassifiedFragment : Fragment() {
    var binding: FragmentFavoriteClassifiedBinding? = null
    var favoriteClassifiedAdapter: FavoriteClassifiedAdapter? = null
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
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
            FragmentFavoriteClassifiedBinding.inflate(inflater, container, false)

        favoriteClassifiedAdapter = FavoriteClassifiedAdapter {
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

            loginBtn.setOnClickListener {
                postClassifiedViewModel.setNavigateToAllClassified(true)
            }

            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 36, 40))

            adsGenericAdapter1?.items = ActiveAdvertiseStaticData.getClassifiedTopBannerAds()

            adsGenericAdapter2?.items = ActiveAdvertiseStaticData.getClassifiedBottomAds()

            topAdvertiseRv.adapter = adsGenericAdapter1
            layoutManager1 =  LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            topAdvertiseRv.layoutManager = layoutManager1


            bottomAdvertiseRv.adapter = adsGenericAdapter2
            layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            bottomAdvertiseRv.layoutManager = layoutManager2

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


        classifiedViewModel.favoriteClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE

                    if (response.data?.classifieds?.isNotEmpty() == true) {
                        binding?.nestedScrollView?.visibility = View.GONE
                        binding?.recyclerViewClassified?.visibility = View.VISIBLE
                        binding?.topAdvertiseRv?.visibility = View.VISIBLE
                        binding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                        response.data.classifieds.let { favoriteClassifiedAdapter!!.setData(it) }
                    } else {
                        binding?.recyclerViewClassified?.visibility = View.GONE
                        binding?.topAdvertiseRv?.visibility = View.GONE
                        binding?.bottomAdvertiseRv?.visibility = View.GONE
                        binding?.nestedScrollView?.visibility = View.VISIBLE
                    }

                    binding?.recyclerViewClassified?.adapter =
                        favoriteClassifiedAdapter
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

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        classifiedViewModel.isLikedButtonClicked.observe(viewLifecycleOwner) { isLikeButtonClicked ->
            if (isLikeButtonClicked) {
                if (email != null) {
                    classifiedViewModel.getFavoriteClassified(email)
                }
                classifiedViewModel.setIsLikedButtonClicked(false)
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
        if (timer2 == null && timerTask2 == null) {
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

