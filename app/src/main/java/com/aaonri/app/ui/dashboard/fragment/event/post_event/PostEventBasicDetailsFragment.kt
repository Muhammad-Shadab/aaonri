package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventBasicDetailsBinding
import com.aaonri.app.utils.DecimalDigitsInputFilter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PostEventBasicDetailsFragment : Fragment() {
    var postEventBinding: FragmentPostEventBasicDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    val months =
        arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    var date: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postEventBinding = FragmentPostEventBasicDetailsBinding.inflate(inflater, container, false)

        postEventBinding?.apply {

            askingFee.filters = arrayOf(DecimalDigitsInputFilter(2))

            selectCategoryEvent.text = "categories"
            eventTimezone.text = "EST"

            addressDetailsNextBtn.setOnClickListener {

                if (titleEvent.text.toString().isNotEmpty() && titleEvent.text.trim()
                        .toString().length >= 3
                ) {
                    if (selectCategoryEvent.text.toString().isNotEmpty()) {

                        if (selectstartDate.text.toString().isNotEmpty()) {

                            if (selectStartTime.text.toString().isNotEmpty()) {

                                if (selectEndDate.text.toString().isNotEmpty()) {

                                    if (selectEndTime.text.toString().isNotEmpty()) {

                                        if (eventTimezone.text.toString().isNotEmpty()) {
                                            if (askingFee.text.toString().isNotEmpty()
                                            ) {
                                                if (askingFee.text.toString()
                                                        .toDouble() < 999999999 && askingFee.text.toString()
                                                        .toDouble() > 0
                                                ) {
                                                    if (eventDescEt.text.isNotEmpty()) {
                                                        findNavController().navigate(R.id.action_postEventBasicDetailsFragment_to_uploadEventPicFragment)
                                                    } else {
                                                        showAlert("Please enter valid event description")
                                                    }
                                                } else {
                                                    showAlert("Please enter valid fee")
                                                }
                                            } else {
                                                showAlert("Please enter valid fee")
                                            }
                                        } else {
                                            showAlert("Please enter valid timezone")
                                        }
                                    } else {
                                        showAlert("Please enter valid end time")
                                    }
                                } else {
                                    showAlert("Please enter valid end date")
                                }
                            } else {
                                showAlert("Please enter valid start time")
                            }
                        } else {
                            showAlert("Please enter valid start date")
                        }
                    } else {
                        showAlert("Please select valid category")
                    }

                } else {
                    showAlert("Please enter valid event title")
                }
            }
            selectCategoryEvent.setOnClickListener {
                findNavController().navigate(R.id.action_postEventBasicDetailsFragment_to_eventCategoryBottom)
            }
            selectstartDate.setOnClickListener {
                getSelectedDate(selectstartDate)
            }
            selectEndDate.setOnClickListener {
                getSelectedDate(selectEndDate)
            }
            selectStartTime.setOnClickListener {
                getSelectedTime(selectStartTime)
            }
            selectEndTime.setOnClickListener {
                getSelectedTime(selectEndTime)
            }
        }

        postEventViewModel.getEventCategory()

        return postEventBinding?.root
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun getSelectedDate(selectstartDate: TextView? = null) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = context?.let { it1 ->
            DatePickerDialog(
                it1,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    selectstartDate?.text = "${months[monthOfYear]} $dayOfMonth $year"
                },
                year,
                month,
                day
            )
        }
        datepicker?.datePicker?.minDate = System.currentTimeMillis() - 1000;
        datepicker?.show()
    }

    private fun getSelectedTime(selectStartTime: TextView) {
        var ampm = ""
        var hoursOfTheDay: Int
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                hoursOfTheDay = hourOfDay
                if (hoursOfTheDay == 0) {
                    hoursOfTheDay += 12
                    ampm = "AM"
                } else if (hoursOfTheDay == 12) {
                    ampm = "PM"
                } else if (hoursOfTheDay > 12) {
                    hoursOfTheDay -= 12
                    ampm = "PM"
                } else {
                    ampm = "AM"
                }
                if (hourOfDay < 10) {
                }
                selectStartTime.text = "$hoursOfTheDay:$minute $ampm"
            }, hour, minute, false
        )
        mTimePicker.show()

    }

}