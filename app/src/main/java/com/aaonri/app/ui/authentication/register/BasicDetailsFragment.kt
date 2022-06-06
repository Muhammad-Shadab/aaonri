package com.aaonri.app.ui.authentication.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentBasicDetailsBinding
import com.aaonri.app.utils.Validator
import com.example.newsapp.utils.Resource
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
    val registrationViewModel: RegistrationViewModel by viewModels()
    var isEmailValid = false
    var isPasswordValid = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        basicDetailsBinding = FragmentBasicDetailsBinding.inflate(inflater, container, false)

        var job: Job? = null

        basicDetailsBinding?.apply {

            authCommonViewModel.addNavigationForStepper(AuthConstant.BASIC_DETAILS_SCREEN)

            emailAddressBasicDetails.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    editable?.let {
                        if (editable.toString().isNotEmpty() && editable.toString().length > 8) {
                            if (Validator.emailValidation(editable.toString())) {
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
                    if (it.toString().isNotEmpty() && it.toString().length >= 6) {
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

                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)

                if (firstName?.isNotEmpty() == true && lastName?.isNotEmpty() == true && isEmailValid && isPasswordValid) {
                    authCommonViewModel.addBasicDetails(
                        firstName.toString(),
                        lastName.toString(),
                        emailAddress.toString(),
                        password.toString()
                    )
                    findNavController().navigate(R.id.action_basicDetailsFragment_to_addressDetailsFragment)
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please complete all details", Snackbar.LENGTH_LONG
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


        return basicDetailsBinding?.root
    }

}