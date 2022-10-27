package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponseItem
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentClassifiedFilterBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.DecimalDigitsInputFilter
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.material.snackbar.Snackbar

class ClassifiedFilterFragmentBottom : Fragment() {
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var binding: FragmentClassifiedFilterBinding? = null
    var isDatePublishedSelected = false
    var isPriceLowToHighSelected = false
    var isPriceHighToLowSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentClassifiedFilterBinding.inflate(inflater, container, false)

        val zipCode = context?.let { PreferenceManager<String>(it)[Constant.USER_ZIP_CODE, ""] }

        binding?.apply {
            minPriceRange.stickPrefix("$")
            maxPriceRange.stickPrefix("$")
            minPriceRange.filters = arrayOf(DecimalDigitsInputFilter(2))
            maxPriceRange.filters = arrayOf(DecimalDigitsInputFilter(2))

            selectCategoryClassifiedSpinner.setOnClickListener {
                val action =
                    ClassifiedFilterFragmentBottomDirections.actionClassifiedFilterFragmentBottomToSelectClassifiedCategoryBottom2()
                findNavController().navigate(action)
            }

            selectSubCategoryClassifiedSpinner.setOnClickListener {
                if (selectCategoryClassifiedSpinner.text.isNotEmpty()) {
                    val action =
                        ClassifiedFilterFragmentBottomDirections.actionClassifiedFilterFragmentBottomToSelectClassifiedSubCategoryBottom2()
                    findNavController().navigate(action)
                }
            }

            myLocationCheckBox.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    zipCodeEt.isEnabled = false
                    zipCodeEt.setText(zipCode)
                } else {
                    zipCodeEt.isEnabled = true
                    zipCodeEt.setText("")
                }
            }

            applyBtn.setOnClickListener {
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                var minValue = ""
                var maxValue = ""
                if (minPriceRange.text.toString().contains("$")) {
                    minValue = minPriceRange.text.toString().replace("$", "")
                }
                if (maxPriceRange.text.toString().contains("$")) {
                    maxValue = maxPriceRange.text.toString().replace("$", "")
                }

                if (minValue.isNotEmpty() && minValue.length < 9) {
                    if (maxValue.isNotEmpty() && maxValue.length < 9) {
                        if (minValue.toInt() > 0 && minValue.toInt() < maxValue.toInt() && maxValue.toInt() != minValue.toInt()) {

                            if (zipCodeEt.text.toString().isNotEmpty()) {
                                if (zipCodeEt.text.toString().length >= 5) {

                                    postClassifiedViewModel.setCategoryFilter(
                                        selectCategoryClassifiedSpinner.text.toString()
                                    )
                                    postClassifiedViewModel.setSubCategoryFilter(
                                        selectSubCategoryClassifiedSpinner.text.toString()
                                    )
                                    postClassifiedViewModel.setMinValue(minValue)
                                    postClassifiedViewModel.setMaxValue(maxValue)

                                    postClassifiedViewModel.setZipCodeInFilterScreen(zipCodeEt.text.toString())
                                    postClassifiedViewModel.setChangeSortTripletFilter(
                                        isDatePublishedSelected,
                                        isPriceLowToHighSelected,
                                        isPriceHighToLowSelected
                                    )

                                    val action =
                                        ClassifiedFilterFragmentBottomDirections.actionClassifiedFilterFragmentBottomToClassifiedScreenFragment()
                                    findNavController().navigate(action)
                                    postClassifiedViewModel.setClickedOnFilter(true)
                                    postClassifiedViewModel.setIsFilterEnable(true)
                                } else {
                                    showAlert("Please enter valid ZipCode")
                                }
                            } else {
                                postClassifiedViewModel.setZipCodeInFilterScreen("")
                                postClassifiedViewModel.setCategoryFilter(
                                    selectCategoryClassifiedSpinner.text.toString()
                                )
                                postClassifiedViewModel.setSubCategoryFilter(
                                    selectSubCategoryClassifiedSpinner.text.toString()
                                )
                                postClassifiedViewModel.setMinValue(minValue)
                                postClassifiedViewModel.setMaxValue(maxValue)
                                postClassifiedViewModel.setChangeSortTripletFilter(
                                    isDatePublishedSelected,
                                    isPriceLowToHighSelected,
                                    isPriceHighToLowSelected
                                )
                                val action =
                                    ClassifiedFilterFragmentBottomDirections.actionClassifiedFilterFragmentBottomToClassifiedScreenFragment()
                                findNavController().navigate(action)
                                postClassifiedViewModel.setClickedOnFilter(true)
                                postClassifiedViewModel.setIsFilterEnable(true)
                            }

                        } else {
                            showAlert("Please enter valid price range")
                        }
                    } else {
                        showAlert("Please enter valid price range")
                    }
                } else if (zipCodeEt.text.toString().isNotEmpty()) {
                    if (zipCodeEt.text.toString().length >= 5) {
                        postClassifiedViewModel.setCategoryFilter(
                            selectCategoryClassifiedSpinner.text.toString()
                        )
                        postClassifiedViewModel.setSubCategoryFilter(
                            selectSubCategoryClassifiedSpinner.text.toString()
                        )
                        postClassifiedViewModel.setZipCodeInFilterScreen(zipCodeEt.text.toString())

                        postClassifiedViewModel.setChangeSortTripletFilter(
                            isDatePublishedSelected,
                            isPriceLowToHighSelected,
                            isPriceHighToLowSelected
                        )
                        val action =
                            ClassifiedFilterFragmentBottomDirections.actionClassifiedFilterFragmentBottomToClassifiedScreenFragment()
                        findNavController().navigate(action)
                        postClassifiedViewModel.setClickedOnFilter(true)
                        postClassifiedViewModel.setIsFilterEnable(true)

                    } else {
                        postClassifiedViewModel.setZipCodeInFilterScreen("")
                        showAlert("Please enter valid ZipCode")
                    }
                } else if (selectCategoryClassifiedSpinner.text.toString()
                        .isNotEmpty() || selectSubCategoryClassifiedSpinner.text.toString()
                        .isNotEmpty()
                ) {
                    postClassifiedViewModel.setChangeSortTripletFilter(
                        isDatePublishedSelected,
                        isPriceLowToHighSelected,
                        isPriceHighToLowSelected
                    )
                    postClassifiedViewModel.setCategoryFilter(
                        selectCategoryClassifiedSpinner.text.toString()
                    )
                    postClassifiedViewModel.setSubCategoryFilter(
                        selectSubCategoryClassifiedSpinner.text.toString()
                    )
                    val action =
                        ClassifiedFilterFragmentBottomDirections.actionClassifiedFilterFragmentBottomToClassifiedScreenFragment()
                    findNavController().navigate(action)
                    postClassifiedViewModel.setClickedOnFilter(true)
                } else if (isDatePublishedSelected || isPriceHighToLowSelected || isPriceLowToHighSelected/*postClassifiedViewModel.changeSortTriplet.first || postClassifiedViewModel.changeSortTriplet.second || postClassifiedViewModel.changeSortTriplet.third*/) {

                    postClassifiedViewModel.setChangeSortTripletFilter(
                        isDatePublishedSelected,
                        isPriceLowToHighSelected,
                        isPriceHighToLowSelected
                    )
                    val action =
                        ClassifiedFilterFragmentBottomDirections.actionClassifiedFilterFragmentBottomToClassifiedScreenFragment()
                    findNavController().navigate(action)
                    postClassifiedViewModel.setClickedOnFilter(true)
                    //postClassifiedViewModel.setZipCodeInFilterScreen("")
                }

                postClassifiedViewModel.setIsMyLocationChecked(myLocationCheckBox.isChecked)

            }

            closeClassifiedBtn.setOnClickListener {
                postClassifiedViewModel.setClickedOnFilter(false)
                findNavController().navigateUp()
            }

            clearAllBtn.setOnClickListener {
                postClassifiedViewModel.setClearAllFilter(true)
                postClassifiedViewModel.setClickOnClearAllFilterBtn(true)
            }

            setData()

            datePublished.setOnClickListener {

                if (!isDatePublishedSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        datePublished.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> datePublished.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        priceLowToHigh.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> priceLowToHigh.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        priceHighToLow.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> priceHighToLow.setTextColor(it1) }

                    /*context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        relevance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> relevance.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        distance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> distance.setTextColor(it1) }*/

                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        datePublished.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> datePublished.setTextColor(it1) }
                }
                isDatePublishedSelected = !isDatePublishedSelected
                isPriceHighToLowSelected = false
                isPriceLowToHighSelected = false
                /*postClassifiedViewModel.setChangeSortTripletFilter(
                    isDatePublishedSelected,
                    isPriceLowToHighSelected,
                    isPriceHighToLowSelected
                )*/
                /* isRelevanceSelected = false
                 isDistanceSelected = false*/
            }

            /*relevance.setOnClickListener {

                if (!isRelevanceSelected) {

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        relevance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> relevance.setTextColor(it1) }


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        datePublished.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> datePublished.setTextColor(it1) }


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        distance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> distance.setTextColor(it1) }
                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        relevance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> relevance.setTextColor(it1) }
                }
                isRelevanceSelected = !isRelevanceSelected
                isDatePublishedSelected = false
                isDistanceSelected = false
            }
            distance.setOnClickListener {

                if (!isDistanceSelected) {

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        distance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> distance.setTextColor(it1) }


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        datePublished.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> datePublished.setTextColor(it1) }


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        relevance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> relevance.setTextColor(it1) }

                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        distance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> distance.setTextColor(it1) }
                }

                isDistanceSelected = !isDistanceSelected
                isDatePublishedSelected = false
                isRelevanceSelected = false

            }*/

            priceLowToHigh.setOnClickListener {

                if (!isPriceLowToHighSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        priceLowToHigh.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> priceLowToHigh.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        priceHighToLow.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> priceHighToLow.setTextColor(it1) }


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        datePublished.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> datePublished.setTextColor(it1) }

                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        priceLowToHigh.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> priceLowToHigh.setTextColor(it1) }
                }
                isPriceLowToHighSelected = !isPriceLowToHighSelected
                isPriceHighToLowSelected = false
                isDatePublishedSelected = false
                /*postClassifiedViewModel.setChangeSortTripletFilter(
                    isDatePublishedSelected,
                    isPriceLowToHighSelected,
                    isPriceHighToLowSelected
                )*/
            }

            priceHighToLow.setOnClickListener {
                if (!isPriceHighToLowSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        priceHighToLow.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> priceHighToLow.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        priceLowToHigh.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> priceLowToHigh.setTextColor(it1) }
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        datePublished.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> datePublished.setTextColor(it1) }

                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        priceHighToLow.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> priceHighToLow.setTextColor(it1) }
                }
                isPriceHighToLowSelected = !isPriceHighToLowSelected
                isPriceLowToHighSelected = false
                isDatePublishedSelected = false
                /*postClassifiedViewModel.setChangeSortTripletFilter(
                    isDatePublishedSelected,
                    isPriceLowToHighSelected,
                    isPriceHighToLowSelected
                )*/
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

        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            if (it) {
                binding?.myLocationLinear?.visibility = View.GONE
                binding?.locationTv?.visibility = View.GONE
            } else {
                binding?.myLocationLinear?.visibility = View.VISIBLE
                binding?.locationTv?.visibility = View.VISIBLE
            }
        }

        postClassifiedViewModel.clearAllFilter.observe(viewLifecycleOwner) { clearAllFilter ->
            if (clearAllFilter) {

                binding?.minPriceRange?.setText("")
                binding?.maxPriceRange?.setText("")
                binding?.zipCodeEt?.setText("")
                binding?.myLocationCheckBox?.isChecked = false
                binding?.selectCategoryClassifiedSpinner?.text = ""
                binding?.selectSubCategoryClassifiedSpinner?.text = ""

                postClassifiedViewModel.setSearchQuery("")
                postClassifiedViewModel.setMinValue("")
                postClassifiedViewModel.setMaxValue("")
                postClassifiedViewModel.setIsMyLocationChecked(false)
                postClassifiedViewModel.setZipCodeInFilterScreen("")
                postClassifiedViewModel.setClickedOnFilter(false)
                postClassifiedViewModel.setCategoryFilter("")
                postClassifiedViewModel.setSubCategoryFilter("")

                postClassifiedViewModel.setChangeSortTripletFilter(
                    datePublished = false,
                    priceLowToHigh = false,
                    priceHighToLow = false
                )

                postClassifiedViewModel.setSelectedClassifiedCategory(
                    ClassifiedCategoryResponseItem(
                        emptyList(),
                        0,
                        0,
                        0,
                        ""
                    )
                )

                postClassifiedViewModel.setSelectedSubClassifiedCategory(
                    ClassifiedSubcategoryX(
                        0,
                        0,
                        0,
                        ""
                    )
                )

                /*selectedFilterList.clear()*/

                binding?.apply {

                    myLocationCheckBox.isChecked = false

                    /*zipCode.setText("")*/

                    isPriceHighToLowSelected = false
                    isPriceLowToHighSelected = false
                    /*isDistanceSelected = false
                    isRelevanceSelected = false*/
                    isDatePublishedSelected = false

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        minPriceRange.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)?.let { it1 -> minPriceRange.setTextColor(it1) }


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        maxPriceRange.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)?.let { it1 -> maxPriceRange.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        datePublished.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)?.let { it1 -> datePublished.setTextColor(it1) }

                    /*context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        relevance.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)?.let { it1 -> relevance.setTextColor(it1) }*/

                    /* context?.let { it1 ->
                         ContextCompat.getColor(
                             it1,
                             R.color.white
                         )
                     }?.let { it2 ->
                         distance.setBackgroundColor(
                             it2
                         )
                     }
                     context?.getColor(R.color.black)?.let { it1 -> distance.setTextColor(it1) }*/


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        priceLowToHigh.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> priceLowToHigh.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        priceHighToLow.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> priceHighToLow.setTextColor(it1) }
                }
            }
            postClassifiedViewModel.setClearAllFilter(false)
        }

        /* postClassifiedViewModel.isMyLocationCheckedInFilterScreen.observe(viewLifecycleOwner) {
             classifiedFilterBinding?.myLocationCheckBox?.isChecked = it
         }*/

        /*postClassifiedViewModel.minMaxPriceRangeZipCode.observe(viewLifecycleOwner) {
            if (it.first.isNotEmpty() || it.second.isNotEmpty() || it.third.isNotEmpty()) {
                classifiedFilterBinding?.minPriceRange?.setText(it.first)
                classifiedFilterBinding?.maxPriceRange?.setText(it.second)
                classifiedFilterBinding?.zipCode?.setText(it.third)
            }
        }*/


        return binding?.root


    }

    private fun setData() {

        binding?.minPriceRange?.setText(postClassifiedViewModel.minValueInFilterScreen)
        binding?.maxPriceRange?.setText(postClassifiedViewModel.maxValueInFilterScreen)
        binding?.zipCodeEt?.setText(postClassifiedViewModel.zipCodeInFilterScreen)
        binding?.myLocationCheckBox?.isChecked =
            postClassifiedViewModel.isMyLocationCheckedInFilterScreen

        postClassifiedViewModel.changeSortTriplet.let {
            if (it.first) {
                isDatePublishedSelected = true
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.blueBtnColor
                    )
                }?.let { it2 ->
                    binding?.datePublished?.setBackgroundColor(
                        it2
                    )
                }
                context?.getColor(R.color.white)
                    ?.let { it1 -> binding?.datePublished?.setTextColor(it1) }
            }
            if (it.second) {
                isPriceLowToHighSelected = true
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.blueBtnColor
                    )
                }?.let { it2 ->
                    binding?.priceLowToHigh?.setBackgroundColor(
                        it2
                    )
                }
                context?.getColor(R.color.white)
                    ?.let { it1 -> binding?.priceLowToHigh?.setTextColor(it1) }
            }
            if (it.third) {
                isPriceHighToLowSelected = true
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.blueBtnColor
                    )
                }?.let { it2 ->
                    binding?.priceHighToLow?.setBackgroundColor(
                        it2
                    )
                }
                context?.getColor(R.color.white)
                    ?.let { it1 -> binding?.priceHighToLow?.setTextColor(it1) }
            }
        }

    }


    /*private fun clearAllData() {

        postClassifiedViewModel.setClickOnClearAllFilter(true)
        classifiedFilterBinding?.minPriceRange?.setText("")
        classifiedFilterBinding?.maxPriceRange?.setText("")
        classifiedFilterBinding?.zipCodeEt?.setText("")
        classifiedFilterBinding?.myLocationCheckBox?.isChecked = false
        classifiedFilterBinding?.selectCategoryClassifiedSpinner?.text = ""
        classifiedFilterBinding?.selectSubCategoryClassifiedSpinner?.text = ""

        postClassifiedViewModel.setMinValue("")
        postClassifiedViewModel.setMaxValue("")
        postClassifiedViewModel.setIsMyLocationChecked(false)
        postClassifiedViewModel.setZipCodeInFilterScreen("")
        postClassifiedViewModel.setClickedOnFilter(false)

        *//*selectedFilterList.clear()*//*

        classifiedFilterBinding?.apply {

            myLocationCheckBox.isChecked = false

            *//*zipCode.setText("")*//*


            isPriceHighToLowSelected = false
            isPriceLowToHighSelected = false
            *//*isDistanceSelected = false
            isRelevanceSelected = false*//*
            isDatePublishedSelected = false

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                minPriceRange.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.black)?.let { it1 -> minPriceRange.setTextColor(it1) }


            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                maxPriceRange.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.black)?.let { it1 -> maxPriceRange.setTextColor(it1) }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                datePublished.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.black)?.let { it1 -> datePublished.setTextColor(it1) }

            *//*context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                relevance.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.black)?.let { it1 -> relevance.setTextColor(it1) }*//*

            *//* context?.let { it1 ->
                 ContextCompat.getColor(
                     it1,
                     R.color.white
                 )
             }?.let { it2 ->
                 distance.setBackgroundColor(
                     it2
                 )
             }
             context?.getColor(R.color.black)?.let { it1 -> distance.setTextColor(it1) }*//*


            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                priceLowToHigh.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.black)
                ?.let { it1 -> priceLowToHigh.setTextColor(it1) }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                priceHighToLow.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.black)
                ?.let { it1 -> priceHighToLow.setTextColor(it1) }
        }


    }*/

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