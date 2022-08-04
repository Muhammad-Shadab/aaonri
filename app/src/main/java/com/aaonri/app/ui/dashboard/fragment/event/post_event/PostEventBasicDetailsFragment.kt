package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.EventStaticData
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventBasicDetailsBinding
import com.aaonri.app.ui.dashboard.fragment.classified.RichTextEditor
import com.aaonri.app.utils.DecimalDigitsInputFilter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class PostEventBasicDetailsFragment : Fragment() {
    var postEventBinding: FragmentPostEventBasicDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var description: String? = ""

    var isDateValid = false
    var startHour: Int ? = null
    var startMin: Int ? = null

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("result")
                if (data?.isNotEmpty() == true) {
                    postEventBinding?.eventDescEt?.fromHtml(data.trim())
                    description = data.trim()
                } else {
                    postEventBinding?.eventDescEt?.text = ""
                }
            }
        }

    var selectedDate = ""
    var startTime = ""
    var endTime = ""
    var startDate = ""
    var endDate = ""
    val parseFormat = SimpleDateFormat("HH:mm")
    val displayFormat = SimpleDateFormat("hh:mm a")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postEventBinding = FragmentPostEventBasicDetailsBinding.inflate(inflater, container, false)

        setData()

        postEventViewModel.addNavigationForStepper(EventConstants.EVENT_BASIC_DETAILS)

        postEventBinding?.apply {

            eventDescEt.textSize = 16F

            eventDescEt.setMovementMethod(ScrollingMovementMethod())
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

                                            if (askingFee.text.toString()
                                                    .isNotEmpty() && askingFee.text.toString()
                                                    .toDouble() < 999999999 && askingFee.text.toString()
                                                    .toDouble() > 0 || isFreeEntryCheckBox.isChecked
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
                                                        eventStartDate = startDate.ifEmpty { selectstartDate.text.toString() },
                                                        eventStartTime = startTime.ifEmpty { selectStartTime.text.toString() },
                                                        eventEndDate = endDate.ifEmpty { selectEndDate.text.toString() },
                                                        eventEndTime = endTime.ifEmpty { selectEndTime.text.toString() },
                                                        eventTimeZone = eventTimezone.text.toString(),
                                                        eventFee = askingFee.text.toString(),
                                                        eventDesc = if (description?.isNotEmpty() == true) description!!.trim() else ""
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
            eventDescEt.addTextChangedListener { editable ->
                descLength.text = "${editable.toString().length}/2000"
            }
            selectCategoryEvent.setOnClickListener {
                findNavController().navigate(R.id.action_postEventBasicDetailsFragment_to_eventCategoryBottom)
            }
            eventTimezone.setOnClickListener {
                findNavController().navigate(R.id.action_postEventBasicDetailsFragment_to_eventTimeZoneBottom)
            }
            selectstartDate.setOnClickListener {

                getSelectedDate(selectstartDate, true)

            }
            selectEndDate.setOnClickListener {
                if (selectstartDate.text.isNotEmpty()) {
                    selectedDate = selectstartDate.text.toString()
                    getSelectedDate(selectEndDate, false)
                } else {
                    showAlert("Please select start date first")
                }
            }
            selectStartTime.setOnClickListener {
                if(selectstartDate.text.isNotEmpty()) {
                    getSelectedTime(selectStartTime, true)
                }
                else {
                    showAlert("Please select start date first")
                }            }
            selectEndTime.setOnClickListener {
                if(selectEndDate.text.isNotEmpty()) {
                    getSelectedTime(selectEndTime, false)
                }
                else{
                    showAlert("Please select end date first")
                }

            }

            /*    selectstartDate.addTextChangedListener {
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
                }*/
//            selectEndDate.addTextChangedListener {
//                if (selectEndDate.text.toString().isNotEmpty() && selectstartDate.text.toString()
//                        .isNotEmpty()
//                ) {
//                    isDateValid =
//                        if (selectstartDate.text.toString() == selectEndDate.text.toString()) {
//                            showAlert("End Date should be more then Current Date.")
//                            false
//                        } else {
//                            true
//                        }
//                }
//            }

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

            eventDescEt.setOnClickListener {
                val intent = Intent(context, RichTextEditor::class.java)
                intent.putExtra("data", description)
                intent.putExtra("placeholder", "Please describe about the event*")
                resultLauncher.launch(intent)
            }
        }



        postEventViewModel.selectedEventCategory.observe(viewLifecycleOwner) {
            postEventBinding?.selectCategoryEvent?.text = it.title
        }

        postEventViewModel.selectedEventTimeZone.observe(viewLifecycleOwner) {
            postEventBinding?.eventTimezone?.text = it
        }

        if (postEventViewModel.isUpdateEvent) {

            val eventDetails = EventStaticData.getEventDetailsData()

            if (postEventViewModel.isNavigateBackBasicDetails) {
                setData()
                postEventViewModel.setIsNavigateBackToBasicDetails(false)
            } else {
                val uploadedImages = mutableListOf<Uri>()
                if (eventDetails?.zipCode?.isNotEmpty() == true) {
                    postEventBinding?.offlineRadioBtn?.isChecked = true
                } else {
                    postEventBinding?.onlineRdaioBtn?.isChecked = true
                }
                postEventBinding?.titleEvent?.setText(eventDetails?.title)
                postEventBinding?.selectCategoryEvent?.text = eventDetails?.category
                postEventBinding?.selectstartDate?.text =
                    eventDetails?.startDate?.split("T")?.get(0)
                postEventBinding?.selectStartTime?.text =displayFormat.format(parseFormat.parse(eventDetails?.startTime)).toString()
                postEventBinding?.selectEndDate?.text =
                    eventDetails?.endDate?.split("T")?.get(0)
                postEventBinding?.selectEndTime?.text = displayFormat.format(parseFormat.parse(eventDetails?.endTime)).toString()
                postEventBinding?.eventTimezone?.text = eventDetails?.timeZone
                postEventBinding?.eventDescEt?.fromHtml(eventDetails?.description)

                if (eventDetails?.fee != null) {
                    if (eventDetails.fee > 0) {
                        val random = eventDetails.fee

                        val df = DecimalFormat("####")
                        df.roundingMode = RoundingMode.DOWN
                        val roundoff = df.format(random)
                        postEventBinding?.askingFee?.setText(roundoff)
                    } else {
                        postEventBinding?.isFreeEntryCheckBox?.isChecked = true
                        postEventBinding?.askingFee?.isEnabled = false
                    }
                } else {
                    //Toast.makeText(context, "else condition", Toast.LENGTH_SHORT).show()
                }

                eventDetails?.images?.forEach {
                    uploadedImages.add("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${it.imagePath}".toUri())
                }
                if (uploadedImages.isNotEmpty()) {
                    postEventViewModel.setListOfUploadImagesUri(uploadedImages.distinct() as MutableList<Uri>)
                }

                description = eventDetails?.description

                /*eventDetails?.description?.let {
                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set("description", it)
                }*/
            }
        }



        if (EventStaticData.getEventCategory().isEmpty()) {
            postEventViewModel.getEventCategory()
        }

        /*postEventViewModel.eventDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    postEventBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    postEventBinding?.progressBar?.visibility = View.GONE

                    if (postEventViewModel.isNavigateBackBasicDetails) {
                        setData()
                        postEventViewModel.setIsNavigateBackToBasicDetails(false)
                    } else {
                        val uploadedImages = mutableListOf<Uri>()
                        if (response.data?.zipCode?.isNotEmpty() == true) {
                            postEventBinding?.offlineRadioBtn?.isChecked = true
                        } else {
                            postEventBinding?.onlineRdaioBtn?.isChecked = true
                        }
                        postEventBinding?.titleEvent?.setText(response.data?.title)
                        postEventBinding?.selectCategoryEvent?.text = response.data?.category
                        postEventBinding?.selectstartDate?.text =
                            response.data?.startDate?.split("T")?.get(0)
                        postEventBinding?.selectStartTime?.text = response.data?.startTime
                        postEventBinding?.selectEndDate?.text =
                            response.data?.endDate?.split("T")?.get(0)
                        postEventBinding?.selectEndTime?.text = response.data?.endTime
                        postEventBinding?.eventTimezone?.text = response.data?.timeZone

                        if (response.data?.fee != null) {
                            if (response.data.fee > 0) {
                                postEventBinding?.askingFee?.setText(response.data.fee.toString())
                            } else {
                                postEventBinding?.isFreeEntryCheckBox?.isChecked = true
                                postEventBinding?.askingFee?.isEnabled = false
                            }
                        } else {
                            //Toast.makeText(context, "else condition", Toast.LENGTH_SHORT).show()
                        }
                        postEventBinding?.eventDescEt?.text =
                            Html.fromHtml(response.data?.description)

                        response.data?.images?.forEach {
                            uploadedImages.add("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${it.imagePath}".toUri())
                        }
                        if (uploadedImages.isNotEmpty()) {
                            postEventViewModel.setListOfUploadImagesUri(uploadedImages.distinct() as MutableList<Uri>)
                        }

                        response.data?.description?.let {
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set("description", it)
                        }
                    }
                }
                is Resource.Error -> {
                    postEventBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }*/

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })

        return postEventBinding?.root
    }

    private fun setData() {

        postEventViewModel.apply {
            eventBasicDetailMap.let {
                postEventBinding?.titleEvent?.setText(it[EventConstants.EVENT_TITLE])
                postEventBinding?.selectCategoryEvent?.text = it[EventConstants.EVENT_CATEGORY]
                postEventBinding?.selectstartDate?.text = it[EventConstants.EVENT_START_DATE]
                postEventBinding?.selectStartTime?.text = it[EventConstants.EVENT_START_TIME]
                postEventBinding?.selectEndDate?.text = it[EventConstants.EVENT_END_DATE]
                postEventBinding?.selectEndTime?.text = it[EventConstants.EVENT_END_TIME]
                postEventBinding?.eventTimezone?.text = it[EventConstants.EVENT_TIMEZONE]

                if (isEventFree) {
                    postEventBinding?.isFreeEntryCheckBox?.isChecked = true
                    postEventBinding?.askingFee?.isEnabled = false
                } else {
                    postEventBinding?.askingFee?.setText(it[EventConstants.EVENT_ASKING_FEE])
                }
                if (it[EventConstants.EVENT_DESC]?.isNotEmpty() == true) {
                    postEventBinding?.eventDescEt?.fromHtml(it[EventConstants.EVENT_DESC])
                }
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSelectedDate(selectstartDate: TextView? = null, isStartdate: Boolean) {
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
                    selectstartDate?.text = "$selectedDate"
                    if (isStartdate) {

                        endDate = ""
                        postEventBinding?.selectEndDate?.text = ""
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
            if (isStartdate) {
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

    private fun getSelectedTime(selectStartTime: TextView, isStartTime: Boolean) {
        var ampm = ""
        var getMinute = ""
        var hoursOfTheDay: Int
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        var hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        var minute = mcurrentTime.get(Calendar.MINUTE)
        var getHoursCLock :Int = 30
        var getMinutesCLock :Int = 0
        if(postEventBinding?.selectstartDate?.text.toString() == postEventBinding?.selectEndDate?.text.toString()&&getHoursCLock!=30)
        {
             hour = getHoursCLock
             minute = getMinutesCLock


        }

        mTimePicker = TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                hoursOfTheDay = hourOfDay
                 if(isStartTime)
                 {
                     getHoursCLock = hourOfDay
                     getMinutesCLock = minute
                     startHour =hourOfDay
                     startMin = minute
                 }


                val date = parseFormat.parse("$hourOfDay:$minute")

          /*      if (hoursOfTheDay == 0) {
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
                if (minute < 10) {
                    getMinute = "0$minute"
                } else {
                    getMinute = "$minute"
                }*/

                if (isStartTime) {
                    //this is for startTime
                    selectStartTime.text = displayFormat.format(date)
                    startTime = parseFormat.format(date).toString()
                } else {
                    //this is for  endTime
                    if(hourOfDay> startHour!!||hourOfDay ==startHour && startMin!! >minute) {
                        selectStartTime.text = displayFormat.format(date)
                        endTime = parseFormat.format(date).toString()
                    }
                    else{
                        showAlert("Please select valid time")
                    }
                }

            }, hour, minute, false
        )
        mTimePicker.show()
    }

    /*  override fun onResume() {
          super.onResume()

          if (Html.fromHtml(context?.let { PreferenceManager<String>(it)["description", ""] })?.trim()
                  ?.isNotEmpty() == true
          ) {
              postEventBinding?.eventDescEt?.text =
                  Html.fromHtml(context?.let { PreferenceManager<String>(it)["description", ""] })
          } else {
              postEventBinding?.eventDescEt?.text = ""
          }
      }*/

    /* override fun onResume() {
         super.onResume()
         Toast.makeText(context, context?.let { PreferenceManager<String>(it)["description", ""] }, Toast.LENGTH_SHORT).show()
         postEventBinding?.eventDescEt?.text = Html.fromHtml(context?.let { PreferenceManager<String>(it)["description", ""] })
     }*/

}




