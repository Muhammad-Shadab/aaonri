package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentResetMyPasswordBinding
import com.aaonri.app.utils.Validator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetMyPasswordFragment : Fragment() {
    /*val forgotPassViewModel: ForgotPasswordViewModel by viewModels()*/
    var resetPasswordBinding: FragmentResetMyPasswordBinding? = null

    var isEmailValid = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = FragmentResetMyPasswordBinding.inflate(inflater, container, false)

        resetPasswordBinding?.apply {

            emailForgotPasswordEt.addTextChangedListener { editable ->
                if (editable.toString().trim().isNotEmpty() && editable.toString()
                        .trim().length >= 8
                ) {
                    if (Validator.emailValidation(editable.toString())) {
                        emailValidationTv.visibility = View.GONE
                        isEmailValid = true
                    } else {
                        isEmailValid = false
                        emailValidationTv.visibility = View.VISIBLE
                    }
                } else {
                    isEmailValid = false
                    emailValidationTv.visibility = View.GONE
                }
            }

            resetPasswordBtn.setOnClickListener {
                if (emailForgotPasswordEt.text.toString().isNotEmpty() && isEmailValid) {
                    /*forgotPassViewModel.sendForGotPasswordLink(
                        emailForgotPasswordEt.text.toString()
                    )*/
                    findNavController().navigate(R.id.action_resetPassword_to_checkYourEmailFragment)
                }
            }

        }

        return resetPasswordBinding?.root

    }
}