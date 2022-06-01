package com.aaonri.app.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLoginBinding
import com.aaonri.app.util.Constant
import com.example.newsapp.utils.Resource
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

        val userEmail = introBinding?.loginEmailEt?.text
        val loginPasswordEt = introBinding?.loginPasswordEt?.text


        introBinding?.apply {

            loginBtn.setOnClickListener {
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
                    Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show()
                }
            }

            createAccountTv.setOnClickListener {
                findNavController().navigate(R.id.action_introFragment_to_onbardingNavHostFragment)
            }
        }




        registrationViewModel.loginData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    introBinding?.progressBarCommunityBottom?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    introBinding?.progressBarCommunityBottom?.visibility = View.GONE
                    Toast.makeText(context, "${response.data?.userName}", Toast.LENGTH_SHORT)
                        .show()
                    if (response.data?.userName.equals("failed")) {
                        Toast.makeText(context, "Check your email and password", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        findNavController().navigate(R.id.action_introFragment_to_dashboard_nav_graph)
                        Toast.makeText(context, "Successfully login", Toast.LENGTH_SHORT)
                            .show()
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
}