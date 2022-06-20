package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedFilterBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ClassifiedFilterFragmentBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var classifiedFilterBinding: FragmentClassifiedFilterBinding? = null
    var isMinPriceRangeSelected = true
    var isMaxPriceRangeSelected = true
    var isDatePublishedSelected = false
    var isRelevanceSelected = false
    var isDistanceSelected = false
    var isPriceHighToLowSelected = false
    var isPriceLowToHighSelected = false
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
                postClassifiedViewModel.setFilterData(selectedFilterList)
                findNavController().navigateUp()
            }

            closeClassifiedBtn.setOnClickListener {
                dismiss()
            }

            clearAllBtn.setOnClickListener {
                clearAllData()
            }
            /*myLocationCheckBox.setOnCheckedChangeListener { p0, p1 ->
                if (p1) {
                    zipCode.setText(userZip)
                    zipCode.isEnabled = false
                } else {
                    zipCode.setText("")
                    zipCode.isEnabled = true
                }
            }*/

            postClassifiedViewModel.filterSelectedDataList.observe(viewLifecycleOwner) {
                selectedFilterList = it
                if (it.contains("Min Price")) {
                    isMinPriceRangeSelected = false
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        minPriceRange.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)?.let { it1 -> minPriceRange.setTextColor(it1) }
                }

                if (it.contains("Max Price")) {
                    isMaxPriceRangeSelected = false
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        maxPriceRange.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)?.let { it1 -> maxPriceRange.setTextColor(it1) }
                }

                if (it.contains("Date Published")) {
                    isDatePublishedSelected = true
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
                    context?.getColor(R.color.white)?.let { it1 -> datePublished.setTextColor(it1) }
                }

                if (it.contains("Relevance")) {
                    isRelevanceSelected = true
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
                    context?.getColor(R.color.white)?.let { it1 -> relevance.setTextColor(it1) }
                }

                if (it.contains("Distance")) {
                    isDistanceSelected = true
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
                    context?.getColor(R.color.white)?.let { it1 -> distance.setTextColor(it1) }
                }

                if (it.contains("Price Low to High")) {
                    isPriceLowToHighSelected = true
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
                }

                if (it.contains("Price High to Low")) {
                    isPriceHighToLowSelected = true
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
                }


            }


            minPriceRange.setOnClickListener {
                if (selectedFilterList.contains("Min Price")) {
                    selectedFilterList.remove("Min Price")
                } else {
                    selectedFilterList.remove("Max Price")
                    selectedFilterList.add("Min Price")
                }

                if (isMinPriceRangeSelected) {
                    isMaxPriceRangeSelected = true
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
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        minPriceRange.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)?.let { it1 -> minPriceRange.setTextColor(it1) }
                    isMinPriceRangeSelected = false
                } else {
                    isMinPriceRangeSelected = true
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
                }

            }

            maxPriceRange.setOnClickListener {

                if (selectedFilterList.contains("Max Price")) {
                    selectedFilterList.remove("Max Price")
                } else {
                    selectedFilterList.remove("Min Price")
                    selectedFilterList.add("Max Price")
                }

                if (isMaxPriceRangeSelected) {
                    isMinPriceRangeSelected = true
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
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        maxPriceRange.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)?.let { it1 -> maxPriceRange.setTextColor(it1) }
                    isMaxPriceRangeSelected = false
                } else {
                    isMaxPriceRangeSelected = true
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
                }
            }
            datePublished.setOnClickListener {
                if (selectedFilterList.contains("Date Published")) {
                    selectedFilterList.remove("Date Published")
                } else {
                    selectedFilterList.add("Date Published")
                }
                isDatePublishedSelected = !isDatePublishedSelected
                if (isDatePublishedSelected) {
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
                    context?.getColor(R.color.white)?.let { it1 -> datePublished.setTextColor(it1) }
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
                    context?.getColor(R.color.black)?.let { it1 -> datePublished.setTextColor(it1) }
                }
            }

            relevance.setOnClickListener {

                if (selectedFilterList.contains("Relevance")) {
                    selectedFilterList.remove("Relevance")
                } else {
                    selectedFilterList.add("Relevance")
                }

                isRelevanceSelected = !isRelevanceSelected
                if (isRelevanceSelected) {
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
                    context?.getColor(R.color.white)?.let { it1 -> relevance.setTextColor(it1) }
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
                    context?.getColor(R.color.black)?.let { it1 -> relevance.setTextColor(it1) }
                }

            }
            distance.setOnClickListener {

                if (selectedFilterList.contains("Distance")) {
                    selectedFilterList.remove("Distance")
                } else {
                    selectedFilterList.add("Distance")
                }

                isDistanceSelected = !isDistanceSelected
                if (isDistanceSelected) {
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
                    context?.getColor(R.color.white)?.let { it1 -> distance.setTextColor(it1) }
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
                    context?.getColor(R.color.black)?.let { it1 -> distance.setTextColor(it1) }
                }

            }
            priceLowToHigh.setOnClickListener {

                if (selectedFilterList.contains("Price Low to High")) {
                    selectedFilterList.remove("Price Low to High")
                } else {
                    selectedFilterList.add("Price Low to High")
                }

                isPriceLowToHighSelected = !isPriceLowToHighSelected
                if (isPriceLowToHighSelected) {
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

            }
            priceHighToLow.setOnClickListener {

                if (selectedFilterList.contains("Price High to Low")) {
                    selectedFilterList.remove("Price High to Low")
                } else {
                    selectedFilterList.add("Price High to Low")
                }

                isPriceHighToLowSelected = !isPriceHighToLowSelected
                if (isPriceHighToLowSelected) {
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

            }
        }


        return classifiedFilterBinding?.root
    }

    private fun clearAllData() {

        selectedFilterList.clear()

        classifiedFilterBinding?.apply {

            myLocationCheckBox.isChecked = false

            zipCode.setText("")

            isMinPriceRangeSelected = true
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

            isMaxPriceRangeSelected = true
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

}