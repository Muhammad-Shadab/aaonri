package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentOnbardingNavHostBinding
import com.aceinteract.android.stepper.StepperNavigationView

class OnbardingNavHostFragment : Fragment() {
    var onboardingBinding: FragmentOnbardingNavHostBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onboardingBinding = FragmentOnbardingNavHostBinding.inflate(inflater, container, false)

        onboardingBinding?.apply {

        }

        return onboardingBinding?.root
    }

}