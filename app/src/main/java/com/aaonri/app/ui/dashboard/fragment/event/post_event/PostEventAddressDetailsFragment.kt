package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventAddressDetailsBinding
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

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }
}