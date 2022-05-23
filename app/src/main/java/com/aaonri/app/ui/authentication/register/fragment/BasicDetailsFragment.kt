package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentBasicDetailsBinding
import com.aaonri.app.databinding.FragmentIntroBinding

class BasicDetailsFragment : Fragment() {
    var basicDetailsBinding: FragmentBasicDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        basicDetailsBinding = FragmentBasicDetailsBinding.inflate(inflater, container, false)



        return basicDetailsBinding?.root
    }
}