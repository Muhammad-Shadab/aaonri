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
import com.aaonri.app.R
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class ClassifiedFilterFragmentBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var classifiedFilterBinding: FragmentClassifiedFilterBinding? = null
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

                minPriceRange.stickPrefix("$")
                maxPriceRange.stickPrefix("$")

                applyBtn.setOnClickListener {

                    val minValue = minPriceRange.text.toString().replace("$", "")
                    val maxValue = maxPriceRange.text.toString().replace("$", "")

                    if (minValue.isNotEmpty()) {
                        if (minValue.toInt() in 10..30){

                        }else{
                            Toast.makeText(context, "Please enter valid price range", Toast.LENGTH_SHORT).show()
                        }
                    } else if (maxValue.isNotEmpty()) {
                        if (maxValue.toInt() in 10..30){

                        }else{
                            Toast.makeText(context, "Please enter valid price range", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // findNavController().navigateUp()
                }

                closeClassifiedBtn.setOnClickListener {
                    dismiss()
                }

                clearAllBtn.setOnClickListener {
                    clearAllData()
                }

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
                }
            }


            return classifiedFilterBinding?.root
        }

    private fun clearAllData() {

        selectedFilterList.clear()

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