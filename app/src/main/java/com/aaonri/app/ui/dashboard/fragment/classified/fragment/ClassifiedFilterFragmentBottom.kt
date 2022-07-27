package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentClassifiedFilterBinding
import com.aaonri.app.ui.dashboard.fragment.classified.post_classified.ClassifiedBasicDetailsFragmentDirections
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.DecimalDigitsInputFilter
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class ClassifiedFilterFragmentBottom : Fragment() {
    //override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var classifiedFilterBinding: FragmentClassifiedFilterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //isCancelable = false
        classifiedFilterBinding =
            FragmentClassifiedFilterBinding.inflate(inflater, container, false)

        val zipCode = context?.let { PreferenceManager<String>(it)[Constant.USER_ZIP_CODE, ""] }

        classifiedFilterBinding?.apply {
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
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(
                                    ClassifiedConstant.MIN_VALUE_FILTER,
                                    minValue
                                )
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(
                                    ClassifiedConstant.MAX_VALUE_FILTER,
                                    maxValue
                                )
                            //dismiss()
                            postClassifiedViewModel.setClickedOnFilter(true)
                        } else {
                            /* dialog?.window?.decorView?.let {
                                 Snackbar.make(
                                     it,
                                     "Please enter valid price range",
                                     Snackbar.LENGTH_SHORT
                                 ).show()
                             }*/
                        }
                    } else {
                        /*dialog?.window?.decorView?.let {
                            Snackbar.make(
                                it,
                                "Please enter valid price range",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }*/
                    }
                    /* if (minValue.toDouble() in 10.0..30.0) {
                         context?.let { it1 -> PreferenceManager<String>(it1) }
                             ?.set(
                                 ClassifiedConstant.MIN_VALUE_FILTER,
                                 "$minValue"
                             )
                         dismiss()
                         postClassifiedViewModel.setClickedOnFilter(true)
                     } else {
                         dialog?.window?.decorView?.let {
                             Snackbar.make(
                                 it,
                                 "Please enter valid price range",
                                 Snackbar.LENGTH_SHORT
                             ).show()
                         }
                     }*/
                } else {
                    /*context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(
                            ClassifiedConstant.MIN_VALUE_FILTER,
                            ""
                        )*/
                    /*dialog?.window?.decorView?.let {
                        Snackbar.make(
                            it,
                            "Please enter valid price range",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }*/
                }
                if (minValue.isNotEmpty() && minValue.length < 9) {
                    if (maxValue.isNotEmpty() && maxValue.length < 9) {
                        if (maxValue.toInt() > minValue.toInt() && minValue.toInt() > 0 && maxValue.toInt() != minValue.toInt()) {
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(
                                    ClassifiedConstant.MIN_VALUE_FILTER,
                                    minValue
                                )
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(
                                    ClassifiedConstant.MAX_VALUE_FILTER,
                                    maxValue
                                )
                            //dismiss()
                            postClassifiedViewModel.setClickedOnFilter(true)
                        } else {
                            /*dialog?.window?.decorView?.let {
                                Snackbar.make(
                                    it,
                                    "Please enter valid price range",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }*/
                        }
                    }
                    /*if (maxValue.toDouble() in 10.0..30.0) {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(
                                ClassifiedConstant.MAX_VALUE_FILTER,
                                "$maxValue"
                            )
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(
                                ClassifiedConstant.ZIPCODE_FILTER,
                                "${zipCodeEt.text}"
                            )
                        dismiss()
                        postClassifiedViewModel.setClickedOnFilter(true)
                    }*/
                    /*else {
                            dialog?.window?.decorView?.let {
                                Snackbar.make(
                                    it,
                                    "Please enter valid price range",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }*/
                } else {
                    /*context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(
                            ClassifiedConstant.MAX_VALUE_FILTER,
                            ""
                        )*/
                    /*dialog?.window?.decorView?.let {
                        Snackbar.make(
                            it,
                            "Please enter valid price range",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }*/
                }

                /*if (myLocationCheckBox.isChecked) {
                    if (zipCodeEt.text.toString()
                            .isNotEmpty() && zipCodeEt.text.toString().length >= 5
                    ) {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(
                                ClassifiedConstant.ZIPCODE_FILTER,
                                "${zipCodeEt.text}"
                            )
                        postClassifiedViewModel.setClickedOnFilter(true)
                        dismiss()
                    } else {
                        dialog?.window?.decorView?.let {
                            Snackbar.make(
                                it,
                                "Please enter valid ZipCode",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }*/

                if (zipCodeEt.text.toString().isNotEmpty()) {
                    if (zipCodeEt.text.toString().length >= 5) {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(
                                ClassifiedConstant.ZIPCODE_FILTER,
                                "${zipCodeEt.text}"
                            )
                        //dismiss()
                        postClassifiedViewModel.setClickedOnFilter(true)
                    } else {
                        /*dialog?.window?.decorView?.let {
                            Snackbar.make(
                                it,
                                "Please enter valid ZipCode",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }*/
                    }
                } else {
                    classifiedFilterBinding?.zipCodeEt?.setText("")
                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(
                            ClassifiedConstant.ZIPCODE_FILTER,
                            ""
                        )
                }

                /* context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                     ?.set(
                         ClassifiedConstant.MY_LOCATION_CHECKBOX,
                         myLocationCheckBox.isChecked
                     )*/
            }

            closeClassifiedBtn.setOnClickListener {
                //dismiss()
            }

            clearAllBtn.setOnClickListener {
                //dismiss()
                clearAllData()
            }

            setData()

            /*datePublished.setOnClickListener {

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
                        ?.let { it1 -> distance.setTextColor(it1) }
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
                isRelevanceSelected = false
                isDistanceSelected = false
            }

            relevance.setOnClickListener {

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

            }
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
            }*/
        }

        postClassifiedViewModel.selectedClassifiedCategory.observe(viewLifecycleOwner) {
            classifiedFilterBinding?.selectCategoryClassifiedSpinner?.text = it.title
            if (postClassifiedViewModel.clearSubCategory) {
                classifiedFilterBinding?.selectSubCategoryClassifiedSpinner?.text = ""
                postClassifiedViewModel.setClearSubCategory(false)
            }
        }

        postClassifiedViewModel.selectedSubClassifiedCategory.observe(viewLifecycleOwner) {
            classifiedFilterBinding?.selectSubCategoryClassifiedSpinner?.text = it.title
        }

        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            if (it) {
                classifiedFilterBinding?.myLocationLinear?.visibility = View.GONE
                classifiedFilterBinding?.locationTv?.visibility = View.GONE
            } else {
                classifiedFilterBinding?.myLocationLinear?.visibility = View.VISIBLE
                classifiedFilterBinding?.locationTv?.visibility = View.VISIBLE
            }
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


        return classifiedFilterBinding?.root


    }

    private fun setData() {
        /*val minMaxValue =
            context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MIN_MAX_FILTER, ""] }*/
        val minValue =
            context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MIN_VALUE_FILTER, ""] }
        val maxValue =
            context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MAX_VALUE_FILTER, ""] }
        val zipCodeValue =
            context?.let { PreferenceManager<String>(it)[ClassifiedConstant.ZIPCODE_FILTER, ""] }
        val myLocationCheckBox =
            context?.let { PreferenceManager<Boolean>(it)[ClassifiedConstant.MY_LOCATION_CHECKBOX, false] }

        classifiedFilterBinding?.minPriceRange?.setText(minValue?.replace("Range: $", ""))
        classifiedFilterBinding?.maxPriceRange?.setText(maxValue?.replace("Range: $", ""))
        classifiedFilterBinding?.zipCodeEt?.setText(zipCodeValue)
        if (myLocationCheckBox != null) {
            classifiedFilterBinding?.myLocationCheckBox?.isChecked = myLocationCheckBox
        }
        /*if (myLocationCheckBox != null) {
            classifiedFilterBinding?.myLocationCheckBox?.isChecked = myLocationCheckBox
        }*/
    }


    private fun clearAllData() {
        postClassifiedViewModel.setClickOnClearAllFilter(true)
        classifiedFilterBinding?.minPriceRange?.setText("")
        classifiedFilterBinding?.maxPriceRange?.setText("")
        classifiedFilterBinding?.zipCodeEt?.setText("")
        /*classifiedFilterBinding?.myLocationCheckBox?.isChecked = false*/

        context?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(
                ClassifiedConstant.MIN_VALUE_FILTER, ""
            )
        context?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(
                ClassifiedConstant.MAX_VALUE_FILTER, ""
            )
        context?.let { it1 -> PreferenceManager<Boolean>(it1) }
            ?.set(
                ClassifiedConstant.MY_LOCATION_CHECKBOX, false
            )
        context?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(
                ClassifiedConstant.ZIPCODE_FILTER,
                ""
            )
        postClassifiedViewModel.setClickedOnFilter(false)


        /*selectedFilterList.clear()

        classifiedFilterBinding?.apply {

            myLocationCheckBox.isChecked = false

            zipCode.setText("")


            isPriceHighToLowSelected = false
            isPriceLowToHighSelected = false
            isDistanceSelected = false
            isRelevanceSelected = false
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
            context?.getColor(R.color.black)?.let { it1 -> relevance.setTextColor(it1) }

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
            context?.getColor(R.color.black)?.let { it1 -> distance.setTextColor(it1) }


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

    */

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