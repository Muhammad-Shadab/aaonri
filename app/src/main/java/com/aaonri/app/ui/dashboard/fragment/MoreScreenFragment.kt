package com.aaonri.app.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentMoreScreenBinding
import com.aaonri.app.ui.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.auth.api.signin.GoogleSignInClient


class MoreScreenFragment : Fragment() {
    var binding: FragmentMoreScreenBinding? = null
    private var adapter: ServicesItemAdapter? = null
    var isJobSelected = false
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreScreenBinding.inflate(inflater, container, false)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        adapter = ServicesItemAdapter({ selectedCommunity ->
            authCommonViewModel.addServicesList(selectedCommunity)
        }, { isJobSelected = it }) {
            when (it) {
                "Classifieds" -> {
                    dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
                }
                "Events" -> {
                    findNavController().navigate(MoreScreenFragmentDirections.actionMoreScreenFragmentToEventScreenFragment())
                }
                "Jobs" -> {

                }
                "Immigration" -> {
                    findNavController().navigate(MoreScreenFragmentDirections.actionMoreScreenFragmentToImmigrationScreenFragment())

                }
                "Shop With Us" -> {
                    dashboardCommonViewModel.setIsShopWithUsClicked(true)
                }

                "Advertise With Us" -> {
                    dashboardCommonViewModel.setIsAdvertiseClicked(true)
                }
            }
        }

        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            if (it) {
                binding?.profileInfoLl?.visibility = View.GONE
            } else {
                binding?.profileInfoLl?.visibility = View.VISIBLE
            }
        }

        binding?.apply {
            useremailTv.text =
                context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
            userNameTv.text = context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

            context?.let {
                Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).centerCrop().into(profilePicIv)
            }

            binding?.servicesGridRecyclerView?.adapter = adapter
            binding?.servicesGridRecyclerView?.layoutManager = GridLayoutManager(context, 3)

        }

        registrationViewModel.service.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { servicesResponse ->
                        adapter?.setData(servicesResponse.filter { it.active && it.id != 17 }, true)

                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        return binding?.root
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}