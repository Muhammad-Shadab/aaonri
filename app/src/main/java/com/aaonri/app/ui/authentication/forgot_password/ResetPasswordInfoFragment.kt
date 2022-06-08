package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.data.authentication.forgot_password.viewmodel.ForgotPasswordViewModel
import com.aaonri.app.databinding.FragmentResetPasswordInfoBinding
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordInfoFragment : Fragment() {
    private val args: ResetPasswordInfoFragmentArgs by navArgs()
    val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    var resetPasswordBinding: FragmentResetPasswordInfoBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = FragmentResetPasswordInfoBinding.inflate(inflater, container, false)

        forgotPasswordViewModel.verifyPassword(args.code, args.email, args.id)

        resetPasswordBinding?.apply {
            resetPasswordBtn.setOnClickListener {
                val action =
                    ResetPasswordInfoFragmentDirections.actionResetPasswordInfoFragmentToCreateNewPasswordFragment(args.email)
                findNavController().navigate(action)
            }
        }

        forgotPasswordViewModel.verifyPassword.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    resetPasswordBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    resetPasswordBinding?.progressBar?.visibility = View.GONE
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Successfully verified", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Empty -> {
                    resetPasswordBinding?.progressBar?.visibility = View.GONE
                }
            }
        }

        return resetPasswordBinding?.root
    }
}