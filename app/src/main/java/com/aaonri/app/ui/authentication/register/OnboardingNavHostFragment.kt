package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentOnbardingNavHostBinding

class OnboardingNavHostFragment : Fragment() {
    var onboardingBinding: FragmentOnbardingNavHostBinding? = null
    var steps = listOf("1", "2", "3", "4")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onboardingBinding = FragmentOnbardingNavHostBinding.inflate(inflater, container, false)

         //val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        val nestedNavHostFragment =
            fragmentManager?.findFragmentById(R.id.onbardingNavHostFragment) as? NavHostFragment
        val navController = nestedNavHostFragment?.navController
        val navBackStackEntry = navController?.currentBackStackEntry

        onboardingBinding?.apply {

            stepView.go(2, true)

            navigateBack.setOnClickListener {
               activity?.onBackPressed()
            }
        }

        return onboardingBinding?.root
    }

}