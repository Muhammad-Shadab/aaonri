package com.aaonri.app.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    var homeScreenBinding: FragmentHomeScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
        return homeScreenBinding?.root
    }
}