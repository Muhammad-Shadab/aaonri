package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
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
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.create
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


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

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.ADDRESS_DETAILS_SCREEN)

        val text = resources.getString(R.string.your_classified_will)

        val ss = SpannableString(text)

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                val emailIntent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "adminclassifieds@aaonri.com", null
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

        ss.setSpan(clickableSpan1, 174, 201, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        addressDetailsBinding?.apply {

            textDesc1.text = ss
            textDesc1.movementMethod = LinkMovementMethod.getInstance()


            if (emailRadioBtn.isChecked) {
                emailAddressBasicDetails.setText(email)
            }

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
                    emailAddressBasicDetails.setText(email)
                    emailAddressBasicDetails.visibility = View.VISIBLE
                } else {
                    emailAddressBasicDetails.setText("")
                    emailTv.setTextColor(Color.parseColor("#979797"))
                    emailAddressBasicDetails.visibility = View.GONE
                }
            }

            classifiedDetailsNextBtn.setOnClickListener {

                val phoneNumber = phoneNumberAddressDetails.text.trim().toString().replace("-", "")


                if (cityNameAddressDetails.text.trim().toString().length >= 3
                ) {
                    if (zipCodeAddressDetails.text.trim().toString().length >= 5) {
                        if (emailRadioBtn.isChecked) {
                            if (Validator.emailValidation(
                                    emailAddressBasicDetails.text.trim().toString()
                                )
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

                    if (response.data?.id.toString().isNotEmpty()) {
                        if(postClassifiedViewModel.listOfImagesUri.isNotEmpty()){
                            postClassifiedViewModel.listOfImagesUri.forEach {
                                callUploadClassifiedPicApi(it, response.data?.id, response.data?.id)
                            }
                        }else{
                            findNavController().navigate(R.id.action_addressDetailsClassifiedFragment_to_classifiedPostSuccessBottom)
                        }
                    }

                    addressDetailsBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    addressDetailsBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

        }

        postClassifiedViewModel.uploadClassifiedPics.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    addressDetailsBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {

                    Toast.makeText(
                        context,
                        "${response.data?.message}",
                        Toast.LENGTH_SHORT
                    ).show()

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

    private fun callUploadClassifiedPicApi(uri: Uri, id: Int?, id1: Int?) {

        val file = File(uri.toString().replace("file:", ""))

        val addId = id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val delId = id1.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage = MultipartBody.Part.createFormData("files", file.name, requestFile)

        postClassifiedViewModel.uploadClassifiedPics(requestImage, addId, delId)
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
                askingPrice = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.ASKING_PRICE]!!.toDouble(),
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