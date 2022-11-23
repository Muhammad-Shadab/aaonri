package com.aaonri.app.ui.dashboard.fragment.jobs.seeker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.model.JobSearchFilterModel
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobPagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobScreenFragment : Fragment() {
    var binding: FragmentJobScreenBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    private val tabTitles =
        arrayListOf("All Jobs", "Job Alerts", "My Profile")
    lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobScreenBinding.inflate(inflater, container, false)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val userName =
            context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

        val isUserLogin =
            context.let {
                it?.let { it1 -> PreferenceManager<Boolean>(it1) }
                    ?.get(Constant.IS_USER_LOGIN, false)
            }

        val fragment = this
        val jobPagerAdapter = JobPagerAdapter(fragment)

        jobSeekerViewModel.getUserJobProfileByEmail(
            emailId = email ?: "",
            isApplicant = true
        )


        /** Dialog for edit/update profile and logout user **/
        val updateLogoutDialog = Dialog(requireContext())
        updateLogoutDialog.setContentView(R.layout.update_profile_dialog)
        updateLogoutDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dialog_shape
            )
        )

        updateLogoutDialog.setCancelable(false)
        val editProfileBtn =
            updateLogoutDialog.findViewById<TextView>(R.id.editProfileBtn)
        val logOutBtn =
            updateLogoutDialog.findViewById<TextView>(R.id.logOutBtn)
        val closeDialogBtn =
            updateLogoutDialog.findViewById<ImageView>(R.id.closeDialogBtn)
        val dialogProfileIv =
            updateLogoutDialog.findViewById<ImageView>(R.id.profilePicIv)
        val userNameTv =
            updateLogoutDialog.findViewById<TextView>(R.id.userNameTv)
        val userEmailTv =
            updateLogoutDialog.findViewById<TextView>(R.id.userEmailTv)

        userNameTv.text = userName
        userEmailTv.text = email
        context?.let {
            Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).circleCrop().error(R.drawable.profile_pic_placeholder)
                .into(dialogProfileIv)
        }

        val window: Window? = updateLogoutDialog.window
        val wlp: WindowManager.LayoutParams? = window?.attributes

        wlp?.gravity = Gravity.TOP
        window?.attributes = wlp

        logOutBtn.setOnClickListener {
            updateLogoutDialog.dismiss()
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure you want to Logout?")
            builder.setPositiveButton("OK") { dialog, which ->

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.BLOCKED_USER_ID, "")

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
            updateLogoutDialog.dismiss()
        }

        editProfileBtn.setOnClickListener {
            val action =
                JobScreenFragmentDirections.actionJobScreenFragmentToUpdateProfileFragment(true)
            findNavController().navigate(action)
            updateLogoutDialog.dismiss()
        }

        /**  Dialog for guest user **/
        val guestUserLoginDialog = Dialog(requireContext())
        guestUserLoginDialog.setContentView(R.layout.guest_user_login_dialog)
        guestUserLoginDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dialog_shape
            )
        )
        guestUserLoginDialog.setCancelable(false)
        val dismissBtn =
            guestUserLoginDialog.findViewById<TextView>(R.id.dismissDialogTv)
        val loginBtn =
            guestUserLoginDialog.findViewById<TextView>(R.id.loginDialogTv)

        loginBtn.setOnClickListener {
            activity?.finish()
        }
        dismissBtn.setOnClickListener {
            guestUserLoginDialog.dismiss()
        }

        binding?.apply {

            context?.let {
                Glide.with(it).load(profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop().error(R.drawable.profile_pic_placeholder).into(profilePicIv)
            }

            navigateBack.setOnClickListener {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToHomeScreenFragment()
                findNavController().navigate(action)
            }

            profilePicCv.setOnClickListener {
                if (isUserLogin == true) {
                    updateLogoutDialog.show()
                } else {
                    guestUserLoginDialog.show()
                }
            }

            jobScreenViewPager.adapter = jobPagerAdapter
            TabLayoutMediator(
                jobsScreenTabLayout,
                jobScreenViewPager
            ) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                jobsScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }

            jobsScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }
            })

            jobScreenViewPager.isUserInputEnabled = false

            searchViewIcon.setOnClickListener {
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "",
                        location = "",
                        yearsOfExperience = "",
                        jobType = "",
                        industries = ""
                    )
                )
                searchView.performClick()
            }

            searchView.setOnClickListener {
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "",
                        location = "",
                        yearsOfExperience = "",
                        jobType = "",
                        industries = ""
                    )
                )
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobSearchFragment()
                findNavController().navigate(action)
            }

            if (isUserLogin == false) {
                jobsScreenTabLayout.visibility = View.GONE
                jobScreenViewPager.isUserInputEnabled = false
            }

        }

        /**Calling list of all experience api just because it is used in two different screens**/
        jobSeekerViewModel.getAllActiveExperienceLevel()

        jobSeekerViewModel.navigateAllJobToDetailsJobScreen.observe(viewLifecycleOwner) { jobId ->
            if (jobId != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobDetailsFragment(
                        jobId,
                        false
                    )
                findNavController().navigate(action)
                jobSeekerViewModel.navigateAllJobToDetailsJobScreen.postValue(null)
            }
        }

        jobSeekerViewModel.navigateToUploadJobProfileScreen.observe(viewLifecycleOwner) {
            if (it != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobProfileUploadFragment(
                        false,
                        0
                    )
                findNavController().navigate(action)
                jobSeekerViewModel.navigateToUploadJobProfileScreen.postValue(null)
            }
        }

        jobSeekerViewModel.navigateToUpdateJobProfileScreen.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobProfileUploadFragment(
                        true,
                        pair.second
                    )
                findNavController().navigate(action)
                jobSeekerViewModel.navigateToUpdateJobProfileScreen.postValue(null)
            }
        }

        jobSeekerViewModel.userJobProfileCoverLetterValue.observe(viewLifecycleOwner) {
            if (it != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToCoverLetterBottomSheet()
                findNavController().navigate(action)
            }
        }

        jobSeekerViewModel.navigateToCreateJobAlert.observe(viewLifecycleOwner) {
            if (it != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobCreateAlertFragment(
                        false
                    )
                findNavController().navigate(action)
                jobSeekerViewModel.navigateToCreateJobAlert.postValue(null)
            }
        }

        jobSeekerViewModel.navigateToUpdateJobAlert.observe(viewLifecycleOwner) {
            if (it != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobCreateAlertFragment(true)
                findNavController().navigate(action)
            }
        }

        jobSeekerViewModel.changeJobScreenTab.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    "VIEW MY JOB PROFILE" -> {
                        Handler(Looper.getMainLooper()).postDelayed(
                            { binding?.jobsScreenTabLayout?.getTabAt(2)?.select() }, 100
                        )
                    }
                    "VIEW JOBS" -> {
                        Handler(Looper.getMainLooper()).postDelayed(
                            { binding?.jobsScreenTabLayout?.getTabAt(0)?.select() }, 100
                        )
                    }
                    "VIEW MY JOB ALERTS" -> {
                        Handler(Looper.getMainLooper()).postDelayed(
                            { binding?.jobsScreenTabLayout?.getTabAt(1)?.select() }, 100
                        )
                    }
                }
                jobSeekerViewModel.changeJobScreenTab.postValue(null)
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        JobScreenFragmentDirections.actionJobScreenFragmentToHomeScreenFragment()
                    findNavController().navigate(action)
                }
            })


        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}