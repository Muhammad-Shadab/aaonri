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
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.ClassifiedStaticData
import com.aaonri.app.data.classified.model.PostClassifiedRequest
import com.aaonri.app.data.classified.model.UserAdsImage
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentAddressDetailsClassifiedBinding
import com.aaonri.app.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


@AndroidEntryPoint
class AddressDetailsClassifiedFragment : Fragment() {
    var binding: FragmentAddressDetailsClassifiedBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var isEmailValid = false
    var addId = 0
    var deletedItemList = mutableListOf<UserAdsImage>()
    var imageIdToBeDeleted = ""
    var isUserUploadedNewImages = false
    var imagesUriWhileUpdating = mutableListOf<Uri>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentAddressDetailsClassifiedBinding.inflate(inflater, container, false)

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        val userPhoneNumber =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PHONE_NUMBER, ""] }
        val city = context?.let { PreferenceManager<String>(it)[Constant.USER_CITY, ""] }
        val zipCode = context?.let { PreferenceManager<String>(it)[Constant.USER_ZIP_CODE, ""] }

        //Toast.makeText(context, "$userPhoneNumber", Toast.LENGTH_SHORT).show()

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.ADDRESS_DETAILS_SCREEN)

        val text = resources.getString(R.string.your_classified_will)

        val ss = SpannableString(text)
        val ss1 = SpannableString(resources.getString(R.string.if_you_want))

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
        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                val emailIntent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "Classfieds@aaonri.com", null
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
        ss1.setSpan(clickableSpan2, 86, 107, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val SpanString = SpannableString(
            resources.getString(R.string.by_posting_an_ad)
        )

        val teremsAndCondition: ClickableSpan = object : ClickableSpan() {
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
        SpanString.setSpan(teremsAndCondition, 50, 62, 0)
        SpanString.setSpan(privacy, 71, 85, 0)

        binding?.apply {

            textDesc1.text = ss
            textDec2.text = ss1
            textDec3.movementMethod = LinkMovementMethod.getInstance()
            textDec3.setText(SpanString, TextView.BufferType.SPANNABLE)
            textDec3.isSelected = true
            textDesc1.movementMethod = LinkMovementMethod.getInstance()
            textDec2.movementMethod = LinkMovementMethod.getInstance()


            if (city?.isNotEmpty() == true) {
                cityNameAddressDetails.setText(city)
            }
            if (zipCode?.isNotEmpty() == true) {
                zipCodeAddressDetails.setText(zipCode)
            }

            if (emailRadioBtn.isChecked) {
                emailAddressBasicDetails.setText(email)
            }

            phoneRadioBtn.setOnCheckedChangeListener { p0, p1 ->
                activity?.let { view?.let { it1 -> SystemServiceUtil.closeKeyboard(it, it1) } }
                if (p1) {
                    phoneTv.setTextColor(Color.parseColor("#333333"))
                    phoneNumberAddressDetails.visibility = View.VISIBLE
                    emailAddressBasicDetails.setText(userPhoneNumber)
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

                if (cityNameAddressDetails.text.trim().toString().length >= 2
                ) {
                    if (zipCodeAddressDetails.text.trim().toString().length >= 5) {
                        if (emailRadioBtn.isChecked) {
                            if (Validator.emailValidation(
                                    emailAddressBasicDetails.text.trim().toString()
                                )
                            ) {
                                if (classifiedKeywordEt.text.trim().toString().length > 3) {
                                    if (agreeCheckboxClassified.isChecked) {
                                        if (postClassifiedViewModel.isUpdateClassified) {
                                            classifiedDetailsNextBtn.isEnabled = false
                                            updateClassified(
                                                adEmail = emailAddressBasicDetails.text.toString(),
                                                adPhone = "",
                                                adKeywords = classifiedKeywordEt.text.toString(),
                                                cityName = cityNameAddressDetails.text.toString(),
                                                zipCode = zipCodeAddressDetails.text.toString()
                                            )
                                        } else {
                                            classifiedDetailsNextBtn.isEnabled = false
                                            postClassifiedRequest(
                                                adEmail = emailAddressBasicDetails.text.toString(),
                                                adPhone = "",
                                                adKeywords = classifiedKeywordEt.text.toString(),
                                                cityName = cityNameAddressDetails.text.toString(),
                                                zipCode = zipCodeAddressDetails.text.toString()
                                            )
                                        }
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
                                        if (postClassifiedViewModel.isUpdateClassified) {
                                            classifiedDetailsNextBtn.isEnabled = false
                                            updateClassified(
                                                adEmail = "",
                                                adPhone = phoneNumberAddressDetails.text.toString(),
                                                adKeywords = classifiedKeywordEt.text.toString(),
                                                cityName = cityNameAddressDetails.text.toString(),
                                                zipCode = zipCodeAddressDetails.text.toString()
                                            )
                                        } else {
                                            classifiedDetailsNextBtn.isEnabled = false
                                            postClassifiedRequest(
                                                adEmail = "",
                                                adPhone = phoneNumberAddressDetails.text.toString(),
                                                adKeywords = classifiedKeywordEt.text.toString(),
                                                cityName = cityNameAddressDetails.text.toString(),
                                                zipCode = zipCodeAddressDetails.text.toString()
                                            )
                                        }
                                    } else {
                                        showAlert("Please accept terms & condition")
                                    }
                                } else {
                                    showAlert("Please enter valid classified keyword")
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

        binding?.phoneNumberAddressDetails?.addTextChangedListener(object :
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

        binding?.emailAddressBasicDetails?.addTextChangedListener { editable ->
            editable?.let {
                if (editable.toString().isNotEmpty() && editable.toString().length > 8) {
                    if (Validator.emailValidation(editable.toString())) {
                        isEmailValid = true
                        binding?.emailValidationTv?.visibility = View.GONE
                    } else {
                        isEmailValid = false
                        binding?.emailValidationTv?.visibility = View.VISIBLE
                    }
                } else {
                    isEmailValid = false
                    binding?.emailValidationTv?.visibility = View.GONE
                }
            }
        }

        postClassifiedViewModel.postClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.id.toString().isNotEmpty()) {
                        addId = response.data?.id!!
                        if (postClassifiedViewModel.listOfImagesUri.isNotEmpty()) {
                            callUploadClassifiedPicApi(response.data.id, false)
                        } else {
                            val action =
                                AddressDetailsClassifiedFragmentDirections.actionAddressDetailsClassifiedFragmentToClassifiedPostSuccessBottom()
                            findNavController().navigate(action)
                        }
                    }
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        postClassifiedViewModel.updateClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (postClassifiedViewModel.listOfImagesUri.size > 0) {
                        callUploadClassifiedPicApi(postClassifiedViewModel.updateClassifiedId, true)
                    } else {
                        val action =
                            AddressDetailsClassifiedFragmentDirections.actionAddressDetailsClassifiedFragmentToClassifiedPostSuccessBottom()
                        findNavController().navigate(action)
                    }
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (postClassifiedViewModel.isUpdateClassified) {

            val addDetails = ClassifiedStaticData.getAddDetails()

            binding?.cityNameAddressDetails?.setText(addDetails?.userAds?.adLocation)
            binding?.zipCodeAddressDetails?.setText(addDetails?.userAds?.adZip)
            binding?.classifiedKeywordEt?.setText(addDetails?.userAds?.adKeywords)
            binding?.agreeCheckboxClassified?.isChecked =
                addDetails?.userAds?.isTermsAndConditionAccepted == true
            if (addDetails?.userAds?.adEmail?.isNotEmpty() == true) {
                binding?.emailRadioBtn?.isChecked = true
                binding?.emailAddressBasicDetails?.setText(addDetails.userAds.adEmail)
            } else {
                binding?.phoneRadioBtn?.isChecked = true
                binding?.phoneNumberAddressDetails?.setText(addDetails?.userAds?.adPhone)
            }

            /** Finding deleted images **/
            var data: UserAdsImage? = null
            addDetails?.userAds?.userAdsImages?.forEachIndexed { index, userAdsImage ->
                val innerIndex =
                    postClassifiedViewModel.listOfImagesUri.indexOfFirst { it.toString() == "${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}" }
                if (innerIndex == -1) {
                    data = addDetails.userAds.userAdsImages[index]
                    if (!deletedItemList.contains(data)) {
                        deletedItemList.add(data!!)
                    }
                }
            }

            var i = 0
            var len = addDetails?.userAds?.userAdsImages?.size
            deletedItemList.forEach {
                i++
                imageIdToBeDeleted += "${it.imageId}${if (i != len) "," else ""}"
            }

            postClassifiedViewModel.listOfImagesUri.forEach { imageUri ->
                if (!imageUri.toString().startsWith("htt")) {
                    if (!imagesUriWhileUpdating.contains(imageUri)) {
                        imagesUriWhileUpdating.add(imageUri)
                    }
                }
            }


        }


        postClassifiedViewModel.uploadClassifiedPics.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    val action =
                        AddressDetailsClassifiedFragmentDirections.actionAddressDetailsClassifiedFragmentToClassifiedPostSuccessBottom()
                    findNavController().navigate(action)
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        postClassifiedViewModel.deleteClassifiedPics.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    val action =
                        AddressDetailsClassifiedFragmentDirections.actionAddressDetailsClassifiedFragmentToClassifiedPostSuccessBottom()
                    findNavController().navigate(action)
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding?.root
    }

    private fun callUploadClassifiedPicApi(id: Int?, isUpdateClassified: Boolean) {

        val listOfImages: MutableList<MultipartBody.Part> = mutableListOf()

        if (isUpdateClassified) {
            imagesUriWhileUpdating.forEach {
                if (!it.toString().startsWith("htt")) {
                    val file = File(it.toString().replace("file:", ""))

                    val requestFile: RequestBody =
                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val requestImage =
                        MultipartBody.Part.createFormData("files", file.name, requestFile)
                    if (!listOfImages.contains(requestImage)) {
                        listOfImages.add(requestImage)
                    }
                }
            }
        } else {
            postClassifiedViewModel.listOfImagesUri.forEach {
                if (!it.toString().startsWith("htt")) {
                    val file = File(it.toString().replace("file:", ""))

                    val requestFile: RequestBody =
                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val requestImage =
                        MultipartBody.Part.createFormData("files", file.name, requestFile)
                    if (!listOfImages.contains(requestImage)) {
                        listOfImages.add(requestImage)
                    }
                }
            }
        }

        val addId = id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val delId =
            if (imageIdToBeDeleted.isNotEmpty()) imageIdToBeDeleted.toRequestBody("multipart/form-data".toMediaTypeOrNull()) else "".toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        if (listOfImages.size > 0) {
            postClassifiedViewModel.uploadClassifiedPics(listOfImages, addId, delId)
        } else {
            val attachmentEmpty = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "");
            val fileToUpload = MultipartBody.Part.createFormData("files", "", attachmentEmpty)
            postClassifiedViewModel.deleteClassifiedPics(fileToUpload, addId, delId)
        }
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
                adDescription = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_DESCRIPTION]!!,
                adEmail = adEmail,
                adExpireDT = "",
                adImages = null,
                adKeywords = adKeywords,
                adLocation = cityName,
                adPhone = adPhone,
                adThumbnails = null,
                adTitle = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_TITLE]!!,
                adZip = zipCode,
                approved = false,
                askingPrice = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_ASKING_PRICE]!!.toDouble(),
                category = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_CATEGORY]!!,
                contactType = if (adPhone.isNotEmpty()) "Phone" else "Email",
                createdOn = "",
                delImages = null,
                deletedOn = null,
                favorite = false,
                id = null,
                isNew = postClassifiedViewModel.isProductNewCheckBox,
                isTermsAndConditionAccepted = true,
                lastUpdated = "",
                popularOnAaonri = false,
                rejected = false,
                rejectionReson = null,
                subCategory = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_SUB_CATEGORY]!!,
                totalFavourite = 0,
                userAdsImages = listOf(),
                userId = email.toString()
            )
        )
    }

    private fun updateClassified(
        adEmail: String,
        adPhone: String,
        adKeywords: String,
        cityName: String,
        zipCode: String
    ) {
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        postClassifiedViewModel.updateClassified(
            PostClassifiedRequest(
                active = true,
                adDescription = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_DESCRIPTION]!!,
                adEmail = adEmail,
                adExpireDT = "",
                adImages = null,
                adKeywords = adKeywords,
                adLocation = cityName,
                adPhone = adPhone,
                adThumbnails = null,
                adTitle = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_TITLE]!!,
                adZip = zipCode,
                approved = false,
                askingPrice = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_ASKING_PRICE]!!.toDouble(),
                category = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_CATEGORY]!!,
                contactType = if (adPhone.isNotEmpty()) "Phone" else "Email",
                createdOn = "",
                delImages = null,
                deletedOn = null,
                favorite = false,
                id = postClassifiedViewModel.updateClassifiedId,
                isNew = postClassifiedViewModel.isProductNewCheckBox,
                isTermsAndConditionAccepted = true,
                lastUpdated = "",
                popularOnAaonri = false,
                rejected = false,
                rejectionReson = null,
                subCategory = postClassifiedViewModel.classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_SUB_CATEGORY]!!,
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}