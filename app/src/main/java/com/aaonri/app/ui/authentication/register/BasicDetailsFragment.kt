package com.aaonri.app.ui.authentication.register

import android.app.Activity
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentBasicDetailsBinding
import com.aaonri.app.utils.*
import com.bumptech.glide.Glide
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
    var basicDetailsBinding: FragmentBasicDetailsBinding? = null
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    var isEmailValid = false
    var isPasswordValid = false
    var profile = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        basicDetailsBinding = FragmentBasicDetailsBinding.inflate(inflater, container, false)
        var job: Job? = null

        val socialProfile =
            context?.let { PreferenceManager<String>(it)[Constant.PROFILE_USER, ""] }

        val blockCharacterSet = "1234567890~#^|$%&*!@\""

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            if (source != null && blockCharacterSet.contains("" + source)) {
                ""
            } else null
        }

        basicDetailsBinding?.apply {

            authCommonViewModel.addNavigationForStepper(AuthConstant.BASIC_DETAILS_SCREEN)

            if (authCommonViewModel.isNewUserRegisterUsingGmail) {
                firstNameBasicDetails.setText(context?.let { PreferenceManager<String>(it)[Constant.GMAIL_FIRST_NAME, ""] })
                lastNameBasicDetails.setText(context?.let { PreferenceManager<String>(it)[Constant.GMAIL_LAST_NAME, ""] })
                emailAddressBasicDetails.setText(context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] })
                context?.let { Glide.with(it).load(socialProfile).into(addProfileIv) }
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
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    editable?.let {
                        if (editable.toString().isNotEmpty() && editable.toString().length > 8) {
                            if (Validator.emailValidation(editable.toString())) {
                                isEmailValid = true
                                basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.GONE
                                registrationViewModel.isEmailAlreadyRegister(
                                    EmailVerifyRequest(
                                        emailAddressBasicDetails.text.toString()
                                    )
                                )
                            } else {
                                isEmailValid = false
                                basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.VISIBLE
                                basicDetailsBinding?.emailAlreadyExistTv?.text =
                                    "Please enter valid email"
                            }
                        } else {
                            isEmailValid = false
                            basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.GONE
                        }
                    }
                }
            }


            passwordBasicDetails.addTextChangedListener { editable ->
                editable?.let {
                    if (it.toString().isNotEmpty() && it.toString().length >= 8) {
                        if (Validator.passwordValidation(it.toString())) {
                            isPasswordValid = true
                            basicDetailsBinding?.passwordValidationTv?.visibility = View.GONE
                        } else {
                            isPasswordValid = false
                            basicDetailsBinding?.passwordValidationTv?.text =
                                "Please enter valid password"
                            basicDetailsBinding?.passwordValidationTv?.visibility = View.VISIBLE
                        }
                    } else {
                        isPasswordValid = false
                        basicDetailsBinding?.passwordValidationTv?.visibility = View.GONE
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
//                        authCommonViewModel.addCountryClicked(true)
                        findNavController().navigate(R.id.action_basicDetailsFragment_to_addressDetailsFragment)
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
                    basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.GONE
                }
                is Resource.Success -> {
                    if (response.data?.status == "true") {
                        isEmailValid = false
                        basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.VISIBLE
                        basicDetailsBinding?.emailAlreadyExistTv?.text =
                            "This email is already registered"
                    } else {
                        isEmailValid = true
                    }
                }
                is Resource.Error -> {
                    basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })

        return basicDetailsBinding?.root
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {

                val fileUri = data?.data!!

                profile = fileUri.toString()
                basicDetailsBinding?.progressBarBasicDetails?.visibility = View.INVISIBLE
                setImage()
                //basicDetailsBinding?.addProfileIv?.setImageURI(fileUri)
                basicDetailsBinding?.addProfileBtn?.visibility = View.GONE

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                basicDetailsBinding?.progressBarBasicDetails?.visibility = View.INVISIBLE
            }
        }

    private fun setImage() {
        if (profile.isNotEmpty()) {
            basicDetailsBinding?.addProfileIv?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load(profile)
                        .circleCrop()
                        .into(it)
                }
            }
        } else {
            basicDetailsBinding?.addProfileIv?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load(R.drawable.profile_pic_placeholder)
                        .circleCrop()
                        .into(it)
                }
            }

        }
    }

}