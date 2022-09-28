package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.content.Intent
import android.content.res.ColorStateList
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
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.EventStaticData
import com.aaonri.app.data.event.model.PostEventRequest
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventAddressDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
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


class PostEventAddressDetailsFragment : Fragment() {
    var binding: FragmentPostEventAddressDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentPostEventAddressDetailsBinding.inflate(inflater, container, false)

        postEventViewModel.addNavigationForStepper(EventConstants.EVENT_ADDRESS_DETAILS)

        if (!postEventViewModel.isEventOffline) {
            binding?.zipCodeEt?.isEnabled = false
            binding?.zipCodeEt?.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.lightGrey))
            binding?.zipCodeEt?.hint = "Zipcode"
        } else {
            binding?.zipCodeEt?.hint = "Zipcode*"
        }

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

        val teremsAndCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
//                Toast.makeText(context, "TeremsAndCondition", Toast.LENGTH_SHORT).show()
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
//                Toast.makeText(context, "privacy", Toast.LENGTH_SHORT).show()

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
        SpanString.setSpan(teremsAndCondition, 50, 62, 0)
        SpanString.setSpan(privacy, 71, 85, 0)


        binding?.apply {

            textDesc1.text = ss
            privacyTextTv.movementMethod = LinkMovementMethod.getInstance()
            privacyTextTv.setText(SpanString, TextView.BufferType.SPANNABLE)
            privacyTextTv.isSelected = true

            textDesc1.movementMethod = LinkMovementMethod.getInstance()

            classifiedDetailsNextBtn.setOnClickListener {

                if (cityNameEt.text.toString()
                        .isNotEmpty() && cityNameEt.text.toString().length < 3
                ) {
                    showAlert("Please enter valid city name")
                } else {
                    if (postEventViewModel.isEventOffline) {
                        if (zipCodeEt.text.toString()
                                .isNotEmpty() && zipCodeEt.text.toString().length >= 5
                        ) {
                            if (socialMediaLinkEt.text.isNotEmpty() && socialMediaLinkEt.text.toString().length >= 10) {
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
                                showAlert("Please enter valid  ticket link")
                            }
                        } else {
                            showAlert("Please enter valid zip code")
                        }
                    } else {
                        zipCodeEt.isEnabled = false
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
                            postEventViewModel.getLocationByZipCode(
                                editable.toString(),
                                "US"
                            )
                        } else {
                            //invalidZipCodeTv.visibility = View.GONE
                        }
                    }
                }
            }

        }



        postEventViewModel.postEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.id.toString().isNotEmpty()) {
                        if (postEventViewModel.listOfImagesUri.isNotEmpty()) {
                            postEventViewModel.listOfImagesUri.forEachIndexed { index, uri ->
                                if (!uri.toString().startsWith("htt")) {
                                    callUploadClassifiedPicApi(
                                        uri,
                                        response.data?.id,
                                        response.data?.id,
                                    )
                                    /*when (index) {
                                        0 -> {
                                            callUploadClassifiedPicApi(
                                                uri,
                                                response.data?.id,
                                                response.data?.id,
                                                imageName = "cover.png"
                                            )
                                        }
                                        1 -> {
                                            callUploadClassifiedPicApi(
                                                uri,
                                                response.data?.id,
                                                response.data?.id,
                                                imageName = "first.png"
                                            )
                                        }
                                        2 -> {
                                            callUploadClassifiedPicApi(
                                                uri,
                                                response.data?.id,
                                                response.data?.id,
                                                imageName = "second.png"
                                            )
                                        }
                                        3 -> {
                                            callUploadClassifiedPicApi(
                                                uri,
                                                response.data?.id,
                                                response.data?.id,
                                                imageName = "third.png"
                                            )
                                        }
                                    }*/
                                }
                            }

                            findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
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
                        if (postEventViewModel.listOfImagesUri.isNotEmpty()) {
                            postEventViewModel.listOfImagesUri.forEachIndexed { index, uri ->
                                when (index) {
                                    0 -> {
                                        callUploadClassifiedPicApi(
                                            uri,
                                            response.data?.id,
                                            response.data?.id
                                        )
                                    }
                                    1 -> {

                                    }
                                    2 -> {

                                    }
                                    3 -> {

                                    }
                                }
                            }
                            findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
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
                if (zipCode.isNotEmpty()) {
                    binding?.zipCodeEt?.setText(zipCode)
                } else {
                    binding?.zipCodeEt?.isEnabled = false
                }
                binding?.landmarkEt?.setText(eventPlace)
                binding?.stateEt?.setText(state)
                binding?.socialMediaLinkEt?.setText(socialMediaLink)
                binding?.agreeCheckboxClassified?.isChecked =
                    acceptedTermsAndConditions

            }
        }

        /*postEventViewModel.eventDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    postEventAddressBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    postEventAddressBinding?.progressBar?.visibility = View.GONE
                    response.data?.apply {
                        postEventAddressBinding?.eventAddressEt1?.setText(address1)
                        postEventAddressBinding?.eventAddressEt2?.setText(address2)
                        postEventAddressBinding?.cityNameEt?.setText(city)
                        if (zipCode.isNotEmpty()) {
                            postEventAddressBinding?.zipCodeEt?.setText(zipCode)
                        } else {
                            postEventAddressBinding?.zipCodeEt?.isEnabled = false
                        }
                        postEventAddressBinding?.landmarkEt?.setText(eventPlace)
                        postEventAddressBinding?.stateEt?.setText(state)
                        postEventAddressBinding?.socialMediaLinkEt?.setText(socialMediaLink)
                    }
                }
                is Resource.Error -> {
                    postEventAddressBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }*/

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
                endDate = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_DATE]!!,
                endTime = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_TIME]!!,
                eventPlace = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK]!! else "",
                favorite = false,
                fee = if (postEventViewModel.isEventFree) 0.0 else postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_ASKING_FEE]?.toDouble()!!,
                id = 0,
                images = null,
                isActive = true,
                isFree = postEventViewModel.isEventFree,
                socialMediaLink = postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_SOCIAL_MEDIA_LINK]!!,
                startDate = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_DATE]!!,
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


    fun updateEvent() {
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
                endDate = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_DATE]!!,
                endTime = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_END_TIME]!!,
                eventPlace = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK]!! else "",
                favorite = false,
                fee = if (postEventViewModel.isEventFree) 0.0 else postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_ASKING_FEE]?.toDouble()!!,
                id = postEventViewModel.updateEventId,
                images = null,
                isActive = true,
                isFree = postEventViewModel.isEventFree,
                socialMediaLink = postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_SOCIAL_MEDIA_LINK]!!,
                startDate = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_START_DATE]!!,
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