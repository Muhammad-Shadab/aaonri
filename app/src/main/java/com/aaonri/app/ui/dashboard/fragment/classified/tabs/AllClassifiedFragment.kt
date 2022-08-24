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
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentAllClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllClassifiedFragment : Fragment() {
    var allClassifiedBinding: FragmentAllClassifiedBinding? = null
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allClassifiedBinding =
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

        allClassifiedBinding?.apply {
            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))

            recyclerViewClassified.setHasFixedSize(true)
            recyclerViewClassified.isNestedScrollingEnabled = false
            topAdvertiseRv.isNestedScrollingEnabled = false
            bottomAdvertiseRv.isNestedScrollingEnabled = false

            adsGenericAdapter1?.items = ActiveAdvertiseStaticData.getClassifiedTopBanner()

            adsGenericAdapter2?.items =
                ActiveAdvertiseStaticData.getClassifiedJustAboveFooterImageOnly() + ActiveAdvertiseStaticData.getClassifiedJustAboveBottomTabBOTH() + ActiveAdvertiseStaticData.getClassifiedJustAboveFooterTextOnly()

            topAdvertiseRv.adapter = adsGenericAdapter1
            topAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            bottomAdvertiseRv.adapter = adsGenericAdapter2
            bottomAdvertiseRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    allClassifiedBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    allClassifiedBinding?.progressBar?.visibility = View.GONE

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
                        allClassifiedBinding?.recyclerViewClassified?.visibility = View.VISIBLE
                        allClassifiedBinding?.topAdvertiseRv?.visibility = View.VISIBLE
                        allClassifiedBinding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                    }
                    allClassifiedBinding?.recyclerViewClassified?.adapter = allClassifiedAdapter
                    if (response.data?.userAdsList?.isEmpty() == true) {
                        allClassifiedBinding?.recyclerViewClassified?.visibility = View.GONE
                        allClassifiedBinding?.topAdvertiseRv?.visibility = View.GONE
                        allClassifiedBinding?.bottomAdvertiseRv?.visibility = View.GONE
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "No result found", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                is Resource.Error -> {
                    allClassifiedBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        postClassifiedViewModel.keyClassifiedKeyboardListener.observe(viewLifecycleOwner) {
            if (it) {
                allClassifiedBinding?.recyclerViewClassified?.visibility = View.VISIBLE
                allClassifiedBinding?.topAdvertiseRv?.visibility = View.VISIBLE
                allClassifiedBinding?.bottomAdvertiseRv?.visibility = View.VISIBLE
                allClassifiedAdapter?.setData(classifiedViewModel.allClassifiedList)
            }
        }

        return allClassifiedBinding?.root
    }
}

