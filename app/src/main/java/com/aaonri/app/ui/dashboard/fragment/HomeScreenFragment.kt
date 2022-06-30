package com.aaonri.app.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    var homeScreenBinding: FragmentHomeScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        homeScreenBinding?.apply {

            homeTv.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            }

        }

        /* dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner){
             if (it) {
                 homeScreenBinding?.logOutBtn?.visibility = View.GONE
             } else {
                 homeScreenBinding?.logOutBtn?.visibility = View.VISIBLE
             }
         }*/

        return homeScreenBinding?.root
    }
}