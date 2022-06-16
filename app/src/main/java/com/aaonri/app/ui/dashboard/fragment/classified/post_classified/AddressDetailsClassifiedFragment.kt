package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.PostClassifiedRequest
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentAddressDetailsClassifiedBinding
import com.aaonri.app.utils.*
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
/*
        val title = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.TITLE].toString()

        Toast.makeText(
            context,
            "title = $title}",
            Toast.LENGTH_SHORT
        ).show()*/

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
                                if (classifiedKeywordEt.text.trim().toString().length > 3) {
                                    if (agreeCheckboxClassified.isChecked) {
                                        postClassifiedRequest(
                                            adKeywords = classifiedKeywordEt.text.toString(),
                                            adEmail = emailAddressBasicDetails.text.toString(),
                                            adPhone = "",
                                            cityName = cityNameAddressDetails.text.trim()
                                                .toString(),
                                            zipCode = zipCodeAddressDetails.text.trim().toString()
                                        )
                                        postClassifiedViewModel.addClassifiedAddressDetails(
                                            city = cityNameAddressDetails.text.toString(),
                                            zip = zipCodeAddressDetails.text.toString(),
                                            email = emailAddressBasicDetails.text.toString(),
                                            phone = "",
                                            description = classifiedKeywordEt.text.toString()
                                        )
                                        postClassifiedViewModel.addIsAgreeToAaonri(true)
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
                                if (classifiedKeywordEt.text.trim().toString().length > 3) {
                                    if (agreeCheckboxClassified.isChecked) {
                                        postClassifiedRequest(
                                            adKeywords = classifiedKeywordEt.text.toString(),
                                            adEmail = "",
                                            adPhone = phoneNumber,
                                            cityName = cityNameAddressDetails.text.trim()
                                                .toString(),
                                            zipCode = zipCodeAddressDetails.text.trim().toString()
                                        )
                                        postClassifiedViewModel.addClassifiedAddressDetails(
                                            city = cityNameAddressDetails.text.toString(),
                                            zip = zipCodeAddressDetails.text.toString(),
                                            email = "",
                                            phone = phoneNumber,
                                            description = classifiedKeywordEt.text.toString()
                                        )
                                        postClassifiedViewModel.addIsAgreeToAaonri(true)
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

        postClassifiedViewModel.postClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    addressDetailsBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    findNavController().navigate(R.id.action_addressDetailsClassifiedFragment_to_classifiedPostSuccessBottom)
                    addressDetailsBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    addressDetailsBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

        }


        return addressDetailsBinding?.root
    }

    private fun postClassifiedRequest(
        adEmail: String,
        adPhone: String,
        adKeywords: String,
        cityName: String,
        zipCode: String
    ) {
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        postClassifiedViewModel.postClassified(
            PostClassifiedRequest(
                active = true,
                adDescription = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.DESCRIPTION]!!,
                adEmail = adEmail,
                adExpireDT = "2022-02-21T06:57:24.837+0000",
                adImages = null,
                adKeywords = adKeywords,
                adLocation = cityName,
                adPhone = adPhone,
                adThumbnails = null,
                adTitle = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.TITLE]!!,
                adZip = zipCode,
                approved = false,
                askingPrice = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.ASKING_PRICE]!!.toInt(),
                category = postClassifiedViewModel.classifiedCategory,
                contactType = if (adPhone.isNotEmpty()) "Phone" else "Email",
                createdOn = "2022-02-06T06:57:24.858+0000",
                delImages = null,
                deletedOn = null,
                favorite = false,
                id = null,
                isNew = postClassifiedViewModel.isProductNewCheckBox,
                isTermsAndConditionAccepted = true,
                lastUpdated = "2022-02-06T06:57:24.858+0000",
                popularOnAaonri = false,
                rejected = false,
                rejectionReson = null,
                subCategory = postClassifiedViewModel.classifiedSubCategory,
                totalFavourite = 0,
                userAdsImages = listOf(),
                userId = email.toString()
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