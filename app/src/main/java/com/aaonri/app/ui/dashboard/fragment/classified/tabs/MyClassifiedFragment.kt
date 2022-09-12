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
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentMyClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyClassifiedFragment : Fragment() {
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var myClassifiedBinding: FragmentMyClassifiedBinding? = null
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myClassifiedBinding =
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
        myClassifiedBinding?.apply {
            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 36, 40))

            adsGenericAdapter1?.items = ActiveAdvertiseStaticData.getClassifiedTopBannerAds()

            adsGenericAdapter2?.items = ActiveAdvertiseStaticData.getClassifiedBottomAds()

            topAdvertiseRv.adapter = adsGenericAdapter1
            topAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            bottomAdvertiseRv.adapter = adsGenericAdapter2
            bottomAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        classifiedViewModel.myClassified.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    myClassifiedBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    myClassifiedBinding?.progressBar?.visibility = View.GONE
                    if (response.data?.userAdsList?.isNotEmpty() == true) {
                        myClassifiedBinding?.recyclerViewClassified?.visibility = View.VISIBLE
                        myClassifiedBinding?.topAdvertiseRv?.visibility = View.VISIBLE
                        myClassifiedBinding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                        myClassifiedBinding?.emptyClassifiedImage?.visibility = View.GONE
                        myClassifiedBinding?.emptyTextVew?.visibility = View.GONE
                        response.data.userAdsList.let { allClassifiedAdapter?.setData(it) }
                        myClassifiedBinding?.recyclerViewClassified?.adapter =
                            allClassifiedAdapter
                    } else {
                        myClassifiedBinding?.emptyClassifiedImage?.visibility = View.VISIBLE
                        myClassifiedBinding?.emptyTextVew?.visibility = View.VISIBLE
                        myClassifiedBinding?.recyclerViewClassified?.visibility = View.GONE
                        myClassifiedBinding?.topAdvertiseRv?.visibility = View.GONE
                        myClassifiedBinding?.bottomAdvertiseRv?.visibility = View.GONE
                    }

                }
                is Resource.Error -> {
                    myClassifiedBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }


        return myClassifiedBinding?.root
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
    }
}