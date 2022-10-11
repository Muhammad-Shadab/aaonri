package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.ClassifiedStaticData
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedBasicDetailsBinding
import com.aaonri.app.ui.dashboard.fragment.classified.RichTextEditorActivity
import com.aaonri.app.utils.DecimalDigitsInputFilter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat


@AndroidEntryPoint
class ClassifiedBasicDetailsFragment : Fragment() {
    var binding: FragmentClassifiedBasicDetailsBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var description: String? = ""

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("result")
                if (data?.isNotEmpty() == true) {
                    binding?.classifiedDescEt?.fromHtml(data.trim())
                    description = data.trim()
                } else {
                    binding?.classifiedDescEt?.text = ""
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentClassifiedBasicDetailsBinding.inflate(inflater, container, false)

        setData()

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.BASIC_DETAILS_SCREEN)

        binding?.apply {
            classifiedDescEt.movementMethod = ScrollingMovementMethod()
            priceClassifiedEt.stickPrefix("$")
            priceClassifiedEt.filters = arrayOf(DecimalDigitsInputFilter(2))

            classifiedDescEt.textSize = 15F

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
                                if (price.toDouble() < 999999999 && price.toDouble() >= 1) {
                                    if (classifiedDescEt.text.trim().isNotEmpty()) {
                                        postClassifiedViewModel.addIsProductNewCheckBox(
                                            isProductNewCheckBox.isChecked
                                        )
                                        postClassifiedViewModel.addClassifiedBasicDetails(
                                            title = titleClassifiedEt.text.trim().toString(),
                                            price = priceClassifiedEt.text.trim().toString()
                                                .replace("$", ""),
                                            adDescription = if (description != null) description!!.trim() else "",
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

            classifiedDescEt.setOnClickListener {
                val intent = Intent(context, RichTextEditorActivity::class.java)
                intent.putExtra("data", description)
                intent.putExtra("placeholder", "Please describe what you are selling?*")
                resultLauncher.launch(intent)
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
            binding?.selectCategoryClassifiedSpinner?.text = it.title
            if (postClassifiedViewModel.clearSubCategory) {
                binding?.selectSubCategoryClassifiedSpinner?.text = ""
                postClassifiedViewModel.setClearSubCategory(false)
            }
        }

        postClassifiedViewModel.selectedSubClassifiedCategory.observe(viewLifecycleOwner) {
            binding?.selectSubCategoryClassifiedSpinner?.text = it.title
        }

        if (postClassifiedViewModel.isUpdateClassified) {
            val addDetails = ClassifiedStaticData.getAddDetails()
            if (postClassifiedViewModel.isNavigateBackBasicDetails) {
                setData()
                postClassifiedViewModel.setIsNavigateBackToBasicDetails(false)
            } else {
                val uploadedImages = mutableListOf<Uri>()
                val uploadedImagesIdList = mutableListOf<Int>()
                binding?.selectCategoryClassifiedSpinner?.text =
                    addDetails?.userAds?.category
                binding?.selectSubCategoryClassifiedSpinner?.text =
                    addDetails?.userAds?.subCategory
                binding?.titleClassifiedEt?.setText(addDetails?.userAds?.adTitle)
                val random = addDetails?.userAds?.askingPrice

                val df = DecimalFormat("####")
                df.roundingMode = RoundingMode.DOWN
                val roundoff = df.format(random)
                binding?.priceClassifiedEt?.setText(roundoff)
                binding?.isProductNewCheckBox?.isChecked =
                    addDetails?.userAds?.isNew == true

                binding?.classifiedDescEt?.fromHtml(addDetails?.userAds?.adDescription.toString())
                addDetails?.userAds?.userAdsImages?.forEach {
                    uploadedImagesIdList.add(it.imageId)
                    uploadedImages.add("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${it.imagePath}".toUri())
                }

                addDetails?.userAds?.category?.let {
                    postClassifiedViewModel.setClassifiedCategoryWhileUpdating(
                        it
                    )
                }

                if (uploadedImages.isNotEmpty()) {
                    postClassifiedViewModel.setListOfUploadImagesUri(uploadedImages.distinct() as MutableList<Uri>)
                }

                if (uploadedImagesIdList.isNotEmpty()) {
                    postClassifiedViewModel.setClassifiedUploadedImagesIdList(
                        uploadedImagesIdList
                    )
                }

                binding?.classifiedDescEt?.fromHtml(addDetails?.userAds?.adDescription)

                description = addDetails?.userAds?.adDescription

                /*context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set("description", addDetails?.userAds?.adDescription.toString())*/
            }
        }

        if (postClassifiedViewModel.isUpdateClassified) {
            ClassifiedStaticData.getCategoryList()
                .forEachIndexed { index, classifiedCategoryResponseItem ->
                    if (classifiedCategoryResponseItem.title == postClassifiedViewModel.selectedClassifiedCategoryWhileUpdating) {
                        postClassifiedViewModel.setClassifiedSubCategoryList(
                            classifiedCategoryResponseItem
                        )
                    }
                }
        }

        /*postClassifiedViewModel.classifiedAdDetailsData.observe(viewLifecycleOwner) { response ->
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
                        val uploadedImagesIdList = mutableListOf<Int>()
                        classifiedDetailsBinding?.selectCategoryClassifiedSpinner?.text =
                            response.data?.userAds?.category
                        classifiedDetailsBinding?.selectSubCategoryClassifiedSpinner?.text =
                            response.data?.userAds?.subCategory
                        classifiedDetailsBinding?.titleClassifiedEt?.setText(response.data?.userAds?.adTitle)
                        classifiedDetailsBinding?.priceClassifiedEt?.setText(response.data?.userAds?.askingPrice.toString())
                        classifiedDetailsBinding?.isProductNewCheckBox?.isChecked =
                            response.data?.userAds?.isNew == true

                        classifiedDetailsBinding?.classifiedDescEt?.text =
                            Html.fromHtml(response.data?.userAds?.adDescription.toString())
                        response.data?.userAds?.userAdsImages?.forEach {
                            uploadedImagesIdList.add(it.imageId)
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

                        if (uploadedImagesIdList.isNotEmpty()) {
                            postClassifiedViewModel.setClassifiedUploadedImagesIdList(
                                uploadedImagesIdList
                            )
                        }

                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set("description", response.data?.userAds?.adDescription.toString())

                    }
                }
                is Resource.Error -> {
                    classifiedDetailsBinding?.progressBarBasicDetails?.visibility = View.VISIBLE
                }
                else -> {}
            }
        }*/

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })


        /*postClassifiedViewModel.classifiedCategoryData.observe(viewLifecycleOwner) { response ->
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
        }*/


        /*if (postClassifiedViewModel.isUpdateClassified) {

        }*/


        return binding?.root
    }

    private fun setData() {
        postClassifiedViewModel.apply {

            classifiedBasicDetailsMap.let {
                binding?.selectCategoryClassifiedSpinner?.text =
                    it[ClassifiedConstant.BASIC_DETAILS_CATEGORY]
                binding?.selectSubCategoryClassifiedSpinner?.text =
                    it[ClassifiedConstant.BASIC_DETAILS_SUB_CATEGORY]
                binding?.titleClassifiedEt?.setText(it[ClassifiedConstant.BASIC_DETAILS_TITLE])
                binding?.priceClassifiedEt?.setText(it[ClassifiedConstant.BASIC_DETAILS_ASKING_PRICE])
                if (it[ClassifiedConstant.BASIC_DETAILS_DESCRIPTION]?.isNotEmpty() == true) {
                    binding?.classifiedDescEt?.fromHtml(it[ClassifiedConstant.BASIC_DETAILS_DESCRIPTION])
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

