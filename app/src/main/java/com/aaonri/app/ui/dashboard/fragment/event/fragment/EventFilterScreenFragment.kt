package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.event.model.EventCategoryResponseItem
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventFilterScreenBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFilterScreenFragment : Fragment() {
    var binding: FragmentEventFilterScreenBinding? = null
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var isAllSelected = false
    var isFreeSelected = false
    var isPaidSelected = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventFilterScreenBinding.inflate(layoutInflater, container, false)

        var zipCode = context?.let { PreferenceManager<String>(it)[Constant.USER_ZIP_CODE, ""] }
        binding?.apply {
            selectEventCitySpinner.setOnClickListener {
                findNavController().navigate(R.id.action_eventFilterScreenFragment_to_selectFilterEventCityBottom)
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

            selectEventCategorySpinner.setOnClickListener {
                findNavController().navigate(R.id.action_eventFilterScreenFragment_to_eventCategoryBottom2)
            }

            applyBtn.setOnClickListener {

                if (zipCodeEt.text.toString()
                        .isNotEmpty() && zipCodeEt.text.toString().length >= 5 || zipCodeEt.text.isEmpty()
                ) {
                    eventViewModel.setCityFilter(
                        if (selectEventCitySpinner.text.toString()
                                .isNotEmpty()
                        ) selectEventCitySpinner.text.toString() else ""
                    )

                    eventViewModel.setCategoryFilter(
                        if (selectEventCategorySpinner.text.toString()
                                .isNotEmpty()
                        ) selectEventCategorySpinner.text.toString() else ""
                    )

                    (zipCodeEt.text.toString().ifEmpty { "" })?.let { it1 ->
                        eventViewModel.setZipCodeInFilterScreen(
                            it1
                        )
                    }

                    eventViewModel.setIsAllSelected(isAllSelected)
                    eventViewModel.setIsFreeSelected(isFreeSelected)
                    eventViewModel.setIsPaidSelected(isPaidSelected)

                    eventViewModel.setClickedOnFilter(true)
                    eventViewModel.setIsFilterEnable(true)
                    findNavController().navigate(R.id.action_eventFilterScreenFragment_to_eventScreenFragment)

                } else {
                    showAlert("Please enter valid ZipCode")
                }

                eventViewModel.setIsMyLocationChecked(myLocationCheckBox.isChecked)

            }


            eventViewModel.selectedEventCity.observe(viewLifecycleOwner) {
                binding?.selectEventCitySpinner?.text = it
            }


            closeEventBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            clearAllBtn.setOnClickListener {
                eventViewModel.setClickOnClearAllFilterBtn(true)
                eventViewModel.setClearAllFilter(true)
            }

            allTv.setOnClickListener {

                if (!isAllSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        allTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> allTv.setTextColor(it1) }



                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        paidTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> paidTv.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        freeTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> freeTv.setTextColor(it1) }

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
                        allTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> allTv.setTextColor(it1) }
                }
                isAllSelected = !isAllSelected
                isFreeSelected = false
                isPaidSelected = false
                /* isRelevanceSelected = false
                 isDistanceSelected = false*/
            }


            paidTv.setOnClickListener {

                if (!isPaidSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        paidTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> paidTv.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        allTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> allTv.setTextColor(it1) }


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        freeTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> freeTv.setTextColor(it1) }

                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        paidTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> paidTv.setTextColor(it1) }
                }
                isPaidSelected = !isPaidSelected
                isFreeSelected = false
                isAllSelected = false
            }

            freeTv.setOnClickListener {
                if (!isFreeSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        freeTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> freeTv.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        allTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> allTv.setTextColor(it1) }
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        paidTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> paidTv.setTextColor(it1) }

                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        freeTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> freeTv.setTextColor(it1) }
                }
                isFreeSelected = !isFreeSelected
                isPaidSelected = false
                isAllSelected = false
            }
        }

        setData()

        postEventViewModel.selectedEventCategory.observe(viewLifecycleOwner) {
            binding?.selectEventCategorySpinner?.text = it.title
        }
        eventViewModel.selectedEventLocationLiveData.observe(viewLifecycleOwner) {
            binding?.selectEventCitySpinner?.text = it
        }

        eventViewModel.clearAllFilter.observe(viewLifecycleOwner) { clearAllFilter ->
            if (clearAllFilter) {

                eventViewModel.setCategoryFilter("")
                eventViewModel.setSelectedEventLocation("")
                eventViewModel.setIsMyLocationChecked(false)
                eventViewModel.setZipCodeInFilterScreen("")
                eventViewModel.setSearchQuery("")

                eventViewModel.setIsAllSelected(false)
                eventViewModel.setIsFreeSelected(false)
                eventViewModel.setIsPaidSelected(false)

                postEventViewModel.setSelectedEventCategory(
                    EventCategoryResponseItem(
                        false,
                        0,
                        0,
                        ""
                    )
                )

                eventViewModel.setEventCityList(mutableListOf())

                /*selectedFilterList.clear()*/

                binding?.apply {

                    myLocationCheckBox.isChecked = false

                    isAllSelected = false
                    isFreeSelected = false
                    isPaidSelected = false

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        allTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)?.let { it1 -> allTv.setTextColor(it1) }


                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        freeTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)?.let { it1 -> freeTv.setTextColor(it1) }

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        paidTv.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)?.let { it1 -> paidTv.setTextColor(it1) }
                }
            }
            eventViewModel.setClearAllFilter(false)
        }

        return binding?.root
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun setData() {

        binding?.selectEventCategorySpinner?.text = eventViewModel.categoryFilter
        binding?.selectEventCitySpinner?.text = eventViewModel.cityFilter
        binding?.myLocationCheckBox?.isChecked = eventViewModel.isMyLocationCheckedInFilterScreen

        if (eventViewModel.isAllSelected) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.blueBtnColor
                )
            }?.let { it2 ->
                binding?.allTv?.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.white)
                ?.let { it1 -> binding?.allTv?.setTextColor(it1) }
        }

        if (eventViewModel.isFreeSelected) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.blueBtnColor
                )
            }?.let { it2 ->
                binding?.freeTv?.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.white)
                ?.let { it1 -> binding?.freeTv?.setTextColor(it1) }
        }

        if (eventViewModel.isPaidSelected) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.blueBtnColor
                )
            }?.let { it2 ->
                binding?.paidTv?.setBackgroundColor(
                    it2
                )
            }
            context?.getColor(R.color.white)
                ?.let { it1 -> binding?.paidTv?.setTextColor(it1) }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}