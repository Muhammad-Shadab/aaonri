package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.JobSearchRequest
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentJobRecruiterScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.RecruiterPagerAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job.RecruiterPostJobActivity
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


class JobRecruiterScreenFragment : Fragment() {
    var binding: FragmentJobRecruiterScreenBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()

    private var clicked = false
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_bottom_anim
        )
    }

    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val tabTitles =
        arrayListOf("All Talents", "My Posted Jobs", "Consultant Profile")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobRecruiterScreenBinding.inflate(inflater, container, false)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val userName =
            context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val isUserLogin =
            context.let {
                it?.let { it1 -> PreferenceManager<Boolean>(it1) }
                    ?.get(Constant.IS_USER_LOGIN, false)
            }

        jobRecruiterViewModel.getAllJobProfile()

        /** calling api for my posted job screen **/
        jobRecruiterViewModel.jobSearch(
            JobSearchRequest(
                city = "",
                company = "",
                createdByMe = true,
                experience = "",
                industry = "",
                jobType = "",
                keyWord = "",
                skill = "",
                userEmail = "$email"
            )
        )

        jobRecruiterViewModel.getUserConsultantProfile("$email", false)

        val fragment = this
        val jobPagerAdapter = RecruiterPagerAdapter(fragment)

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
                findNavController().navigateUp()
            }

            profilePicCv.setOnClickListener {
                if (isUserLogin == true) {
                    updateLogoutDialog.show()
                } else {
                    guestUserLoginDialog.show()
                }
            }

            context?.let {
                Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).centerCrop().error(R.drawable.profile_pic_placeholder)
                    .into(profilePicIv)
            }

            floatingActionBtnClassified.setOnClickListener {
                addOnFloatingBtnClick()
            }

            postAJob.setOnClickListener {
                addOnFloatingBtnClick()
                val intent = Intent(context, RecruiterPostJobActivity::class.java)
                activity?.startActivity(intent)
            }

            uploadConsultantProfile.setOnClickListener {
                addOnFloatingBtnClick()
                val action =
                    JobRecruiterScreenFragmentDirections.actionJobRecruiterScreenFragmentToRecruiterUpdateProfileFragment()
                findNavController().navigate(action)
            }

            searchTalentBtn.setOnClickListener {
                addOnFloatingBtnClick()
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
                    if (tab?.position == 2) {

                    } else {

                    }
                    if (tab?.position != 0) {

                    } else {

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }
            })
            jobScreenViewPager.isUserInputEnabled = false
        }


        jobRecruiterViewModel.navigateAllJobProfileScreenToTalentProfileDetailsScreen.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                val action =
                    JobRecruiterScreenFragmentDirections.actionJobRecruiterScreenFragmentToRecruiterTalentDetailsFragment(
                        it
                    )
                findNavController().navigate(action)
                jobRecruiterViewModel.navigateAllJobProfileScreenToTalentProfileDetailsScreen.postValue(
                    null
                )
            }
        }

        jobRecruiterViewModel.navigateFromMyPostedJobToJobDetailsScreen.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                val action =
                    JobRecruiterScreenFragmentDirections.actionJobRecruiterScreenFragmentToRecruiterJobDetailsFragment(
                        it
                    )
                findNavController().navigate(action)
                jobRecruiterViewModel.navigateFromMyPostedJobToJobDetailsScreen.postValue(
                    null
                )
            }
        }

        jobRecruiterViewModel.navigateFromMyPostedJobToJobApplicantScreen.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                val action =
                    JobRecruiterScreenFragmentDirections.actionJobRecruiterScreenFragmentToRecruiterJobApplicantsFragment(
                        it
                    )
                findNavController().navigate(action)
                jobRecruiterViewModel.navigateFromMyPostedJobToJobApplicantScreen.postValue(
                    null
                )
            }
        }





        return binding?.root
    }

    private fun addOnFloatingBtnClick() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding?.searchTalentBtn?.visibility = View.VISIBLE
            binding?.postAJob?.visibility = View.VISIBLE
            binding?.uploadConsultantProfile?.visibility = View.VISIBLE
            binding?.hideBackground?.visibility = View.VISIBLE
            binding?.navigateBack?.visibility = View.GONE
            binding?.profilePicCv?.visibility = View.GONE
        } else {
            binding?.searchTalentBtn?.visibility = View.GONE
            binding?.postAJob?.visibility = View.GONE
            binding?.uploadConsultantProfile?.visibility = View.GONE
            binding?.hideBackground?.visibility = View.GONE
            binding?.navigateBack?.visibility = View.VISIBLE
            binding?.profilePicCv?.visibility = View.VISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            /*binding?.searchTalentBtn?.startAnimation(fromBottom)
            binding?.postAJob?.startAnimation(fromBottom)
            binding?.uploadConsultantProfile?.startAnimation(fromBottom)*/
            binding?.floatingActionBtnClassified?.startAnimation(rotateOpen)
        } else {
            /*binding?.searchTalentBtn?.startAnimation(toBottom)
            binding?.postAJob?.startAnimation(toBottom)
            binding?.uploadConsultantProfile?.startAnimation(toBottom)*/
            binding?.floatingActionBtnClassified?.startAnimation(rotateClose)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}