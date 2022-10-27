package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
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
    var binding: FragmentResetPasswordInfoBinding? = null
    private val args: ResetPasswordInfoFragmentArgs by navArgs()
    val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordInfoBinding.inflate(inflater, container, false)

        forgotPasswordViewModel.verifyPassword(args.code, args.email, args.id)

        binding?.apply {
            resetPasswordBtn.setOnClickListener {
                val action =
                    ResetPasswordInfoFragmentDirections.actionResetPasswordInfoFragmentToCreateNewPasswordFragment(args.email)
                findNavController().navigate(action)
            }
        }

        forgotPasswordViewModel.verifyPassword.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Verified", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_resetPasswordInfoFragment_to_loginFragment)
                }
            })

        return binding?.root
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}