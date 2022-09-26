package com.aaonri.app.ui.authentication.forgot_password

import android.annotation.SuppressLint
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
    var binding: FragmentCreateNewPasswordBinding? = null
    val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    val args: CreateNewPasswordFragmentArgs by navArgs()
    var isPasswordValid = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentCreateNewPasswordBinding.inflate(inflater, container, false)

        binding?.apply {

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

        binding?.newPasswordEt1?.addTextChangedListener { editable ->
            editable?.let {
                if (it.toString().isNotEmpty() && it.toString().length >= 8) {
                    if (Validator.passwordValidation(it.toString())) {
                        isPasswordValid = true
                        binding?.passwordValidationTv1?.visibility = View.GONE
                    } else {
                        isPasswordValid = false
                        binding?.passwordValidationTv1?.text =
                            "Please enter valid password"
                        binding?.passwordValidationTv1?.visibility = View.VISIBLE
                    }
                } else {
                    isPasswordValid = false
                    binding?.passwordValidationTv1?.visibility = View.GONE
                }
            }
        }

        binding?.newPasswordEt2?.addTextChangedListener { editable ->

            editable?.let {
                if (it.isNotEmpty() && binding?.newPasswordEt1?.text.toString().length <= it.length) {
                    if (it.toString() == binding?.newPasswordEt1?.text.toString()) {
                        isPasswordValid = true
                        binding?.passwordValidationTv2?.visibility = View.GONE
                    } else {
                        isPasswordValid = false
                        binding?.passwordValidationTv2?.text =
                            "Password did not matched"
                        binding?.passwordValidationTv2?.visibility = View.VISIBLE
                    }
                } else {
                    isPasswordValid = false
                    binding?.passwordValidationTv2?.visibility = View.GONE
                }
            }
        }

        forgotPasswordViewModel.newPasswordData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    findNavController().navigate(R.id.action_createNewPasswordFragment_to_passwordResetSuccessBottom)
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
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

        return binding?.root
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}