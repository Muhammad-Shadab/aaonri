package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.ImmigrationFilterModel
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationFilterBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class ImmigrationFilterFragment : Fragment() {
    var binding: FragmentImmigrationFilterBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()

    var startDate = ""
    var endDate = ""
    var selectedDate = ""
    var isActiveDiscussionSelected = false
    var isAtLeastOneDiscussionSelected = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImmigrationFilterBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            /*selectCategorySpinner.setOnClickListener {
                val action =
                    ImmigrationFilterFragmentDirections.actionImmigrationFilterFragmentToImmigrationCategoryBottomSheet(
                        "FromFilterScreen"
                    )
                findNavController().navigate(action)
            }*/

            closeBtn.setOnClickListener {
                activity?.onBackPressed()
            }

            selectStartDate.setOnClickListener {
                getSelectedDate(selectStartDate, true)
            }

            selectEndDate.setOnClickListener {
                if (selectStartDate.text.isNotEmpty()) {
                    selectedDate = selectStartDate.text.toString()
                    getSelectedDate(selectEndDate, false)
                } else {
                    showAlert("Please select start date first")
                }
            }

            applyBtn.setOnClickListener {
                immigrationViewModel.setFilterData(
                    ImmigrationFilterModel(
                        startDate = startDate,
                        endDate = endDate,
                        activeDiscussion = isActiveDiscussionSelected,
                        atLeastOnDiscussion = isAtLeastOneDiscussionSelected
                    )
                )
                activity?.onBackPressed()
            }

            clearAllBtn.setOnClickListener {

            }

            activeDiscussion.setOnClickListener {
                isActiveDiscussionSelected = !isActiveDiscussionSelected
                if (isActiveDiscussionSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        activeDiscussion.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> activeDiscussion.setTextColor(it1) }
                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        activeDiscussion.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> activeDiscussion.setTextColor(it1) }
                }
            }

            atLeastOneResponse.setOnClickListener {
                isAtLeastOneDiscussionSelected = !isAtLeastOneDiscussionSelected
                if (isAtLeastOneDiscussionSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        atLeastOneResponse.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> atLeastOneResponse.setTextColor(it1) }
                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        atLeastOneResponse.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> atLeastOneResponse.setTextColor(it1) }
                }
            }
        }

        immigrationViewModel.immigrationFilterData.observe(viewLifecycleOwner) {
            if (it.startDate?.isNotEmpty() == true) {
                binding?.selectStartDate?.text = it.startDate
            }
            if (it.endDate?.isNotEmpty() == true) {
                binding?.selectEndDate?.text = it.endDate
            }
        }

        /* immigrationViewModel.selectedImmigrationFilterCategory.observe(viewLifecycleOwner) {
             binding?.selectCategorySpinner?.text = it.discCatValue
         }*/

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(true)
                    immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(true)
                    findNavController().navigateUp()
                }
            })

        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSelectedDate(selectstartDate: TextView? = null, isStartDate: Boolean) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var selectedMonth: String
        var seletedDay: String
        val datepicker = context?.let { it1 ->
            DatePickerDialog(
                it1,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    selectedMonth = { monthOfYear + 1 }.toString()
                    seletedDay = dayOfMonth.toString()
                    if (monthOfYear + 1 < 10) {
                        selectedMonth = "0${monthOfYear + 1}"
                    }
                    if (dayOfMonth < 10) {
                        seletedDay = "0$dayOfMonth"
                    }
                    selectedDate = "${year}-${selectedMonth}-${seletedDay}"
                    selectstartDate?.text = selectedDate
                    if (isStartDate) {
                        endDate = ""
                        binding?.selectEndDate?.text = ""
                        startDate = selectedDate
                    } else {
                        endDate = selectedDate
                    }

                },
                year,
                month,
                day
            )
        }
        try {
            if (isStartDate) {
                datepicker?.datePicker?.minDate = System.currentTimeMillis() - 1000
            } else {
                val date = SimpleDateFormat("yyyy-MM-dd").parse(selectedDate)
                datepicker?.datePicker?.minDate = date.time - 1000 + (1000 * 60 * 60 * 24)

            }
        } catch (e: Exception) {
//            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        datepicker?.show()
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

}
