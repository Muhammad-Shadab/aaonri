package com.aaonri.app.ui.authentication.register

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.model.UpdateProfileRequest
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentBasicDetailsBinding
import com.aaonri.app.utils.*
import com.aaonri.app.utils.custom.UserProfileStaticData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BasicDetailsFragment : Fragment() {
    var binding: FragmentBasicDetailsBinding? = null
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    var isEmailValid = false
    var isPasswordValid = false
    var profile = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasicDetailsBinding.inflate(inflater, container, false)
        var job: Job? = null

        val socialProfile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val blockCharacterSet = "1234567890~#^|$%&*!@\""

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            if (source != null && blockCharacterSet.contains("" + source)) {
                ""
            } else null
        }

        binding?.apply {

            authCommonViewModel.addNavigationForStepper(AuthConstant.BASIC_DETAILS_SCREEN)

            if (authCommonViewModel.isNewUserRegisterUsingGmail) {
                firstNameBasicDetails.setText(context?.let { PreferenceManager<String>(it)[Constant.GMAIL_FIRST_NAME, ""] })
                lastNameBasicDetails.setText(context?.let { PreferenceManager<String>(it)[Constant.GMAIL_LAST_NAME, ""] })
                emailAddressBasicDetails.setText(context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] })
                binding?.passwordBasicDetails?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                binding?.emailAddressBasicDetails?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                context?.let { Glide.with(it).load(socialProfile).circleCrop().into(addProfileIv) }
                if (socialProfile != null) {
                    profile = socialProfile
                }
                passwordBasicDetails.setText("********")
                emailAddressBasicDetails.isEnabled = false
                passwordBasicDetails.isEnabled = false
                passTi.isEnabled = false
            }

            firstNameBasicDetails.filters = arrayOf(filter)
            lastNameBasicDetails.filters = arrayOf(filter)


            profilePicPlaceholder.setOnClickListener {
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                if (profile.isEmpty()) {
                    ImagePicker.with(requireActivity())
                        .compress(1024)
                        .crop()
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                            progressBarBasicDetails.visibility = View.VISIBLE
                        }
                } else {
                    val materialAlertDialogBuilder =
                        context?.let { it1 -> MaterialAlertDialogBuilder(it1) }
                    materialAlertDialogBuilder?.setTitle("Profile Photo")
                        ?.setMessage("Change profile photo? ")
                        ?.setPositiveButton("CHANGE") { dialog, _ ->
                            ImagePicker.with(requireActivity())
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .crop()
                                .createIntent { intent ->
                                    progressBarBasicDetails.visibility = View.VISIBLE
                                    startForProfileImageResult.launch(intent)
                                }
                            dialog.dismiss()
                        }
                        ?.setNegativeButton("REMOVE") { dialog, _ ->
                            profile = ""
                            setImage()
                            addProfileBtn.visibility = View.VISIBLE
                            dialog.dismiss()
                        }
                        ?.show()
                }
            }

            emailAddressBasicDetails.addTextChangedListener { editable ->
                if (authCommonViewModel.isUpdateProfile) {
                    isEmailValid = true
                } else {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(500L)
                        editable?.let {
                            if (editable.toString()
                                    .isNotEmpty() && editable.toString().length > 8
                            ) {
                                if (Validator.emailValidation(editable.toString().trim())) {
                                    isEmailValid = true
                                    binding?.emailAlreadyExistTv?.visibility = View.GONE
                                    registrationViewModel.isEmailAlreadyRegister(
                                        EmailVerifyRequest(
                                            emailAddressBasicDetails.text.toString()
                                        )
                                    )
                                } else {
                                    isEmailValid = false
                                    binding?.emailAlreadyExistTv?.visibility = View.VISIBLE
                                    binding?.emailAlreadyExistTv?.text =
                                        "Please enter valid email"
                                }
                            } else {
                                isEmailValid = false
                                binding?.emailAlreadyExistTv?.visibility = View.GONE
                            }
                        }
                    }
                }
            }


            passwordBasicDetails.addTextChangedListener { editable ->
                editable?.let {
                    if (authCommonViewModel.isUpdateProfile) {
                        isPasswordValid = true
                    } else if (authCommonViewModel.isNewUserRegisterUsingGmail) {
                        isPasswordValid = true
                    } else {
                        if (it.toString().isNotEmpty() && it.toString().length >= 8) {
                            if (Validator.passwordValidation(it.toString())) {
                                isPasswordValid = true
                                binding?.passwordValidationTv?.visibility = View.GONE
                            } else {
                                isPasswordValid = false
                                binding?.passwordValidationTv?.text =
                                    "Please enter valid password"
                                binding?.passwordValidationTv?.visibility = View.VISIBLE
                            }
                        } else {
                            isPasswordValid = false
                            binding?.passwordValidationTv?.visibility = View.GONE
                        }
                    }
                }
            }


            basicDetailsNextBtn.setOnClickListener {

                val firstName = firstNameBasicDetails.text
                val lastName = lastNameBasicDetails.text
                val emailAddress = emailAddressBasicDetails.text
                val password = passwordBasicDetails.text

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                if (firstName.toString().length >= 3 && lastName.toString().length >= 3) {
                    if (authCommonViewModel.isNewUserRegisterUsingGmail) {
                        findNavController().navigate(R.id.action_basicDetailsFragment_to_addressDetailsFragment)
                    } else if (isEmailValid && isPasswordValid) {
                        authCommonViewModel.addBasicDetails(
                            firstName.toString(),
                            lastName.toString(),
                            emailAddress.toString(),
                            password.toString()
                        )
                        if (authCommonViewModel.isUpdateProfile) {
                            if (!profile.startsWith("htt") && profile.isNotEmpty()) {
                                authCommonViewModel.setProfilePicUriValue(profile.toUri())
                            }
                            UserProfileStaticData.getUserProfileDataValue()?.let {
                                registrationViewModel.updateProfile(
                                    UpdateProfileRequest(
                                        activeUser = true,
                                        address1 = it.address1,
                                        address2 = it.address2,
                                        aliasName = it.aliasName,
                                        authorized = true,
                                        city = it.city,
                                        community = it.community,
                                        companyEmail = it.companyEmail,
                                        emailId = it.emailId,
                                        firstName = firstNameBasicDetails.text.toString(),
                                        interests = it.interests,
                                        isAdmin = 0,
                                        isFullNameAsAliasName = it.isFullNameAsAliasName,
                                        isJobRecruiter = it.isJobRecruiter,
                                        isPrimeUser = false,
                                        isSurveyCompleted = false,
                                        lastName = lastNameBasicDetails.text.toString(),
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
                        } else {
                            findNavController().navigate(R.id.action_basicDetailsFragment_to_addressDetailsFragment)
                        }
                    } else {
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "Please complete all details", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please enter valid name", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }


        registrationViewModel.emailAlreadyRegisterData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.emailAlreadyExistTv?.visibility = View.GONE
                }
                is Resource.Success -> {
                    if (response.data?.status == "true") {
                        isEmailValid = false
                        binding?.emailAlreadyExistTv?.visibility = View.VISIBLE
                        binding?.emailAlreadyExistTv?.text =
                            "This email is already registered"
                    } else {
                        isEmailValid = true
                    }
                }
                is Resource.Error -> {
                    binding?.emailAlreadyExistTv?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        if (authCommonViewModel.isUpdateProfile) {
            UserProfileStaticData.getUserProfileDataValue()?.let {
                binding?.textHintTv?.visibility = View.GONE
                profile =
                    "${BuildConfig.BASE_URL}/api/v1/common/profileFile/${it.profilePic}"
                binding?.addProfileIv?.let { it1 ->
                    context?.let { it2 ->
                        Glide.with(it2)
                            .load(profile)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .circleCrop()
                            .into(
                                it1
                            )
                    }
                }
                binding?.addProfileBtn?.visibility = View.GONE
                binding?.updateProfileBtn?.visibility = View.VISIBLE

                binding?.firstNameBasicDetails?.setText(it.firstName)
                binding?.lastNameBasicDetails?.setText(it.lastName)
                binding?.emailAddressBasicDetails?.setText(it.emailId)
                binding?.emailAddressBasicDetails?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                binding?.passwordBasicDetails?.setText(it.password)
                binding?.passwordBasicDetails?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                isEmailValid = true
                isPasswordValid = true
                binding?.emailAddressBasicDetails?.isEnabled = false
                binding?.passwordBasicDetails?.isEnabled = false
                binding?.passTi?.isEnabled = false
                binding?.basicDetailsNextBtn?.text = "UPDATE"
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (authCommonViewModel.isUpdateProfile) {
                        findNavController().navigateUp()
                    } else {
                        activity?.finish()
                    }
                }
            })

        return binding?.root
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {

                val fileUri = data?.data!!

                profile = fileUri.toString()
                binding?.progressBarBasicDetails?.visibility = View.INVISIBLE
                setImage()
                authCommonViewModel.setProfilePicUriValue(fileUri)
                //basicDetailsBinding?.addProfileIv?.setImageURI(fileUri)
                binding?.addProfileBtn?.visibility = View.GONE

                /** Changing user profile pic to blank which means user changed their gmail profile to new profile in case of gmail login**/
                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_PROFILE_PIC, "")

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                binding?.progressBarBasicDetails?.visibility = View.INVISIBLE
            }
        }

    private fun setImage() {
        if (profile.isNotEmpty()) {
            binding?.addProfileIv?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load(profile)
                        .circleCrop()
                        .into(it)
                }
            }
        } else {
            binding?.addProfileIv?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load(R.drawable.profile_pic_placeholder)
                        .circleCrop()
                        .into(it)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}