package com.aaonri.app.ui.dashboard.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
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
            logOutBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure you want to Logout")
                builder.setPositiveButton("OK") { dialog, which ->
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->

                }
                builder.show()
            }
        }

        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner){
            if (it) {
                homeScreenBinding?.logOutBtn?.visibility = View.GONE
            } else {
                homeScreenBinding?.logOutBtn?.visibility = View.VISIBLE
            }
        }

        return homeScreenBinding?.root
    }
}