package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.databinding.FragmentOnbardingNavHostBinding

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