package com.aaonri.app.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.MainActivity
import com.aaonri.app.R
import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLoginBinding
import com.aaonri.app.ui.authentication.register.RegistrationActivity
import com.aaonri.app.util.Constant
import com.example.newsapp.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginFragment : Fragment() {
    val registrationViewModel: RegistrationViewModel by viewModels()
    var introBinding: FragmentLoginBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        introBinding = FragmentLoginBinding.inflate(inflater, container, false)

        introBinding?.apply {

            forgotPassTv.setOnClickListener {
                // findNavController().navigate(R.id.action_loginFragment_to_forgot_password_nav)
            }

            guestUserLogin.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }

            loginBtn.setOnClickListener {

                val userEmail = loginEmailEt.text
                val loginPasswordEt = loginPasswordEt.text

                if (userEmail?.toString()?.isNotEmpty() == true && loginPasswordEt?.toString()
                        ?.isNotEmpty() == true
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
        return introBinding?.root
    }

    override fun onResume() {
        super.onResume()
        introBinding?.loginEmailEt?.setText("")
        introBinding?.loginPasswordEt?.setText("")
    }

}