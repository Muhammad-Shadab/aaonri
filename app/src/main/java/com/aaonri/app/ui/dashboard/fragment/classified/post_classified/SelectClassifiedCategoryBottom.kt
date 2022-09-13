package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedStaticData
import com.aaonri.app.data.classified.adapter.ClassifiedCategoryAdapter
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentSelectClassifiedCategoryBottomBinding
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectClassifiedCategoryBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentSelectClassifiedCategoryBottomBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var classifiedCategoryAdapter: ClassifiedCategoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        isCancelable = false
        binding =
            FragmentSelectClassifiedCategoryBottomBinding.inflate(inflater, container, false)

        classifiedCategoryAdapter = ClassifiedCategoryAdapter {
            postClassifiedViewModel.setSelectedClassifiedCategory(it)
            postClassifiedViewModel.setClassifiedSubCategoryList(it)
            postClassifiedViewModel.setClearSubCategory(true)
            findNavController().navigateUp()
        }

        binding?.apply {

            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            countriesRv.layoutManager = LinearLayoutManager(context)
            countriesRv.adapter = classifiedCategoryAdapter

        }

        if (ClassifiedStaticData.getCategoryList().isNotEmpty()) {
            classifiedCategoryAdapter?.setData(ClassifiedStaticData.getCategoryList())
        } else {
            postClassifiedViewModel.classifiedCategoryData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBarCommunityBottom?.visibility =
                            View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBarCommunityBottom?.visibility =
                            View.GONE
                        response.data?.let {
                            classifiedCategoryAdapter!!.setData(it)
                            ClassifiedStaticData.updateCategoryList(it)
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBarCommunityBottom?.visibility =
                            View.GONE
                    }
                    else -> {

                    }
                }
            }
        }

        if (postClassifiedViewModel.isUpdateClassified) {
            classifiedCategoryAdapter?.setData(ClassifiedStaticData.getCategoryList())
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}