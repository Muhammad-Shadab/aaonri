package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    var introBinding: FragmentLoginBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        introBinding = FragmentLoginBinding.inflate(inflater, container, false)
        introBinding?.apply {
            loginBtn.setOnClickListener {
                findNavController().navigate(R.id.action_introFragment_to_registration_na_graph)
            }
        }

        return introBinding?.root
    }
}