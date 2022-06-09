package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.forgot_password.viewmodel.ForgotPasswordViewModel
import com.aaonri.app.databinding.FragmentResetMyPasswordBinding
import com.aaonri.app.utils.Validator
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetMyPasswordFragment : Fragment() {
    val forgotPassViewModel: ForgotPasswordViewModel by viewModels()
    var resetPasswordBinding: FragmentResetMyPasswordBinding? = null
    var isEmailValid = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = FragmentResetMyPasswordBinding.inflate(inflater, container, false)

        resetPasswordBinding?.apply {

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            backToSignInTv.setOnClickListener {
                findNavController().navigateUp()
            }

            emailForgotPasswordEt.addTextChangedListener { editable ->
                if (editable.toString().trim().isNotEmpty() && editable.toString()
                        .trim().length >= 8
                ) {
                    if (Validator.emailValidation(editable.toString())) {
                        emailValidationTv.visibility = View.GONE
                        isEmailValid = true
                    } else {
                        resetPasswordBinding?.emailValidationTv?.text = "Please enter valid email"
                        isEmailValid = false
                        emailValidationTv.visibility = View.VISIBLE
                    }
                } else {
                    isEmailValid = false
                    emailValidationTv.visibility = View.GONE
                }
            }

            resetPasswordBtn.setOnClickListener {

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                if (emailForgotPasswordEt.text.toString().isNotEmpty() && isEmailValid
                ) {
                    forgotPassViewModel.sendForgotPasswordLink(emailForgotPasswordEt.text.toString())
                }else{
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please enter your registered mail address", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        forgotPassViewModel.forgotPasswordData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    resetPasswordBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.status?.equals("false") == true) {
                        isEmailValid = false
                        resetPasswordBinding?.emailValidationTv?.visibility = View.VISIBLE
                        resetPasswordBinding?.emailValidationTv?.text =
                            response.data.message.toString()
                        resetPasswordBinding?.emailValidationTv?.visibility = View.VISIBLE
                    } else {
                        isEmailValid = true
                        resetPasswordBinding?.emailValidationTv?.visibility = View.GONE
                        findNavController().navigate(R.id.action_resetPassword_to_checkYourEmailFragment)
                        resetPasswordBinding?.emailValidationTv?.visibility = View.GONE
                        forgotPassViewModel.forgotPasswordData.value = null
                    }
                    resetPasswordBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    resetPasswordBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {}
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        return resetPasswordBinding?.root
    }
}