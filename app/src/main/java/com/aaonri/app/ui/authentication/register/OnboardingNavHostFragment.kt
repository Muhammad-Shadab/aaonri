package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.FragmentOnbardingNavHostBinding

class OnboardingNavHostFragment : Fragment() {
    var onboardingBinding: FragmentOnbardingNavHostBinding? = null
    val commonViewModel: CommonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onboardingBinding = FragmentOnbardingNavHostBinding.inflate(inflater, container, false)

        onboardingBinding?.apply {

            stepView.go(2, true)

            navigateBack.setOnClickListener {
                activity?.onBackPressed()
            }
        }

        commonViewModel.navigateToLoginScreen?.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            }
        }


        return onboardingBinding?.root
    }

}