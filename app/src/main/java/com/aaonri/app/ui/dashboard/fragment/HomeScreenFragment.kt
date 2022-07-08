package com.aaonri.app.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
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

        val origin = context?.let { PreferenceManager<String>(it)[Constant.USER_CITY, ""] }

        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner){
            if (it) {
                homeScreenBinding?.profilePicCv?.visibility = View.GONE
                homeScreenBinding?.bellIconIv?.visibility = View.GONE
            } else {
                homeScreenBinding?.profilePicCv?.visibility = View.VISIBLE
                homeScreenBinding?.bellIconIv?.visibility = View.VISIBLE
            }
        }

        homeScreenBinding?.apply {

            if (origin?.isNotEmpty() == true) {
                locationTv.text = origin
                locationTv.visibility = View.VISIBLE
                locationIcon.visibility = View.VISIBLE
            } else {
                locationTv.visibility = View.GONE
                locationIcon.visibility = View.GONE
            }

            homeTv.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            }

        }



        return homeScreenBinding?.root
    }
}