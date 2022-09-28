package com.aaonri.app.ui.dashboard.fragment.homescreen_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.home_filter.viewmodel.HomeFilterViewModel
import com.aaonri.app.databinding.FragmentFilterCategoryBottomSheetBinding
import com.aaonri.app.ui.dashboard.home.adapter.ModuleCategoryAdapter
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterCategoryBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val homeFilterViewModel: HomeFilterViewModel by activityViewModels()
    var binding: FragmentFilterCategoryBottomSheetBinding? = null
    var moduleCategoryAdapter: ModuleCategoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding = FragmentFilterCategoryBottomSheetBinding.inflate(layoutInflater, container, false)


        moduleCategoryAdapter = ModuleCategoryAdapter {
            homeFilterViewModel.setSelectedServiceCategory(it)
            dismiss()
        }

        binding?.apply {


            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            categoriesRv.layoutManager = LinearLayoutManager(context)
            categoriesRv.adapter = moduleCategoryAdapter

        }

        homeFilterViewModel.service.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        response.data?.let { servicesResponse ->
                            moduleCategoryAdapter?.setData(servicesResponse.filter { it.active && it.id != 17 && it.id != 22 && it.id != 27 })
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}