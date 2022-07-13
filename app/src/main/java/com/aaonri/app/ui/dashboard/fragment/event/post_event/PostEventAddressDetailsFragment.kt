package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.model.PostEventRequest
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventAddressDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class PostEventAddressDetailsFragment : Fragment() {
    var postEventAddressBinding: FragmentPostEventAddressDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postEventAddressBinding =
            FragmentPostEventAddressDetailsBinding.inflate(inflater, container, false)


        if (!postEventViewModel.isEventOffline) {
            postEventAddressBinding?.zipCodeEt?.isEnabled = false
        }


        postEventAddressBinding?.apply {

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
        }

        postEventViewModel.postEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    postEventAddressBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.id.toString().isNotEmpty()) {
                        if (postEventViewModel.listOfImagesUri.isNotEmpty()) {
                            postEventViewModel.listOfImagesUri.forEach {
                                callUploadClassifiedPicApi(it, response.data?.id, response.data?.id)
                            }
                            findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                        } else {
                            findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                        }
                    }
                    postEventAddressBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    postEventAddressBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        postEventViewModel.updateEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    postEventAddressBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.id.toString().isNotEmpty()) {
                        if (postEventViewModel.listOfImagesUri.isNotEmpty()) {
                            postEventViewModel.listOfImagesUri.forEach {
                                callUploadClassifiedPicApi(
                                    it,
                                    postEventViewModel.updateEventId,
                                    response.data?.id
                                )
                            }
                            findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                        } else {
                            findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                        }
                    }
                    postEventAddressBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    postEventAddressBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        postEventViewModel.uploadPictureData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    postEventAddressBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    postEventAddressBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    postEventAddressBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        postEventViewModel.eventDetailsData.observe(viewLifecycleOwner) { response ->
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
        }

        return postEventAddressBinding?.root
    }

    private fun callUploadClassifiedPicApi(uri: Uri, id: Int?, id1: Int?) {

        val file = File(uri.toString().replace("file:", ""))

        val addId = id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val delId = "".toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage = MultipartBody.Part.createFormData("files", file.name, requestFile)

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


    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }
}