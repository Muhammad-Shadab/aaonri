package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.classified.adapter.ClassifiedSubCategoryAdapter
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentSelectClassifiedSubCategoryBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectClassifiedSubCategoryBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentSelectClassifiedSubCategoryBottomBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var classifiedSubCategoryAdapter: ClassifiedSubCategoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding =
            FragmentSelectClassifiedSubCategoryBottomBinding.inflate(inflater, container, false)

        classifiedSubCategoryAdapter = ClassifiedSubCategoryAdapter {
            postClassifiedViewModel.setSelectedSubClassifiedCategory(it)
            findNavController().navigateUp()
        }

        binding?.apply {

            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            countriesRv.layoutManager = LinearLayoutManager(context)
            countriesRv.adapter = classifiedSubCategoryAdapter
        }

        postClassifiedViewModel.selectedClassifiedSubCategoryList.observe(viewLifecycleOwner) {
            classifiedSubCategoryAdapter?.setSubCategoryData(it.classifiedSubcategory as MutableList<ClassifiedSubcategoryX>)

        }

        return binding?.root

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}