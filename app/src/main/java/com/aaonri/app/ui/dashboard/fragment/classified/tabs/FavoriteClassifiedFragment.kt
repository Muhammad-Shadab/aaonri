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

@AndroidEntryPoint
class
FavoriteClassifiedFragment : Fragment() {
    var binding: FragmentFavoriteClassifiedBinding? = null
    var favoriteClassifiedAdapter: FavoriteClassifiedAdapter? = null
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null

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
            topAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            bottomAdvertiseRv.adapter = adsGenericAdapter2
            bottomAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}