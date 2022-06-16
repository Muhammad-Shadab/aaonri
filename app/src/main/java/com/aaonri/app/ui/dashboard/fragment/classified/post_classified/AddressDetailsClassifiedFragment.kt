package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentAddressDetailsClassifiedBinding
import com.aaonri.app.utils.SystemServiceUtil
import com.aaonri.app.utils.Validator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressDetailsClassifiedFragment : Fragment() {
    var addressDetailsBinding: FragmentAddressDetailsClassifiedBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var isEmailValid = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addressDetailsBinding =
            FragmentAddressDetailsClassifiedBinding.inflate(inflater, container, false)

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.ADDRESS_DETAILS_SCREEN)

        addressDetailsBinding?.apply {

            phoneRadioBtn.setOnCheckedChangeListener { p0, p1 ->
                activity?.let { view?.let { it1 -> SystemServiceUtil.closeKeyboard(it, it1) } }
                if (p1) {
                    phoneTv.setTextColor(Color.parseColor("#333333"))
                    phoneNumberAddressDetails.visibility = View.VISIBLE
                } else {
                    phoneNumberAddressDetails.setText("")
                    phoneNumberAddressDetails.visibility = View.GONE
                    phoneTv.setTextColor(Color.parseColor("#979797"))
                }
            }
            emailRadioBtn.setOnCheckedChangeListener { p0, p1 ->
                activity?.let { view?.let { it1 -> SystemServiceUtil.closeKeyboard(it, it1) } }
                if (p1) {
                    emailTv.setTextColor(Color.parseColor("#333333"))
                    emailAddressBasicDetails.visibility = View.VISIBLE
                } else {
                    emailAddressBasicDetails.setText("")
                    emailTv.setTextColor(Color.parseColor("#979797"))
                    emailAddressBasicDetails.visibility = View.GONE
                }
            }

            classifiedDetailsNextBtn.setOnClickListener {

                val phoneNumber = phoneNumberAddressDetails.text.trim().toString().replace("-", "")

                if (cityNameAddressDetails.text.trim().toString().length > 3
                ) {
                    if (zipCodeAddressDetails.text.trim().toString().length >= 5) {
                        if (emailRadioBtn.isChecked) {
                            if (isEmailValid && emailAddressBasicDetails.text.trim().toString()
                                    .isNotEmpty()
                            ) {
                                if (classifiedDescEt.text.trim().toString().length > 3) {
                                    if (agreeCheckboxClassified.isChecked) {
                                        postClassifiedViewModel.addClassifiedAddressDetails(
                                            city = cityNameAddressDetails.text.toString(),
                                            zip = zipCodeAddressDetails.text.toString(),
                                            email = emailAddressBasicDetails.text.toString(),
                                            phone = "",
                                            description = classifiedDescEt.text.toString()
                                        )
                                        postClassifiedViewModel.addIsAgreeToAaonri(true)
                                        findNavController().navigate(R.id.action_addressDetailsClassifiedFragment_to_classifiedPostSuccessBottom)
                                    } else {
                                        showAlert("Please accept terms & condition")
                                    }
                                } else {
                                    showAlert("Please enter valid classified description")
                                }
                            } else {
                                showAlert("Please enter valid email")
                            }
                        } else {
                            if (phoneNumber.length >= 10) {
                                if (classifiedDescEt.text.trim().toString().length > 3) {
                                    if (agreeCheckboxClassified.isChecked) {
                                        postClassifiedViewModel.addClassifiedAddressDetails(
                                            city = cityNameAddressDetails.text.toString(),
                                            zip = zipCodeAddressDetails.text.toString(),
                                            email = "",
                                            phone = phoneNumber,
                                            description = classifiedDescEt.text.toString()
                                        )
                                        postClassifiedViewModel.addIsAgreeToAaonri(true)
                                        findNavController().navigate(R.id.action_addressDetailsClassifiedFragment_to_classifiedPostSuccessBottom)
                                    } else {
                                        showAlert("Please accept terms & condition")
                                    }
                                } else {
                                    showAlert("Please enter valid classified description")
                                }
                            } else {
                                showAlert("Please enter valid phone number")
                            }
                        }
                    } else {
                        showAlert("Please enter valid zip code")
                    }
                } else {
                    showAlert("Please enter valid city name")
                }
            }
        }

        addressDetailsBinding?.phoneNumberAddressDetails?.addTextChangedListener(object :
            TextWatcher {
            var length_before = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                length_before = s.length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (length_before < s.length) {
                    if (s.length == 3 || s.length == 7) s.append("-")
                    if (s.length > 3) {
                        if (Character.isDigit(s[3])) s.insert(3, "-")
                    }
                    if (s.length > 7) {
                        if (Character.isDigit(s[7])) s.insert(7, "-")
                    }
                }
            }
        })


        addressDetailsBinding?.emailAddressBasicDetails?.addTextChangedListener { editable ->
            editable?.let {
                if (editable.toString().isNotEmpty() && editable.toString().length > 8) {
                    if (Validator.emailValidation(editable.toString())) {
                        isEmailValid = true
                        addressDetailsBinding?.emailValidationTv?.visibility = View.GONE
                    } else {
                        isEmailValid = false
                        addressDetailsBinding?.emailValidationTv?.visibility = View.VISIBLE
                    }
                } else {
                    isEmailValid = false
                    addressDetailsBinding?.emailValidationTv?.visibility = View.GONE
                }
            }
        }

        return addressDetailsBinding?.root
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