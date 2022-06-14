package com.aaonri.app.ui.dashboard.fragment.classified.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.databinding.FragmentAllClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllClassifiedFragment : Fragment() {
    var allClassifiedBinding: FragmentAllClassifiedBinding? = null
    var allClassifiedAdapter: AllClassifiedAdapter? = null
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allClassifiedBinding =
            FragmentAllClassifiedBinding.inflate(inflater, container, false)
        allClassifiedAdapter = AllClassifiedAdapter()

        classifiedViewModel.getAllUserAdsClassified("a")

        allClassifiedBinding?.apply {

        }

        classifiedViewModel.allUserAdsClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    allClassifiedBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    allClassifiedBinding?.progressBar?.visibility = View.GONE
                    response.data?.userAds?.let { allClassifiedAdapter!!.setData(it) }
                    allClassifiedBinding?.recyclerViewClassified?.adapter = allClassifiedAdapter
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

        allClassifiedBinding?.recyclerViewClassified?.layoutManager = GridLayoutManager(context, 2)
        allClassifiedBinding?.recyclerViewClassified?.addItemDecoration(GridSpacingItemDecoration(2,40,40))


        return allClassifiedBinding?.root
    }
}

