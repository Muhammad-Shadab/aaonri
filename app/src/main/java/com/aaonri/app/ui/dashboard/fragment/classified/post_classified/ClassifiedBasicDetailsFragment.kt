package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponse
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponseItem
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedBasicDetailsBinding
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ClassifiedBasicDetailsFragment : Fragment() {
    var classifiedDetailsBinding: FragmentClassifiedBasicDetailsBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var selectedCategoryIndex = 0
    var classifiedCategory = mutableListOf<ClassifiedCategoryResponseItem>()
    var classifiedSubCategory = mutableListOf<ClassifiedSubcategoryX>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedDetailsBinding =
            FragmentClassifiedBasicDetailsBinding.inflate(inflater, container, false)

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.BASIC_DETAILS_SCREEN)

        postClassifiedViewModel.getClassifiedCategory()

        classifiedDetailsBinding?.apply {

            classifiedDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_classifiedBasicDetailsFragment_to_uploadClassifiedPicFragment)
            }

            classifiedDescEt.addTextChangedListener { editable ->
                descLength.text = "${editable.toString().length}/2000"
            }

            selectCategoryClassifiedSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
        }

        postClassifiedViewModel.classifiedCategory.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    /*val category = mutableListOf<String>()
                    val subCategory = mutableListOf<String>()*/

                    response.data?.forEach { it ->
                        classifiedCategory.add(it)
                    }
                    response.data?.get(selectedCategoryIndex)?.classifiedSubcategory?.forEach {
                        classifiedSubCategory.add(it)
                    }
                    val categoryArrayAdapter =
                        ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, classifiedCategory)
                    val subCategoryAdapter =
                        ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, classifiedSubCategory)
                    classifiedDetailsBinding?.selectCategoryClassifiedSpinner?.adapter =
                        categoryArrayAdapter
                    classifiedDetailsBinding?.selectSubCategoryClassifiedSpinner?.adapter =
                        subCategoryAdapter
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility = View.GONE

                }
                is Resource.Error -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility = View.GONE
                }
                else -> {}
            }
        }

        return classifiedDetailsBinding?.root
    }
}