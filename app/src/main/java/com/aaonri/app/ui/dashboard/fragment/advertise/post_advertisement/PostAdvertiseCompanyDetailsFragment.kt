package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertiseCompanyDetailsFrgamentBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.aaonri.app.utils.Validator

@AndroidEntryPoint
class PostAdvertiseCompanyDetailsFragment : Fragment() {
    var detailsBinding: FragmentPostAdvertiseCompanyDetailsFrgamentBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsBinding =
            FragmentPostAdvertiseCompanyDetailsFrgamentBinding.inflate(inflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        detailsBinding?.apply {

            companyEmailEt.setText(email)

            advertiseDetailsNextBtn.setOnClickListener {
                if (companyProfessionEt.text.toString().length < 3) {
                    showAlert("Please enter valid Product / Services")
                } else if (companyLinkEt.text.toString().length < 10) {
                    showAlert("Please enter valid Link")
                } else {
                    if (companyNameEt.text.toString().length >= 3) {
                        if (companyAddress.text.toString().length >= 3) {
                            if (companyMobileEt.text.toString().length >= 3) {
                                if (Validator.emailValidation(companyEmailEt.text.toString())) {
                                    if (advertiseDescEt.text.toString().length >= 3) {
                                        postAdvertiseViewModel.advertiseBasicDetails(
                                            companyName = companyNameEt.text.toString(),
                                            location = companyAddress.text.toString(),
                                            phoneNumber = companyMobileEt.text.toString(),
                                            email = companyEmailEt.text.toString(),
                                            services = companyProfessionEt.text.toString(),
                                            link = companyLinkEt.text.toString(),
                                            description = advertiseDescEt.text.toString()
                                        )
                                        findNavController().navigate(R.id.action_postAdvertiseCompanyDetailsFrgament_to_postAdvertisementbasicDetailsFragment)
                                    } else {
                                        showAlert("Please enter valid Advertise Description")
                                    }
                                } else {
                                    showAlert("Please enter valid Email")
                                }
                            } else {
                                showAlert("Please enter valid Mobile Number")
                            }
                        } else {
                            showAlert("Please enter valid Address")
                        }
                    } else {
                        showAlert("Please enter valid Company Name")
                    }
                }
            }
        }
        return detailsBinding?.root
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