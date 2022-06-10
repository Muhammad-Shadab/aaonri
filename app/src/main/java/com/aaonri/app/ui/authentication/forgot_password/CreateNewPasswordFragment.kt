package com.aaonri.app.ui.authentication.forgot_password

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.MainActivity
import com.aaonri.app.R
import com.aaonri.app.data.authentication.forgot_password.model.NewPasswordRequest
import com.aaonri.app.data.authentication.forgot_password.viewmodel.ForgotPasswordViewModel
import com.aaonri.app.databinding.FragmentCreateNewPasswordBinding
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.aaonri.app.utils.Validator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNewPasswordFragment : Fragment() {
    var createNewPasswordBinding: FragmentCreateNewPasswordBinding? = null
    val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    val args: CreateNewPasswordFragmentArgs by navArgs()
    var isPasswordValid = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createNewPasswordBinding =
            FragmentCreateNewPasswordBinding.inflate(inflater, container, false)

        createNewPasswordBinding?.apply {

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            createNewPasswordBtn.setOnClickListener {

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                if (newPasswordEt1.text.toString().trim() == newPasswordEt2.text.toString()
                        .trim() && isPasswordValid && newPasswordEt1.text.toString()
                        .trim().length > 8
                ) {
                    forgotPasswordViewModel.newPasswordRequest(
                        NewPasswordRequest(args.email, newPasswordEt2.text.toString().trim())
                    )
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please enter valid password", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        createNewPasswordBinding?.newPasswordEt1?.addTextChangedListener { editable ->
            editable?.let {
                if (it.toString().isNotEmpty() && it.toString().length >= 8) {
                    if (Validator.passwordValidation(it.toString())) {
                        isPasswordValid = true
                        createNewPasswordBinding?.passwordValidationTv1?.visibility = View.GONE
                    } else {
                        isPasswordValid = false
                        createNewPasswordBinding?.passwordValidationTv1?.text =
                            "Please enter valid password"
                        createNewPasswordBinding?.passwordValidationTv1?.visibility = View.VISIBLE
                    }
                } else {
                    isPasswordValid = false
                    createNewPasswordBinding?.passwordValidationTv1?.visibility = View.GONE
                }
            }
        }

        createNewPasswordBinding?.newPasswordEt2?.addTextChangedListener { editable ->

            editable?.let {
                if (it.isNotEmpty() && createNewPasswordBinding?.newPasswordEt1?.text.toString().length <= it.length) {
                    if (it.toString() == createNewPasswordBinding?.newPasswordEt1?.text.toString()) {
                        isPasswordValid = true
                        createNewPasswordBinding?.passwordValidationTv2?.visibility = View.GONE
                    } else {
                        isPasswordValid = false
                        createNewPasswordBinding?.passwordValidationTv2?.text =
                            "Password did not matched"
                        createNewPasswordBinding?.passwordValidationTv2?.visibility = View.VISIBLE
                    }
                } else {
                    isPasswordValid = false
                    createNewPasswordBinding?.passwordValidationTv2?.visibility = View.GONE
                }
            }
        }

        forgotPasswordViewModel.newPasswordData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    createNewPasswordBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    createNewPasswordBinding?.progressBar?.visibility = View.GONE
                    findNavController().navigate(R.id.action_createNewPasswordFragment_to_passwordResetSuccessBottom)
                }
                is Resource.Error -> {
                    createNewPasswordBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
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
                    findNavController().navigateUp()
                }
            })

        return createNewPasswordBinding?.root

    }
}