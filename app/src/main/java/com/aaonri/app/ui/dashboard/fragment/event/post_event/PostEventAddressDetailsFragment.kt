package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.model.PostEventRequest
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventAddressDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.snackbar.Snackbar


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
            Toast.makeText(context, "${postEventViewModel.isEventOffline}", Toast.LENGTH_SHORT)
                .show()
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
                                        //findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
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
                                    //findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                                } else {
                                    showAlert("Please accept terms & condition")
                                }
                            }
                        }
                    }
                }

            }
        }
        return postEventAddressBinding?.root
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
                eventPlace = if (postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_CITY]?.isNotEmpty() == true) postEventViewModel.eventAddressDetailMap[EventConstants.ADDRESS_CITY]!! else "",
                favorite = false,
                fee = postEventViewModel.eventBasicDetailMap[EventConstants.EVENT_ASKING_FEE]?.toDouble()!!,
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