package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
    var registrationBinding: FragmentRegistrationBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registrationBinding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return registrationBinding?.root
    }

}