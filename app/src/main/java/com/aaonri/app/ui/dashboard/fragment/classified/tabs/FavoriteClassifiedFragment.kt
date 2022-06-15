package com.aaonri.app.ui.dashboard.fragment.classified.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.databinding.FragmentFavoriteClassifiedBinding
import com.aaonri.app.databinding.FragmentMyClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteClassifiedFragment : Fragment() {
    var favoriteClassifiedBinding: FragmentFavoriteClassifiedBinding? = null
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoriteClassifiedBinding =
            FragmentFavoriteClassifiedBinding.inflate(inflater, container, false)
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        allClassifiedAdapter = AllClassifiedAdapter()

        favoriteClassifiedBinding?.apply {
            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 60, 40))
        }

        classifiedViewModel.getClassifiedByUser(
            GetClassifiedByUserRequest(
                category = "",
                email = if (email?.isNotEmpty() == true) email else "",
                fetchCatSubCat = true,
                keywords = "",
                location = "",
                maxPrice = 0,
                minPrice = 0,
                myAdsOnly = false,
                popularOnAoonri = null,
                subCategory = "",
                zipCode = ""
            )
        )

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    favoriteClassifiedBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    favoriteClassifiedBinding?.progressBar?.visibility = View.GONE
                    val filteredList = mutableListOf<UserAds>()
                    response.data?.userAdsList?.forEach {
                        if (it.favorite) {
                            filteredList.add(it)
                        }
                    }
                    if (filteredList.isEmpty()) {
                        favoriteClassifiedBinding?.nestedScrollView?.visibility = View.VISIBLE
                    } else {
                        favoriteClassifiedBinding?.nestedScrollView?.visibility = View.GONE
                    }
                    response.data?.userAdsList?.let { allClassifiedAdapter!!.setData(filteredList) }
                    favoriteClassifiedBinding?.recyclerViewClassified?.adapter =
                        allClassifiedAdapter
                }
                is Resource.Error -> {
                    favoriteClassifiedBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        return favoriteClassifiedBinding?.root
    }
}