package com.aaonri.app.ui.dashboard.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentMoreScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


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
        adapter = ServicesItemAdapter({ selectedCommunity ->
            authCommonViewModel.addServicesList(selectedCommunity)
        },{isJobSelected = it}) {
           when(it)
           {
               "Classifieds" -> {
                   dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
               }
               "Events" -> {
                   findNavController().navigate( MoreScreenFragmentDirections.actionMoreScreenFragmentToEventScreenFragment())
               }
               "Jobs" -> {

               }
               "Immigration" -> {
                   findNavController().navigate( MoreScreenFragmentDirections.actionMoreScreenFragmentToImmigrationScreenFragment())

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
                binding?.logOutBtn?.visibility = View.GONE
            } else {
                binding?.logOutBtn?.visibility = View.VISIBLE
            }
        }

        binding?.apply {

            logOutBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure you want to Logout?")
                builder.setPositiveButton("OK") { dialog, which ->

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_EMAIL, "")
                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_ZIP_CODE, "")
                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_CITY, "")
                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_STATE, "")
                    context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                        ?.set(Constant.IS_USER_LOGIN, false)

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_PROFILE_PIC, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.GMAIL_FIRST_NAME, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.GMAIL_LAST_NAME, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_INTERESTED_SERVICES, "")

                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.gmail_client_id))
                        .requestEmail()
                        .build()

                    FirebaseAuth.getInstance().signOut()
                    LoginManager.getInstance().logOut()
                    mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
                    mGoogleSignInClient.signOut()

                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->

                }
                builder.show()
            }

            binding?.servicesGridRecyclerView?.adapter = adapter
            binding?.servicesGridRecyclerView?.layoutManager = GridLayoutManager(context, 3)




        }

        return binding?.root
    }

    private fun getServicesInterestList() {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {

        super.onResume()
        registrationViewModel.service.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { servicesResponse ->
                        /*for (list in servicesResponse) {
                            if (list.active) {
                                serviceList.add(list)
                            }
                        }*/
                        //serviceList.let { adapter?.setData(it) }
                        adapter?.setData(servicesResponse.filter { it.active },true)

                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

}