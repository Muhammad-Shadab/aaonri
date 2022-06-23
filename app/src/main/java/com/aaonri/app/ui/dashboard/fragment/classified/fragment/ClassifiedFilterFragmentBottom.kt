package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class ClassifiedFilterFragmentBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var classifiedFilterBinding: FragmentClassifiedFilterBinding? = null

    /*var isDatePublishedSelected = false
    var isRelevanceSelected = false
    var isDistanceSelected = false
    var isPriceHighToLowSelected = false
    var isPriceLowToHighSelected = false*/
    var selectedFilterList = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        classifiedFilterBinding =
            FragmentClassifiedFilterBinding.inflate(inflater, container, false)



        classifiedFilterBinding?.apply {

            applyBtn.setOnClickListener {

                val minValue = minPriceRange.text.toString()
                val maxValue = maxPriceRange.text.toString()

                if (minValue.isNotEmpty() && maxValue.isNotEmpty()) {
                    if (minValue.toInt() in 10..30 && maxValue.toInt() in 10..30) {
                        if (myLocationCheckBox.isChecked) {
                            if (zipCode.text.toString().isNotEmpty()) {
                                postClassifiedViewModel.setMinMaxPriceRangeZipCode(
                                    minValue,
                                    maxValue,
                                    zipCode.text.toString()
                                )
                                findNavController().navigateUp()
                            } else {
                                dialog?.window?.decorView?.let {
                                    Snackbar.make(
                                        it,
                                        "Please enter valid ZipCode",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            postClassifiedViewModel.setMinMaxPriceRangeZipCode(
                                minValue,
                                maxValue,
                                zipCode.text.toString()
                            )
                            findNavController().navigateUp()
                        }
                    } else {
                        dialog?.window?.decorView?.let {
                            Snackbar.make(
                                it,
                                "Please enter valid price range",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else if (minValue.isNotEmpty()) {
                    if (minValue.toInt() in 10..30) {
                        if (myLocationCheckBox.isChecked) {
                            if (zipCode.text.toString().isNotEmpty()) {
                                postClassifiedViewModel.setMinMaxPriceRangeZipCode(
                                    minValue,
                                    "",
                                    zipCode.text.toString()
                                )
                                findNavController().navigateUp()
                            } else {
                                dialog?.window?.decorView?.let {
                                    Snackbar.make(
                                        it,
                                        "Please enter valid ZipCode",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            postClassifiedViewModel.setMinMaxPriceRangeZipCode(
                                minValue,
                                "",
                                zipCode.text.toString()
                            )
                            findNavController().navigateUp()
                        }
                    } else {
                        dialog?.window?.decorView?.let {
                            Snackbar.make(
                                it,
                                "Please enter valid price range",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else if (maxValue.isNotEmpty()) {
                    if (maxValue.toInt() in 10..30) {
                        if (myLocationCheckBox.isChecked) {
                            if (zipCode.text.toString().isNotEmpty()) {
                                postClassifiedViewModel.setMinMaxPriceRangeZipCode(
                                    "",
                                    maxValue,
                                    zipCode.text.toString()
                                )
                                findNavController().navigateUp()
                            } else {
                                dialog?.window?.decorView?.let {
                                    Snackbar.make(
                                        it,
                                        "Please enter valid ZipCode",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            postClassifiedViewModel.setMinMaxPriceRangeZipCode("", maxValue, zipCode.text.toString())
                            findNavController().navigateUp()
                        }
                    } else {
                        dialog?.window?.decorView?.let {
                            Snackbar.make(
                                it,
                                "Please enter valid price range",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else if (myLocationCheckBox.isChecked) {
                    if (zipCode.text.toString().isNotEmpty()) {
                        postClassifiedViewModel.setMinMaxPriceRangeZipCode(
                            "",
                            "",
                            zipCode.text.toString()
                        )
                        findNavController().navigateUp()
                    } else {
                        dialog?.window?.decorView?.let {
                            Snackbar.make(
                                it,
                                "Please enter valid ZipCode",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else if (zipCode.text.toString().isNotEmpty()) {
                    postClassifiedViewModel.setMinMaxPriceRangeZipCode(
                        "",
                        "",
                        zipCode.text.toString()
                    )
                    findNavController().navigateUp()
                } else {
                    dialog?.window?.decorView?.let {
                        Snackbar.make(
                            it,
                            "Please enter valid price range",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                postClassifiedViewModel.setIsMyLocationChecked(myLocationCheckBox.isChecked)

            }

            closeClassifiedBtn.setOnClickListener {
                dismiss()
            }

            clearAllBtn.setOnClickListener {
                clearAllData()
            }

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

        postClassifiedViewModel.isMyLocationCheckedInFilterScreen.observe(viewLifecycleOwner) {
            classifiedFilterBinding?.myLocationCheckBox?.isChecked = it
        }

        postClassifiedViewModel.minMaxPriceRangeZipCode.observe(viewLifecycleOwner) {
            if (it.first.isNotEmpty() || it.second.isNotEmpty() || it.third.isNotEmpty()) {
                classifiedFilterBinding?.minPriceRange?.setText(it.first)
                classifiedFilterBinding?.maxPriceRange?.setText(it.second)
                classifiedFilterBinding?.zipCode?.setText(it.third)
            }
        }


        return classifiedFilterBinding?.root


    }

    private fun clearAllData() {

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

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }*/
    }


}