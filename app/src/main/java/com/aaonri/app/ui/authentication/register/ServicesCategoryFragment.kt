package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.R
import com.aaonri.app.WebViewActivity
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.model.CommunityAuth
import com.aaonri.app.data.authentication.register.model.UpdateProfileRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentServicesCategoryBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.utils.*
import com.aaonri.app.utils.custom.UserProfileStaticData
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class ServicesCategoryFragment : Fragment() {
    var binding: FragmentServicesCategoryBinding? = null
    private var adapter: ServicesItemAdapter? = null
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var isServicesSelected = false
    var isJobSelected = false
    var isCompanyEmailCheckboxSelected = false
    var selectedCommunity = mutableListOf<CommunityAuth>()
    var selectedServices = mutableListOf<ServicesResponseItem>()
    var selectedServicesInterest = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dialog = Dialog(requireContext())
        binding =
            FragmentServicesCategoryBinding.inflate(inflater, container, false)

        val socialProfile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val ss =
            SpannableString(resources.getString(R.string.by_tapping_get_started_you_agree_our_privacy_policy_terms_of_use))

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
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

        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
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

        ss.setSpan(clickableSpan1, 37, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(clickableSpan2, 54, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        getServicesInterestList()

        adapter = ServicesItemAdapter({ selectedCommunity ->
            authCommonViewModel.addServicesList(selectedCommunity)
        }, {
            isJobSelected = it
        }) {

        }

        binding?.apply {

            companyEmailServices.isEnabled = false
            authCommonViewModel.addNavigationForStepper(AuthConstant.SERVICE_DETAILS_SCREEN)

            privacyPolicyRegistrationTv.text = ss
            privacyPolicyRegistrationTv.movementMethod = LinkMovementMethod.getInstance()

            isAliasNameCheckBox.setOnCheckedChangeListener { p0, p1 ->
                if (p1) {
                    aliasNameServices.isEnabled = false
                    if (authCommonViewModel.basicDetailsMap["firstName"] != null) {
                        aliasNameServices.setText(authCommonViewModel.basicDetailsMap["firstName"] + " " + authCommonViewModel.basicDetailsMap["lastName"])
                    } else {
                        aliasNameServices.setText(UserProfileStaticData.getUserProfileDataValue()?.firstName + " " + UserProfileStaticData.getUserProfileDataValue()?.lastName)
                    }
                } else {
                    aliasNameServices.setText("")
                    aliasNameServices.isEnabled = true
                }
            }

            isRecruiterCheckBox.setOnCheckedChangeListener { p0, p1 ->
                isCompanyEmailCheckboxSelected = p1
                companyEmailServices.isEnabled = p1
            }


            companyEmailServices.addTextChangedListener { editable ->
                editable?.let {
                    if (editable.toString().isNotEmpty() && editable.toString().length > 8) {
                        if (Validator.emailValidation(editable.toString())) {
                            invalidEmailTv.visibility = View.GONE
                        } else {
                            invalidEmailTv.visibility = View.VISIBLE
                        }
                    } else {
                        invalidEmailTv.visibility = View.GONE
                    }

                }
            }

            serviceSubmitBtn.setOnClickListener {
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                if (isServicesSelected) {
                    val companyEmail = companyEmailServices.text
                    val aliasName = aliasNameServices.text

                    if (isCompanyEmailCheckboxSelected) {
                        if (Validator.emailValidation(companyEmail.toString()) && companyEmail.toString().length >= 8) {
                            if (aliasName.toString().isNotEmpty()) {
                                selectedCommunity.forEach {
                                    Log.i("selectedCommunity", "${it.communityName}")
                                }
                                if (authCommonViewModel.isUpdateProfile) {
                                    updateProfile()
                                } else {
                                    registerUser(
                                        companyEmail.toString(),
                                        aliasName.toString(),
                                        isRecruiterCheckBox = isRecruiterCheckBox.isChecked,
                                        isAliasNameCheckBox = isAliasNameCheckBox.isChecked,
                                        //belongToCricketCheckBox = belongToCricketCheckBox.isChecked
                                    )
                                }
                            } else {
                                activity?.let { it1 ->
                                    Snackbar.make(
                                        it1.findViewById(android.R.id.content),
                                        "Alias name required", Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            if (companyEmail.isNotEmpty()) {
                                invalidEmailTv.visibility = View.VISIBLE
                            } else {
                                activity?.let { it1 ->
                                    Snackbar.make(
                                        it1.findViewById(android.R.id.content),
                                        "Company email required", Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else if (aliasName.toString().isNotEmpty()) {
                        if (authCommonViewModel.isUpdateProfile) {
                            updateProfile()
                        } else {
                            registerUser(
                                companyEmail.toString(), aliasName.toString(),
                                isRecruiterCheckBox = isRecruiterCheckBox.isChecked,
                                isAliasNameCheckBox = isAliasNameCheckBox.isChecked,
                                //belongToCricketCheckBox = belongToCricketCheckBox.isChecked
                            )
                        }
                    } else {
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "Alias name required", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please choose at least three services", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            deleteProfileBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure you want to Delete your account? This action would be irreversible and you will not be able to use any services of aaonri.")
                builder.setPositiveButton("Delete") { dialog, which ->

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_EMAIL, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_ZIP_CODE, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_CITY, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_STATE, "")

                    context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                        ?.set(Constant.IS_USER_LOGIN, false)

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_PROFILE_PIC, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.GMAIL_FIRST_NAME, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.GMAIL_LAST_NAME, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_INTERESTED_SERVICES, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_NAME, "")

                    context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                        ?.set(Constant.IS_JOB_RECRUITER, false)

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_PHONE_NUMBER, "")

                    context?.let { it1 -> PreferenceManager<Int>(it1) }
                        ?.set(Constant.USER_ID, 0)

                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.gmail_client_id))
                        .requestEmail()
                        .build()

                    FirebaseAuth.getInstance().signOut()
                    LoginManager.getInstance().logOut()
                    mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
                    mGoogleSignInClient.signOut()

                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->

                }
                builder.show()
            }

            servicesGridRecyclerView.adapter = adapter
            servicesGridRecyclerView.layoutManager = GridLayoutManager(context, 3)
        }

        authCommonViewModel.selectedServicesList.observe(viewLifecycleOwner) { serviceResponseItem ->
            // adapter?.selectedCategoriesList = serviceResponseItem
            selectedServicesInterest = ""
            var i = 0
            var len: Int = serviceResponseItem.size
            serviceResponseItem.forEach {
                i++
                selectedServicesInterest += "${it.id}${if (i != len) "," else ""}"
            }
            if (serviceResponseItem.size >= 3 && isJobSelected) {
                authCommonViewModel.addStepViewLastTick(true)
                isServicesSelected = true
                binding?.servicesGridRecyclerView?.margin(bottom = 0f)
                binding?.aliasNameCardView?.visibility = View.VISIBLE
                binding?.visibilityCardView?.visibility = View.VISIBLE
                binding?.serviceSubmitBtn?.setBackgroundResource(R.drawable.green_btn_shape)
            } else if (serviceResponseItem.size >= 3) {
                isServicesSelected = true
                authCommonViewModel.addStepViewLastTick(true)
                binding?.visibilityCardView?.visibility = View.GONE
                binding?.isRecruiterCheckBox?.isChecked = false
                binding?.aliasNameCardView?.visibility = View.VISIBLE
                if (!authCommonViewModel.isUpdateProfile) {
                    binding?.privacyPolicyRegistrationTv?.visibility = View.VISIBLE
                }
                binding?.servicesGridRecyclerView?.margin(bottom = 0f)
                binding?.serviceSubmitBtn?.setBackgroundResource(R.drawable.green_btn_shape)
            } else {
                isServicesSelected = false
                authCommonViewModel.addStepViewLastTick(false)
                binding?.visibilityCardView?.visibility = View.GONE
                binding?.aliasNameCardView?.visibility = View.GONE
                binding?.privacyPolicyRegistrationTv?.visibility = View.GONE
                binding?.servicesGridRecyclerView?.margin(bottom = 60f)
                binding?.serviceSubmitBtn?.setBackgroundResource(R.drawable.light_green_btn_shape)
            }
        }

        registrationViewModel.registerData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    if (response.data?.status.equals("true")) {

                        if (socialProfile?.isEmpty() == true) {
                            /**If social profile is empty that's mean user changed their profile in case of gmail login**/
                            if (authCommonViewModel.profilePicUri != null) {
                                uploadProfilePicture(
                                    response.data?.user?.userId,
                                    authCommonViewModel.profilePicUri!!,
                                    false
                                )
                            } else {
                                if (!authCommonViewModel.isUpdateProfile) {
                                    dialog.setContentView(R.layout.success_register_dialog)
                                    dialog.window?.setBackgroundDrawable(
                                        ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.dialog_shape
                                        )
                                    )
                                    dialog.setCancelable(false)
                                    dialog.show()
                                    val continueBtn =
                                        dialog.findViewById<TextView>(R.id.continueRegisterBtn)
                                    continueBtn.setOnClickListener {
                                        dialog.dismiss()
                                        activity?.finish()
                                    }
                                }
                            }
                        } else {
                            /** Download social profile and upload the file **/
                            if (downloadImage(socialProfile, "Profile")) {
                                uploadProfilePicture(
                                    response.data?.user?.userId,
                                    isSocialProfile = true
                                )
                            }
                        }
                    } else {
                        response.data?.errorDetails?.forEachIndexed { index, errorDetail ->
                            activity?.let { it1 ->
                                Snackbar.make(
                                    it1.findViewById(android.R.id.content),
                                    "${
                                        errorDetail.errorMessage.replace("<", "")
                                            .replace(">", "")
                                    }",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    registrationViewModel.registerData.postValue(null)
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        authCommonViewModel.selectedCommunityList.observe(viewLifecycleOwner) { communityList ->
            communityList.forEach { community ->
                selectedCommunity.add(
                    (CommunityAuth(
                        community.communityId,
                        community.communityName,
                        /*community.createdDt,
                        community.id*/
                    ))
                )
            }
        }

        authCommonViewModel.uploadProfilePicData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    if (!authCommonViewModel.isUpdateProfile) {
                        dialog.setContentView(R.layout.success_register_dialog)
                        dialog.window?.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.dialog_shape
                            )
                        )
                        dialog.setCancelable(false)
                        dialog.show()
                        val continueBtn =
                            dialog.findViewById<TextView>(R.id.continueRegisterBtn)
                        continueBtn.setOnClickListener {
                            dialog.dismiss()
                            activity?.finish()
                        }
                    }

                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        if (authCommonViewModel.isUpdateProfile) {
            binding?.serviceSubmitBtn?.text = "UPDATE"
            binding?.privacyPolicyRegistrationTv?.visibility = View.GONE
            UserProfileStaticData.getUserProfileDataValue()?.let {
                adapter?.setSelectedServicesList(it.interests)
                if (it.isJobRecruiter) {
                    binding?.companyEmailServices?.setText(it.companyEmail)
                    binding?.isRecruiterCheckBox?.isChecked = true
                }
                if (it.isFullNameAsAliasName) {
                    binding?.isAliasNameCheckBox?.isChecked = true
                }
                binding?.aliasNameServices?.setText(it.aliasName)
            }
            binding?.aliasNameCardView?.margin(bottom = 60F)
            binding?.deleteProfileBtn?.visibility = View.VISIBLE
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        return binding?.root
    }

    /*private fun callUploadResumeApi(userId: Int?) {

        val downloadDire =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val uploadedFile = File(downloadDire, "Profile.jpg")
        val authority = "${context?.packageName}.provider"
        val accessibleUri = context?.let { FileProvider.getUriForFile(it, authority, uploadedFile) }

        val file = createTmpFileFromUri(accessibleUri)

        val id = userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestFile: RequestBody? =
            file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage =
            requestFile?.let { MultipartBody.Part.createFormData("file", file.name, it) }

        if (requestImage != null) {
            authCommonViewModel.uploadProfilePic(requestImage, id)
        }
    }*/

    private fun downloadImage(socialProfile: String?, fileName: String): Boolean {
        try {
            val downloadManager =
                activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val imageLink = Uri.parse(socialProfile)
            val request = DownloadManager.Request(imageLink)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("images/jpeg")
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileName)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    File.separator + fileName + ".jpg"
                )
            downloadManager.enqueue(request)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun uploadProfilePicture(
        userId: Int?,
        profilePic: Uri? = null,
        isSocialProfile: Boolean
    ) {

        val file: File?

        if (isSocialProfile) {
            val downloadDire =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val uploadedFile = File(downloadDire, "Profile.jpg")
            val authority = "${context?.packageName}.provider"
            val accessibleUri =
                context?.let { FileProvider.getUriForFile(it, authority, uploadedFile) }
            file = createTmpFileFromUri(accessibleUri)
        } else {
            file = File(profilePic.toString().replace("file:", ""))
        }

        val id = userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestFile: RequestBody? =
            file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage =
            requestFile?.let { MultipartBody.Part.createFormData("file", file.name, it) }

        if (requestImage != null) {
            authCommonViewModel.uploadProfilePic(requestImage, id)
        }
    }

    private fun createTmpFileFromUri(uri: Uri?): File? {
        return try {
            val stream = uri?.let { context?.contentResolver?.openInputStream(it) }
            val file = File.createTempFile("Profile.jpg", "", context?.cacheDir)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateProfile() {
        UserProfileStaticData.getUserProfileDataValue()?.let {
            registrationViewModel.updateProfile(
                UpdateProfileRequest(
                    activeUser = true,
                    address1 = it.address1,
                    address2 = it.address2,
                    aliasName = binding?.aliasNameServices?.text?.toString(),
                    authorized = true,
                    city = it.city,
                    community = it.community,
                    companyEmail = if (binding?.isRecruiterCheckBox?.isChecked == true) binding?.companyEmailServices?.text?.toString() else "",
                    emailId = it.emailId,
                    firstName = it.firstName,
                    interests = selectedServicesInterest,
                    isAdmin = 0,
                    isFullNameAsAliasName = it.isFullNameAsAliasName,
                    isJobRecruiter = binding?.isRecruiterCheckBox?.isChecked,
                    isPrimeUser = false,
                    isSurveyCompleted = false,
                    lastName = it.lastName,
                    newsletter = false,
                    originCity = it.originCity,
                    originCountry = it.originCountry,
                    originState = it.originState,
                    password = it.password,
                    phoneNo = it.phoneNo,
                    regdEmailSent = false,
                    registeredBy = "manual",
                    userName = it.userName,
                    zipcode = it.zipcode,
                    state = it.state,
                    userType = it.userType,
                )
            )
        }
    }

    private fun registerUser(
        companyEmail: String,
        aliasName: String,
        isRecruiterCheckBox: Boolean,
        isAliasNameCheckBox: Boolean,
    ) {
        val socialProfile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }
        authCommonViewModel.addCompanyEmailAliasName(
            companyEmail,
            aliasName
        )
        authCommonViewModel.addCompanyEmailAliasCheckBoxValue(
            isRecruiterCheckBox,
            isAliasNameCheckBox,
        )
        registrationViewModel.registerUser(
            RegisterRequest(
                activeUser = true,
                address1 = authCommonViewModel.addressDetails["address1"]!!,
                address2 = authCommonViewModel.addressDetails["address2"]!!,
                aliasName = if (authCommonViewModel.companyEmailAliasName?.value?.second?.isNotEmpty() == true) authCommonViewModel.companyEmailAliasName!!.value!!.second else "",
                authorized = true,
                city = authCommonViewModel.locationDetails["city"]!!,
                community = selectedCommunity,
                companyEmail = if (authCommonViewModel.companyEmailAliasName?.value?.first?.isNotEmpty() == true) authCommonViewModel.companyEmailAliasName!!.value!!.first else "",
                emailId = authCommonViewModel.basicDetailsMap["emailAddress"]!!,
                firstName = authCommonViewModel.basicDetailsMap["firstName"]!!,
                interests = selectedServicesInterest,
                isAdmin = 0,
                isFullNameAsAliasName = authCommonViewModel.companyEmailAliasCheckBoxValue["isAliasNameCheckBox"]!!,
                isJobRecruiter = authCommonViewModel.companyEmailAliasCheckBoxValue["isRecruiterCheckBox"]!!,
                isPrimeUser = false,
                isSurveyCompleted = false,
                lastName = authCommonViewModel.basicDetailsMap["lastName"]!!,
                newsletter = false,
                originCity = if (authCommonViewModel.originLocationDetails["originCity"]?.isNotEmpty() == true) authCommonViewModel.originLocationDetails["originCity"]!! else "",
                originCountry = authCommonViewModel.selectedCountryLocationScreen!!.value!!.first,
                originState = if (authCommonViewModel.originLocationDetails["originState"]?.isNotEmpty() == true) authCommonViewModel.originLocationDetails["originState"]!! else "",
                password = authCommonViewModel.basicDetailsMap["password"]!!,
                phoneNo = authCommonViewModel.addressDetails["phoneNumber"]!!,
                picture = if (socialProfile?.isNotEmpty() == true) socialProfile else "",
                regdEmailSent = false,
                registeredBy = "manual",
                userName = aliasName,
                zipcode = authCommonViewModel.locationDetails["zipCode"]!!,
                state = if (authCommonViewModel.locationDetails["state"]?.isNotEmpty() == true) authCommonViewModel.locationDetails["state"]!! else "",
                country = authCommonViewModel.selectedCountryAddressScreen!!.value!!.first,
            )
        )
    }

    private fun getServicesInterestList() {
        registrationViewModel.service.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { servicesResponse ->
                        adapter?.setData(servicesResponse, false)
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun View.margin(
        left: Float? = null,
        top: Float? = null,
        right: Float? = null,
        bottom: Float? = null
    ) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}