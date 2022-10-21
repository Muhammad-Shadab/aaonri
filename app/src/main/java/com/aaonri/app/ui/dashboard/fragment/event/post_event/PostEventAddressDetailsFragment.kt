package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.WebViewActivity
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.EventStaticData
import com.aaonri.app.data.event.model.ImageXX
import com.aaonri.app.data.event.model.PostEventRequest
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventAddressDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.Validator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat


class PostEventAddressDetailsFragment : Fragment() {
    var binding: FragmentPostEventAddressDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var startDate = ""
    var endDate = ""
    var addId = 0
    var deletedItemList = mutableListOf<ImageXX>()
    var imageIdToBeDeleted = ""
    var isUserUploadedNewImages = false

    @SuppressLint("ClickableViewAccessibility", "ServiceCast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentPostEventAddressDetailsBinding.inflate(inflater, container, false)

        postEventViewModel.addNavigationForStepper(EventConstants.EVENT_ADDRESS_DETAILS)

        val text = resources.getString(R.string.if_you_want_event)

        val ss = SpannableString(text)

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val emailIntent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "events@aaonri.com", null
                    )
                )
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.underlineColor =
                    context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }

        ss.setSpan(clickableSpan1, 81, 98, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val SpanString = SpannableString(
            resources.getString(R.string.by_posting_an_ad)
        )

        val termsAndCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("url", "https://aaonri.com/terms-&-conditions")
                activity?.startActivity(intent)
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.underlineColor =
                    context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }
        val privacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("url", "https://aaonri.com/privacy-policy")
                activity?.startActivity(intent)
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.underlineColor =
                    context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }
        SpanString.setSpan(termsAndCondition, 50, 62, 0)
        SpanString.setSpan(privacy, 71, 85, 0)

        val inputFormat = SimpleDateFormat("MM-dd-yyyy")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDateParsed =
            postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_DATE]?.let {
                inputFormat.parse(
                    it
                )
            }

        val endDateParsed =
            postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_DATE]?.let {
                inputFormat.parse(
                    it
                )
            }

        startDate = outputFormat.format(startDateParsed)
        endDate = outputFormat.format(endDateParsed)


        binding?.apply {

            if (!postEventViewModel.isEventOffline) {
                eventAddressEt1.visibility = View.GONE
                eventAddressEt2.visibility = View.GONE
                cityNameEt.visibility = View.GONE
                zipCodeEt.visibility = View.GONE
                landmarkEt.visibility = View.GONE
                stateEt.visibility = View.GONE
            }

            textDesc1.text = ss
            privacyTextTv.movementMethod = LinkMovementMethod.getInstance()
            privacyTextTv.setText(SpanString, TextView.BufferType.SPANNABLE)
            privacyTextTv.isSelected = true

            textDesc1.movementMethod = LinkMovementMethod.getInstance()

            classifiedDetailsNextBtn.setOnClickListener {

                if (cityNameEt.text.toString()
                        .isNotEmpty() && cityNameEt.text.toString().length < 3
                ) {
                    showAlert("Please enter valid city")
                } else {
                    if (postEventViewModel.isEventOffline) {
                        if (zipCodeEt.text.toString()
                                .isNotEmpty() && zipCodeEt.text.toString().length >= 5
                        ) {
                            if (socialMediaLinkEt.text.isNotEmpty() && Validator.urlValidation(
                                    socialMediaLinkEt.text.toString()
                                )
                            ) {
                                if (landmarkEt.text.toString()
                                        .isNotEmpty() && landmarkEt.text.toString().length < 3
                                ) {
                                    showAlert("Please enter valid landmark")
                                } else {
                                    if (stateEt.text.toString()
                                            .isNotEmpty() && stateEt.text.toString().length < 3
                                    ) {
                                        showAlert("Please enter valid state ")
                                    } else {
                                        if (agreeCheckboxClassified.isChecked) {
                                            if (postEventViewModel.isUpdateEvent) {
                                                classifiedDetailsNextBtn.isEnabled = false
                                                postEventViewModel.setEventAddressDetailMap(
                                                    addressLine1 = eventAddressEt1.text.toString(),
                                                    addressLine2 = eventAddressEt2.text.toString(),
                                                    cityName = cityNameEt.text.toString(),
                                                    zipCode = zipCodeEt.text.toString(),
                                                    landmark = landmarkEt.text.toString(),
                                                    state = stateEt.text.toString(),
                                                    socialMediaLink = socialMediaLinkEt.text.toString()
                                                )
                                                updateEvent()
                                            } else {
                                                classifiedDetailsNextBtn.isEnabled = false
                                                postEventViewModel.setEventAddressDetailMap(
                                                    addressLine1 = eventAddressEt1.text.toString(),
                                                    addressLine2 = eventAddressEt2.text.toString(),
                                                    cityName = cityNameEt.text.toString(),
                                                    zipCode = zipCodeEt.text.toString(),
                                                    landmark = landmarkEt.text.toString(),
                                                    state = stateEt.text.toString(),
                                                    socialMediaLink = socialMediaLinkEt.text.toString()
                                                )
                                                postEvent()
                                            }
                                        } else {
                                            showAlert("Please agree to our Terms of Use & Privacy Policy")
                                        }
                                    }
                                }
                            } else {
                                showAlert("Please enter valid ticket link")
                            }
                        } else {
                            showAlert("Please enter valid zip code")
                        }
                    } else {
                        zipCodeEt.isEnabled = false
                        if (socialMediaLinkEt.text.isNotEmpty() && Validator.urlValidation(
                                socialMediaLinkEt.text.toString()
                            )
                        ) {
                            if (landmarkEt.text.toString()
                                    .isNotEmpty() && landmarkEt.text.toString().length < 3
                            ) {
                                showAlert("Please enter valid landmark")
                            } else {
                                if (stateEt.text.toString()
                                        .isNotEmpty() && stateEt.text.toString().length < 3
                                ) {
                                    showAlert("Please enter valid state ")
                                } else {

                                    if (agreeCheckboxClassified.isChecked) {
                                        if (postEventViewModel.isUpdateEvent) {
                                            classifiedDetailsNextBtn.isEnabled = false
                                            postEventViewModel.setEventAddressDetailMap(
                                                addressLine1 = eventAddressEt1.text.toString(),
                                                addressLine2 = eventAddressEt2.text.toString(),
                                                cityName = cityNameEt.text.toString(),
                                                zipCode = zipCodeEt.text.toString(),
                                                landmark = landmarkEt.text.toString(),
                                                state = stateEt.text.toString(),
                                                socialMediaLink = socialMediaLinkEt.text.toString()
                                            )
                                            updateEvent()
                                        } else {
                                            classifiedDetailsNextBtn.isEnabled = false
                                            postEventViewModel.setEventAddressDetailMap(
                                                addressLine1 = eventAddressEt1.text.toString(),
                                                addressLine2 = eventAddressEt2.text.toString(),
                                                cityName = cityNameEt.text.toString(),
                                                zipCode = zipCodeEt.text.toString(),
                                                landmark = landmarkEt.text.toString(),
                                                state = stateEt.text.toString(),
                                                socialMediaLink = socialMediaLinkEt.text.toString()
                                            )
                                            postEvent()
                                        }
                                    } else {
                                        showAlert("Please accept terms & condition")
                                    }
                                }
                            }
                        } else {
                            showAlert("Please enter valid ticket link")
                        }
                    }
                }
            }
            var job: Job? = null
            zipCodeEt.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(300L)
                    editable?.let {
                        if (editable.toString()
                                .isNotEmpty() && editable.toString().length >= 5
                        ) {
                            /*postEventViewModel.getLocationByZipCode(
                                editable.toString(),
                                "US"
                            )*/
                        } else {
                            //invalidZipCodeTv.visibility = View.GONE
                        }
                    }
                }
            }

            socialMediaLinkEt.setOnTouchListener { view, motionEvent ->
                if (!socialMediaLinkEt.text.toString().contains("https://")) {
                    socialMediaLinkEt.setText("https://")
                }
                socialMediaLinkEt.requestFocus()
                socialMediaLinkEt.setSelection(socialMediaLinkEt.length())
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
                return@setOnTouchListener true
            }


        }



        postEventViewModel.postEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.id.toString().isNotEmpty()) {
                        addId = response.data?.id!!
                        if (postEventViewModel.listOfImagesUri.isNotEmpty()) {
                            if (postEventViewModel.listOfImagesUri.size > 0) {
                                if (!postEventViewModel.listOfImagesUri[0].toString()
                                        .startsWith("htt")
                                ) {
                                    callUploadClassifiedPicApi(
                                        postEventViewModel.listOfImagesUri[0],
                                        response.data.id, response.data.id
                                    )
                                    postEventViewModel.listOfImagesUri.removeAt(0)
                                }
                            }
                        } else {
                            findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                        }
                    }
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        postEventViewModel.updateEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.id.toString().isNotEmpty()) {
                        addId = response.data?.id!!
                        deleteEventData(addId)

                        if (postEventViewModel.listOfImagesUri.isNotEmpty()) {
                            if (postEventViewModel.listOfImagesUri.size > 0) {
                                if (!postEventViewModel.listOfImagesUri[0].toString()
                                        .startsWith("http")
                                ) {
                                    callUploadClassifiedPicApi(
                                        postEventViewModel.listOfImagesUri[0],
                                        response.data.id, response.data.id
                                    )
                                    postEventViewModel.listOfImagesUri.removeAt(0)
                                }
                            }
                        } else {
                            findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                        }

                    }
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        postEventViewModel.uploadPictureData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (postEventViewModel.listOfImagesUri.isNotEmpty()) {
                        if (postEventViewModel.listOfImagesUri.size > 0) {
                            if (!postEventViewModel.listOfImagesUri[0].toString()
                                    .startsWith("htt")
                            ) {
                                callUploadClassifiedPicApi(
                                    postEventViewModel.listOfImagesUri[0],
                                    addId, addId
                                )
                                postEventViewModel.listOfImagesUri.removeAt(0)
                            }
                        }
                    } else {
                        findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                    }
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        if (postEventViewModel.isUpdateEvent) {

            val eventDetails = EventStaticData.getEventDetailsData()

            eventDetails?.apply {
                binding?.eventAddressEt1?.setText(address1)
                binding?.eventAddressEt2?.setText(address2)
                binding?.cityNameEt?.setText(city)
                if (zipCode?.isNotEmpty() == true) {
                    binding?.zipCodeEt?.setText(zipCode)
                } /*else {
                    binding?.zipCodeEt?.isEnabled = true
                }*/
                binding?.landmarkEt?.setText(eventPlace)
                binding?.stateEt?.setText(state)
                binding?.socialMediaLinkEt?.setText(socialMediaLink)
                binding?.agreeCheckboxClassified?.isChecked =
                    acceptedTermsAndConditions

                /** Finding deleted images **/
                var data: ImageXX? = null
                eventDetails.images.forEachIndexed { index, userAdsImage ->
                    val innerIndex =
                        postEventViewModel.listOfImagesUri.indexOfFirst { it.toString() == "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}" }
                    if (innerIndex == -1) {
                        data = eventDetails.images[index]
                        if (!deletedItemList.contains(data)) {
                            deletedItemList.add(data!!)
                        }
                    }
                }

                var i = 0
                var len = eventDetails.images.size
                deletedItemList.forEach {
                    i++
                    imageIdToBeDeleted += "${it.imageId}${if (i != len) "," else ""}"
                }

            }
        }


        postEventViewModel.zipCodeData.observe(
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    if (response.data?.result?.isNotEmpty() == true) {
                        binding?.cityNameEt?.setText(
                            response.data.result.getOrNull(
                                0
                            )?.district.toString()
                        )
                        binding?.stateEt?.setText(response.data.result.getOrNull(0)?.state.toString())

                        /* cityName = response.data.result.getOrNull(0)?.province.toString()

                         stateName = response.data.result.getOrNull(0)?.state.toString()*/

                    } else {

                    }

                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }

        return binding?.root
    }

    private fun deleteEventData(eventId: Int) {
        val addId = eventId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val delId = imageIdToBeDeleted.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        postEventViewModel.deleteEventPicture(addId, delId)
    }

    private fun callUploadClassifiedPicApi(uri: Uri, id: Int?, id1: Int?) {

        val file = File(uri.toString().replace("file:", ""))

        val addId = id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val delId = "".toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage = MultipartBody.Part.createFormData("files", "", requestFile)

        postEventViewModel.uploadEventPicture(requestImage, addId, delId)
    }

    private fun postEvent() {
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        postEventViewModel.postEvent(
            PostEventRequest(
                acceptedTermsAndConditions = true,
                address1 = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LINE_1]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LINE_1]!! else "",
                address2 = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LINE_2]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LINE_2]!! else "",
                approved = false,
                category = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_CATEGORY]!!,
                city = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_CITY]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_CITY]!! else "",
                createdBy = if (email?.isNotEmpty() == true) email else "",
                createdOn = "",
                delImages = null,
                description = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_DESC]!!,
                endDate = endDate,
                endTime = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_TIME]!!,
                eventPlace = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK]!! else "",
                favorite = false,
                fee = if (postEventViewModel.isEventFree) 0.0 else postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_ASKING_FEE]?.toDouble()!!,
                id = 0,
                images = null,
                isActive = true,
                isFree = postEventViewModel.isEventFree,
                socialMediaLink = postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_SOCIAL_MEDIA_LINK]!!,
                startDate = startDate,
                startTime = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_TIME]!!,
                state = postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_STATE]!!,
                timeZone = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_TIMEZONE]!!,
                title = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_TITLE]!!,
                totalFavourite = 0,
                totalVisiting = 0,
                zipCode = postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_ZIPCODE]!!
            )
        )
    }


    private fun updateEvent() {
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        postEventViewModel.updateEvent(
            PostEventRequest(
                acceptedTermsAndConditions = true,
                address1 = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LINE_1]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LINE_1]!! else "",
                address2 = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LINE_2]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LINE_2]!! else "",
                approved = false,
                category = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_CATEGORY]!!,
                city = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_CITY]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_CITY]!! else "",
                createdBy = if (email?.isNotEmpty() == true) email else "",
                createdOn = "",
                delImages = null,
                description = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_DESC]!!,
                endDate = endDate,
                endTime = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_TIME]!!,
                eventPlace = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK]!! else "",
                favorite = false,
                fee = if (postEventViewModel.isEventFree) 0.0 else postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_ASKING_FEE]?.toDouble()!!,
                id = postEventViewModel.updateEventId,
                images = null,
                isActive = true,
                isFree = postEventViewModel.isEventFree,
                socialMediaLink = postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_SOCIAL_MEDIA_LINK]!!,
                startDate = startDate,
                startTime = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_TIME]!!,
                state = postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_STATE]!!,
                timeZone = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_TIMEZONE]!!,
                title = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_TITLE]!!,
                totalFavourite = 0,
                totalVisiting = 0,
                zipCode = postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_ZIPCODE]!!
            )
        )
    }


    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}