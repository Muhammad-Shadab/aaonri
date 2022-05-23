package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {
    var introBinding: FragmentIntroBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        introBinding = FragmentIntroBinding.inflate(inflater, container, false)

        introBinding?.apply {
            registerBtn.setOnClickListener {
                findNavController().navigate(R.id.action_introFragment_to_user_registration_graph)
            }
        }

        return introBinding?.root
    }
}