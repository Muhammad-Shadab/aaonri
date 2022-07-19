package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import com.aaonri.app.R
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedBasicDetailsBinding
import com.aaonri.app.utils.DecimalDigitsInputFilter
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClassifiedBasicDetailsFragment : Fragment() {
    var classifiedDetailsBinding: FragmentClassifiedBasicDetailsBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedDetailsBinding =
            FragmentClassifiedBasicDetailsBinding.inflate(inflater, container, false)

        postClassifiedViewModel.getClassifiedCategory()

        setData()

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.BASIC_DETAILS_SCREEN)

        classifiedDetailsBinding?.apply {

            priceClassifiedEt.stickPrefix("$")
            priceClassifiedEt.filters = arrayOf(DecimalDigitsInputFilter(2))

            classifiedDetailsNextBtn.setOnClickListener {

                var price = ""

                if (priceClassifiedEt.text.toString().contains("$")) {
                    price = priceClassifiedEt.text.toString().replace("$", "")
                }

                if (selectCategoryClassifiedSpinner.text.toString().isNotEmpty()) {
                    if (selectSubCategoryClassifiedSpinner.text.toString().isNotEmpty()) {
                        if (titleClassifiedEt.text.isNotEmpty() && titleClassifiedEt.text.trim()
                                .toString().length >= 3
                        ) {
                            if (price.isNotEmpty() && price.length < 9) {
                                if (price.toDouble() < 999999999 && price.toDouble() > 0) {
                                    if (classifiedDescEt.text.isNotEmpty()) {
                                        postClassifiedViewModel.addIsProductNewCheckBox(
                                            isProductNewCheckBox.isChecked
                                        )

                                        postClassifiedViewModel.addClassifiedBasicDetails(
                                            title = titleClassifiedEt.text.trim().toString(),
                                            price = priceClassifiedEt.text.trim().toString()
                                                .replace("$", ""),
                                            adDescription = classifiedDescEt.text.trim().toString(),
                                            classifiedCategory = selectCategoryClassifiedSpinner.text.toString(),
                                            classifiedSubCategory = selectSubCategoryClassifiedSpinner.text.toString()
                                        )
                                        findNavController().navigate(R.id.action_classifiedBasicDetailsFragment_to_uploadClassifiedPicFragment)
                                    } else {
                                        showAlert("Please enter valid classified description")
                                    }
                                } else {
                                    showAlert("Please enter valid classified price")
                                }
                            } else {
                                showAlert("Please enter valid classified price")
                            }

                        } else {
                            showAlert("Please enter valid classified title")
                        }
                    } else {
                        showAlert("Please select valid sub category")
                    }
                } else {
                    showAlert("Please select valid category")
                }
            }

            classifiedDescEt.addTextChangedListener { editable ->
                descLength.text = "${editable.toString().length}/2000"
            }
            selectCategoryClassifiedSpinner.setOnClickListener {
                val action =
                    ClassifiedBasicDetailsFragmentDirections.actionClassifiedBasicDetailsFragmentToSelectClassifiedCategoryBottom()
                findNavController().navigate(action)
            }

            selectSubCategoryClassifiedSpinner.setOnClickListener {
                if (selectCategoryClassifiedSpinner.text.isNotEmpty()) {
                    val action =
                        ClassifiedBasicDetailsFragmentDirections.actionClassifiedBasicDetailsFragmentToSelectClassifiedSubCategoryBottom()
                    findNavController().navigate(action)
                }
            }
        }

        postClassifiedViewModel.selectedClassifiedCategory.observe(viewLifecycleOwner) {
            classifiedDetailsBinding?.selectCategoryClassifiedSpinner?.text = it.title
            if (postClassifiedViewModel.clearSubCategory) {
                classifiedDetailsBinding?.selectSubCategoryClassifiedSpinner?.text = ""
                postClassifiedViewModel.setClearSubCategory(false)
            }
        }

        postClassifiedViewModel.selectedSubClassifiedCategory.observe(viewLifecycleOwner) {
            classifiedDetailsBinding?.selectSubCategoryClassifiedSpinner?.text = it.title
        }

        postClassifiedViewModel.classifiedAdDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility = View.GONE

                    if (postClassifiedViewModel.isNavigateBackBasicDetails) {
                        setData()
                        postClassifiedViewModel.setIsNavigateBackToBasicDetails(false)
                    } else {
                        val uploadedImages = mutableListOf<Uri>()
                        classifiedDetailsBinding?.selectCategoryClassifiedSpinner?.text =
                            response.data?.userAds?.category
                        classifiedDetailsBinding?.selectSubCategoryClassifiedSpinner?.text =
                            response.data?.userAds?.subCategory
                        classifiedDetailsBinding?.titleClassifiedEt?.setText(response.data?.userAds?.adTitle)
                        classifiedDetailsBinding?.priceClassifiedEt?.setText(response.data?.userAds?.askingPrice.toString())
                        classifiedDetailsBinding?.isProductNewCheckBox?.isChecked =
                            response.data?.userAds?.isNew == true
                        //postClassifiedViewModel.setSelectedClassifiedCategory(response.data.userAds.)
                        classifiedDetailsBinding?.classifiedDescEt?.setText(response.data?.userAds?.adDescription.toString())
                        response.data?.userAds?.userAdsImages?.forEach {
                            uploadedImages.add("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${it.imagePath}".toUri())
                        }

                        response.data?.userAds?.category?.let {
                            postClassifiedViewModel.setClassifiedCategoryWhileUpdating(
                                it
                            )
                        }

                        if (uploadedImages.isNotEmpty()) {
                            postClassifiedViewModel.setListOfUploadImagesUri(uploadedImages.distinct() as MutableList<Uri>)
                        }
                    }
                }
                is Resource.Error -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility = View.VISIBLE
                }
                else -> {}
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })


        postClassifiedViewModel.classifiedCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility =
                        View.VISIBLE
                }
                is Resource.Success -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility =
                        View.GONE

                    response.data?.forEach {
                        if (it.title == postClassifiedViewModel.selectedClssifiedCategoryWhileUpdating) {
                            postClassifiedViewModel.setClassifiedSubCategoryList(it)
                        }
                    }
                }
                is Resource.Error -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility =
                        View.GONE
                }
                else -> {

                }
            }
        }

        return classifiedDetailsBinding?.root
    }

    private fun setData() {
        postClassifiedViewModel.apply {

            classifiedBasicDetailsMap.let {

                classifiedDetailsBinding?.selectCategoryClassifiedSpinner?.text =
                    it[ClassifiedConstant.BASIC_DETAILS_CATEGORY]
                classifiedDetailsBinding?.selectSubCategoryClassifiedSpinner?.text =
                    it[ClassifiedConstant.BASIC_DETAILS_SUB_CATEGORY]
                classifiedDetailsBinding?.titleClassifiedEt?.setText(it[ClassifiedConstant.BASIC_DETAILS_TITLE])
                classifiedDetailsBinding?.priceClassifiedEt?.setText(it[ClassifiedConstant.BASIC_DETAILS_ASKING_PRICE])
                classifiedDetailsBinding?.classifiedDescEt?.setText(it[ClassifiedConstant.BASIC_DETAILS_DESCRIPTION])
            }
        }
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun EditText.stickPrefix(prefix: String) {
        this.addTextChangedListener(afterTextChanged = {
            if (!it.toString().startsWith(prefix) && it?.isNotEmpty() == true) {
                this.setText(prefix + this.text)
                this.setSelection(this.length())
            }
        })
    }
}

