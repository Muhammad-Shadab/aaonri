package com.aaonri.app.ui.dashboard.fragment.classified.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentMyClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyClassifiedFragment : Fragment() {
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var myClassifiedBinding: FragmentMyClassifiedBinding? = null
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myClassifiedBinding =
            FragmentMyClassifiedBinding.inflate(inflater, container, false)

        allClassifiedAdapter = AllClassifiedAdapter {
            postClassifiedViewModel.setSendDataToClassifiedDetailsScreen(it)
            postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(true)
        }

        myClassifiedBinding?.apply {
            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 40, 40))
        }

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    myClassifiedBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    myClassifiedBinding?.progressBar?.visibility = View.GONE
                    try {
                        if (response.data?.userAdsList?.isNotEmpty() == true) {
                            myClassifiedBinding?.emptyClassifiedImage?.visibility = View.GONE
                            myClassifiedBinding?.emptyTextVew?.visibility = View.GONE
                            response.data.userAdsList.let { allClassifiedAdapter!!.setData(it) }
                            myClassifiedBinding?.recyclerViewClassified?.adapter =
                                allClassifiedAdapter
                        } else {
                            myClassifiedBinding?.emptyClassifiedImage?.visibility = View.VISIBLE
                            myClassifiedBinding?.emptyTextVew?.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {

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
        classifiedViewModel.getClassifiedByUser(
            GetClassifiedByUserRequest(
                category = "",
                email = if (email?.isNotEmpty() == true) email else "",
                fetchCatSubCat = true,
                keywords = "",
                location = "",
                maxPrice = 0,
                minPrice = 0,
                myAdsOnly = true,
                popularOnAoonri = null,
                subCategory = "",
                zipCode = ""
            )
        )
    }
}