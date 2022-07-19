package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.adapter.ClassifiedCategoryAdapter
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentSelectClassifiedCategoryBottomBinding
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectClassifiedCategoryBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var selectClassifiedCategoryBottom: FragmentSelectClassifiedCategoryBottomBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var classifiedCategoryAdapter: ClassifiedCategoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        isCancelable = false
        selectClassifiedCategoryBottom =
            FragmentSelectClassifiedCategoryBottomBinding.inflate(inflater, container, false)

        classifiedCategoryAdapter = ClassifiedCategoryAdapter {
            postClassifiedViewModel.setSelectedClassifiedCategory(it)
            postClassifiedViewModel.setClassifiedSubCategoryList(it)
            postClassifiedViewModel.setClearSubCategory(true)
            findNavController().navigateUp()
        }

        selectClassifiedCategoryBottom?.apply {

            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            countriesRv.layoutManager = LinearLayoutManager(context)
            countriesRv.adapter = classifiedCategoryAdapter

        }

        postClassifiedViewModel.classifiedCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    selectClassifiedCategoryBottom?.progressBarCommunityBottom?.visibility =
                        View.VISIBLE
                }
                is Resource.Success -> {
                    selectClassifiedCategoryBottom?.progressBarCommunityBottom?.visibility =
                        View.GONE
                    response.data?.let { classifiedCategoryAdapter!!.setData(it) }
                }
                is Resource.Error -> {
                    selectClassifiedCategoryBottom?.progressBarCommunityBottom?.visibility =
                        View.GONE
                }
                else -> {

                }
            }
        }


        return selectClassifiedCategoryBottom?.root
    }
}