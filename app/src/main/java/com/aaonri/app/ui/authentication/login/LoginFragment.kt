package com.aaonri.app.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.MainActivity
import com.aaonri.app.R
import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLoginBinding
import com.aaonri.app.ui.authentication.register.RegistrationActivity
import com.aaonri.app.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    val registrationViewModel: RegistrationViewModel by viewModels()
    var introBinding: FragmentLoginBinding? = null
    var isEmailValid = false
    var isPasswordValid = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        introBinding = FragmentLoginBinding.inflate(inflater, container, false)



        // this code is going to remove
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        if (email?.isNotEmpty() == true) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        introBinding?.apply {

            forgotPassTv.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_resetPassword)
            }

            guestUserLogin.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("guest", true)
                startActivity(intent)
            }

            loginBtn.setOnClickListener {

                val userEmail = loginEmailEt.text
                val loginPasswordEt = loginPasswordEt.text

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                if (userEmail?.toString()?.isNotEmpty() == true && loginPasswordEt?.toString()
                        ?.isNotEmpty() == true && isEmailValid && isPasswordValid
                ) {
                    registrationViewModel.loginUser(
                        Login(
                            changePass = true,
                            emailId = userEmail.toString(),
                            isAdmin = 0,
                            massage = "",
                            password = loginPasswordEt.toString(),
                            userName = ""
                        )
                    )
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please enter valid details", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            createAccountTv.setOnClickListener {
                val intent = Intent(requireContext(), RegistrationActivity::class.java)
                startActivity(intent)
            }
        }

        introBinding?.loginEmailEt?.addTextChangedListener { editable ->
            editable?.let {
                if (editable.toString().isNotEmpty() && editable.toString().length > 8) {
                    if (Validator.emailValidation(editable.toString())) {
                        isEmailValid = true
                        introBinding?.emailValidationTv?.visibility = View.GONE
                    } else {
                        isEmailValid = false
                        introBinding?.emailValidationTv?.visibility = View.VISIBLE
                        introBinding?.emailValidationTv?.text =
                            "Please enter valid email"
                    }
                } else {
                    isEmailValid = false
                    introBinding?.emailValidationTv?.visibility = View.GONE
                }
            }
        }

        introBinding?.loginPasswordEt?.addTextChangedListener { editable ->
            editable?.let {
                if (it.toString().isNotEmpty() && it.toString().length >= 6) {
                    if (Validator.passwordValidation(it.toString())) {
                        isPasswordValid = true
                        introBinding?.passwordValidationTv?.visibility = View.GONE
                    } else {
                        isPasswordValid = false
                        introBinding?.passwordValidationTv?.text =
                            "Please enter valid password"
                        introBinding?.passwordValidationTv?.visibility = View.VISIBLE
                    }
                } else {
                    isPasswordValid = false
                    introBinding?.passwordValidationTv?.visibility = View.GONE
                }
            }
        }

        registrationViewModel.loginData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    introBinding?.progressBarCommunityBottom?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    introBinding?.progressBarCommunityBottom?.visibility = View.GONE
                    if (response.data?.userName.equals("failed")) {
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "Please enter valid email and password", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        response.data?.emailId?.let {
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(Constant.USER_EMAIL, it)
                        }
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
                is Resource.Error -> {
                    introBinding?.progressBarCommunityBottom?.visibility = View.GONE
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
                    activity?.finish()
                }
            })

        return introBinding?.root
    }

    override fun onResume() {
        super.onResume()
        introBinding?.loginEmailEt?.setText("")
        introBinding?.loginPasswordEt?.setText("")
    }

}