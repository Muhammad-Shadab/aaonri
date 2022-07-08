package com.aaonri.app.ui.dashboard.fragment.event.post_event
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventBasicDetailsBinding
import com.aaonri.app.utils.DecimalDigitsInputFilter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class PostEventBasicDetailsFragment : Fragment() {
    var postEventBinding: FragmentPostEventBasicDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    val months =
        arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    var isDateValid = false

    var selectedDate  = ""
    var startTime = ""
    var endTime = ""
    var startDate = ""
    var endDate = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postEventBinding = FragmentPostEventBasicDetailsBinding.inflate(inflater, container, false)

        setData()

        postEventBinding?.apply {

            askingFee.filters = arrayOf(DecimalDigitsInputFilter(2))

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

                                                    if (askingFee.text.toString().isNotEmpty() && askingFee.text.toString().toDouble() < 999999999 && askingFee.text.toString().toDouble() > 0  || isFreeEntryCheckBox.isChecked
                                                    ) {
                                                        if (eventDescEt.text.isNotEmpty()) {
                                                            postEventViewModel.setIsEventOffline(
                                                                offlineRadioBtn.isChecked
                                                            )
                                                            postEventViewModel.setIsEventFree(
                                                                isFreeEntryCheckBox.isChecked
                                                            )
                                                            postEventViewModel.setEventBasicDetails(
                                                                eventTitle = titleEvent.text.toString(),
                                                                eventCategory = selectCategoryEvent.text.toString(),
                                                                eventStartDate = selectstartDate.text.toString(),
                                                                eventStartTime = selectStartTime.text.toString(),
                                                                eventEndDate = selectEndDate.text.toString(),
                                                                eventEndTime = selectEndTime.text.toString(),
                                                                eventTimeZone = eventTimezone.text.toString(),
                                                                eventFee = askingFee.text.toString(),
                                                                eventDesc = eventDescEt.text.toString()
                                                            )
                                                            findNavController().navigate(R.id.action_postEventBasicDetailsFragment_to_uploadEventPicFragment)
                                                        } else {
                                                            showAlert("Please enter valid event description")
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
            eventTimezone.setOnClickListener {
                findNavController().navigate(R.id.action_postEventBasicDetailsFragment_to_eventTimeZoneBottom)
            }
            selectstartDate.setOnClickListener {
                getSelectedDate(selectstartDate,true)
            }
            selectEndDate.setOnClickListener {
                if(selectstartDate.text.isNotEmpty())
                {
                getSelectedDate(selectEndDate,false)
                }
                else{
                    showAlert("Please select start date first")
                }
            }
            selectStartTime.setOnClickListener {
                getSelectedTime(selectStartTime,true)
            }
            selectEndTime.setOnClickListener {
                getSelectedTime(selectEndTime,false)
            }

            selectstartDate.addTextChangedListener {
                if (selectEndDate.text.toString().isNotEmpty() && selectstartDate.text.toString()
                        .isNotEmpty()
                ) {
                    isDateValid =
                        if (selectstartDate.text.toString() == selectEndDate.text.toString()) {
                            showAlert("Start Date should be less then End Date.")
                            false
                        } else {
                            true
                        }
                }
            }
            selectEndDate.addTextChangedListener {
                if (selectEndDate.text.toString().isNotEmpty() && selectstartDate.text.toString()
                        .isNotEmpty()
                ) {
                    isDateValid =
                        if (selectstartDate.text.toString() == selectEndDate.text.toString()) {
                            showAlert("End Date should be more then Current Date.")
                            false
                        } else {
                            true
                        }
                }
            }

            offlineRadioBtn.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onlineRdaioBtn.isChecked = false
                }
            }

            onlineRdaioBtn.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    offlineRadioBtn.isChecked = false
                }
            }

            offlineRadioBtnCardView.setOnClickListener {
                offlineRadioBtn.isChecked = true
                onlineRdaioBtn.isChecked = false
            }
            onlineRadioBtnCardView.setOnClickListener {
                onlineRdaioBtn.isChecked = true
                offlineRadioBtn.isChecked = false
            }
            isFreeEntryCheckBox.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    askingFee.setText("")
                    askingFee.isEnabled = false
                } else {
                    askingFee.isEnabled = true
                }
            }

        }

        postEventViewModel.getEventCategory()
        postEventViewModel.selectedEventCategory.observe(viewLifecycleOwner) {
            postEventBinding?.selectCategoryEvent?.text = it.title
        }

        postEventViewModel.selectedEventTimeZone.observe(viewLifecycleOwner) {
            postEventBinding?.eventTimezone?.text = it
        }
        return postEventBinding?.root
    }

    private fun setData() {
        postEventBinding?.selectstartDate?.text =
            if (postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_DATE]?.isNotEmpty() == true) postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_DATE] else ""
        postEventBinding?.selectStartTime?.text =
            if (postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_TIME]?.isNotEmpty() == true) postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_TIME] else ""
        postEventBinding?.selectEndDate?.text =
            if (postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_DATE]?.isNotEmpty() == true) postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_DATE] else ""
        postEventBinding?.selectEndTime?.text =
            if (postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_TIME]?.isNotEmpty() == true) postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_TIME] else ""
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSelectedDate(selectstartDate: TextView? = null, isStartdate: Boolean) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var selectedMonth:String
        var seletedDay : String
        val datepicker = context?.let { it1 ->
            DatePickerDialog(
                it1,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox

                    selectstartDate?.text = "${months[monthOfYear]} $dayOfMonth $year"
                 if(isStartdate)
                 {
                    selectedMonth={monthOfYear+1}.toString()
                    seletedDay=dayOfMonth.toString()
                   if(monthOfYear+1<10)
                   {
                      selectedMonth="0${monthOfYear+1}"
                   }
                    if(dayOfMonth<10)
                    {
                        seletedDay="0$dayOfMonth"
                    }

                    selectedDate = "${seletedDay}-${selectedMonth}-${year}"
                 }

                },
                year,
                month,
                day
            )
        }
        if(isStartdate) {
            datepicker?.datePicker?.minDate = System.currentTimeMillis()-1000;
            //this is startDate
            startDate=selectedDate
        }
        else{
            val date = SimpleDateFormat("dd-MM-yyyy").parse(selectedDate)
            datepicker?.datePicker?.minDate =  date.time- 1000 + (1000 * 60 * 60 * 24*2)

            //this is for enddDate
            endDate=selectedDate
        }
        datepicker?.show()
    }

    private fun getSelectedTime(selectStartTime: TextView, isStartTime: Boolean) {
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
                if(isStartTime)
                {
                    //this is for startTime
                    startTime="$hourOfDay:$minute"
                }
                else{
                    //this is for  endTime
                    endTime="$hourOfDay:$minute"
                }
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




