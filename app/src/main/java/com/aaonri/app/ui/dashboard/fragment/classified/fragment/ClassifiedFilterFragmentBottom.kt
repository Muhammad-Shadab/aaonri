package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentClassifiedFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ClassifiedFilterFragmentBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var classifiedFilterBinding: FragmentClassifiedFilterBinding? = null
    var isMinPriceRangeSelected = true
    var isMaxPriceRangeSelected = true
    var isDatePublishedSelected = false
    var isRelevanceSelected = false
    var isDistanceSelected = false
    var isPriceHighToLowSelected = false
    var isPriceLowToHighSelected = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        classifiedFilterBinding =
            FragmentClassifiedFilterBinding.inflate(inflater, container, false)

        classifiedFilterBinding?.apply {
            closeClassifiedBtn.setOnClickListener {
                dismiss()
            }

            clearAllBtn.setOnClickListener {
                clearAllData()
            }

            minPriceRange.setOnClickListener {
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