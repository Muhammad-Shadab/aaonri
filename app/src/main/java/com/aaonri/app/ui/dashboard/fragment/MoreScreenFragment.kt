package com.aaonri.app.ui.dashboard.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentMoreScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class MoreScreenFragment : Fragment() {
    var binding: FragmentMoreScreenBinding? = null
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    private var adapter: ServicesItemAdapter? = null
    var isJobSelected = false
    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreScreenBinding.inflate(inflater, container, false)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val userNamePref = context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

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

        val dialog = Dialog(requireContext())

        /** Dialog for edit/update profile and logout user **/
        dialog.setContentView(R.layout.update_profile_dialog)
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dialog_shape
            )
        )

        dialog.setCancelable(false)
        val editProfileBtn =
            dialog.findViewById<TextView>(R.id.editProfileBtn)
        val logOutBtn =
            dialog.findViewById<TextView>(R.id.logOutBtn)
        val closeDialogBtn =
            dialog.findViewById<ImageView>(R.id.closeDialogBtn)
        val dialogProfileIv =
            dialog.findViewById<ImageView>(R.id.profilePicIv)
        val userNameDialogTv =
            dialog.findViewById<TextView>(R.id.userNameTv)
        val userEmailDialogTv =
            dialog.findViewById<TextView>(R.id.userEmailTv)

        val window: Window? = dialog.window
        val wlp: WindowManager.LayoutParams? = window?.attributes

        wlp?.gravity = Gravity.TOP
        window?.attributes = wlp

        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            if (it) {
                binding?.profileInfoLl?.visibility = View.GONE
                binding?.view?.visibility = View.GONE
            } else {
                binding?.view?.visibility = View.VISIBLE
                binding?.profileInfoLl?.visibility = View.VISIBLE
            }
        }

        binding?.apply {

            useremailTv.text = email
            userNameTv.text = userNamePref
            userNameDialogTv.text = userNamePref
            userEmailDialogTv.text = email

            context?.let {
                Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).centerCrop().into(dialogProfileIv)
            }

            context?.let {
                Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).centerCrop().error(R.drawable.profile_pic_placeholder)
                    .into(profilePicIv)
            }

            profilePicCv.setOnClickListener {
                val action =
                    MoreScreenFragmentDirections.actionMoreScreenFragmentToUpdateProfileFragment()
                findNavController().navigate(action)
                //dialog.show()
            }

            editProfileBtn.setOnClickListener {
                val action =
                    MoreScreenFragmentDirections.actionMoreScreenFragmentToUpdateProfileFragment()
                findNavController().navigate(action)
                dialog.dismiss()
            }

            logOutBtn.setOnClickListener {
                dialog.dismiss()
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

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_NAME, "")

                    context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                        ?.set(Constant.IS_JOB_RECRUITER, false)

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_PHONE_NUMBER, "")

                    context?.let { it1 -> PreferenceManager<Int>(it1) }
                        ?.set(Constant.USER_ID, 0)

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

            closeDialogBtn.setOnClickListener {
                dialog.dismiss()
            }

            servicesGridRecyclerView.adapter = adapter
            servicesGridRecyclerView.layoutManager = GridLayoutManager(context, 3)
        }

        registrationViewModel.service.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    var jobId = 0
                    if (BuildConfig.FLAVOR == "dev") {
                        jobId = 17
                    } else {
                        jobId = 3
                    }
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { servicesResponse ->
                        adapter?.setData(
                            servicesResponse.filter { it.active && it.id != jobId },
                            true
                        )
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}