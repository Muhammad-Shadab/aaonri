package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.JobDetails
import com.aaonri.app.data.jobs.recruiter.model.JobSearchRequest
import com.aaonri.app.data.jobs.seeker.model.JobSearchFilterModel
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobSearchBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobSearchAdapter
import com.aaonri.app.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobSearchFragment : Fragment() {
    var binding: FragmentJobSearchBinding? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var jobAdapter: JobSearchAdapter? = null
    var jobSearchFilterModel: JobSearchFilterModel? = null
    var noOfSelectedFilter = 0
    var selectedJobItem: JobDetails? = null
    var isFilterEnable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobSearchBinding.inflate(layoutInflater, container, false)

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
                JobSearchFragmentDirections.actionJobSearchFragmentToUpdateProfileFragment(true)
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

        jobAdapter = JobSearchAdapter { isJobApplyBtnClicked, value ->
            if (isUserLogin == true) {
                if (isJobApplyBtnClicked) {
                    /** Clicked on Apply btn **/
                    selectedJobItem = value
                    /** This function will navigate user to job apply fragment screen if user already uploaded job seeker profile otherwise it will navigate to upload job seeker profile screen**/
                    navigateToJobApplyFragment()
                } else {
                    val action =
                        JobSearchFragmentDirections.actionJobSearchFragmentToJobDetailsFragment(
                            value.jobId,
                            true
                        )
                    findNavController().navigate(action)
                }
            } else {
                guestUserLoginDialog.show()
            }
        }


        binding?.apply {

            val searchKeyword = arguments?.get("searchKeyword")

            if (searchKeyword != null) {
                if (searchKeyword.toString().isNotEmpty()) {
                    searchView.setText(searchKeyword.toString())
                    jobSeekerViewModel.setJobSearchFilterData(
                        JobSearchFilterModel(
                            companyName = "",
                            location = "",
                            yearsOfExperience = "",
                            jobType = "",
                            industries = ""
                        )
                    )
                } else {
                    searchView.requestFocus()
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            val imm =
                                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.toggleSoftInput(
                                InputMethodManager.SHOW_FORCED,
                                InputMethodManager.HIDE_IMPLICIT_ONLY
                            )
                        }, 100
                    )
                }
            } else {
                searchView.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        val imm =
                            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.toggleSoftInput(
                            InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY
                        )
                    }, 100
                )
            }

            if (searchView.text.toString().isNotEmpty()) {
                cancelbutton.visibility = View.VISIBLE
                searchViewIcon.visibility = View.GONE
            } else {
                cancelbutton.visibility = View.GONE
                searchViewIcon.visibility = View.VISIBLE
            }

            recyclerViewAllJob.layoutManager = LinearLayoutManager(context)
            recyclerViewAllJob.adapter = jobAdapter



            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    jobSeekerViewModel.setJobSearchFilterData(
                        JobSearchFilterModel(
                            companyName = "",
                            location = "",
                            yearsOfExperience = "",
                            jobType = "",
                            industries = ""
                        )
                    )
                }
                false
            }

            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (searchView.text.toString().isNotEmpty()) {
                        cancelbutton.visibility = View.VISIBLE
                        searchViewIcon.visibility = View.GONE
                    } else {
                        cancelbutton.visibility = View.GONE
                        searchViewIcon.visibility = View.VISIBLE
                    }
                    if (searchView.hasFocus()) {
                        if (keyword.toString().isEmpty()) {
                            //postClassifiedViewModel.setKeyClassifiedKeyboardListener(true)
                        } else {
                            //postClassifiedViewModel.setKeyClassifiedKeyboardListener(false)
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

            cancelbutton.setOnClickListener {
                cancelbutton.visibility = View.GONE
                searchViewIcon.visibility = View.VISIBLE
                searchView.setText("")
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "",
                        location = "",
                        yearsOfExperience = "",
                        jobType = "",
                        industries = ""
                    )
                )
            }

            navigateBack.setOnClickListener {
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "",
                        location = "",
                        yearsOfExperience = "",
                        jobType = "",
                        industries = ""
                    )
                )
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
                Glide.with(it).load(profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop().error(R.drawable.profile_pic_placeholder).into(profilePicIv)
            }

            filterIcon.setOnClickListener {
                val action =
                    JobSearchFragmentDirections.actionJobSearchFragmentToJobSeekerFilterFragment()
                findNavController().navigate(action)
            }

            deleteCompanyNameFilterIv.setOnClickListener {
                companyNameFilterCv.visibility = View.GONE
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "",
                        location = "${jobSearchFilterModel?.location}",
                        yearsOfExperience = "${jobSearchFilterModel?.yearsOfExperience}",
                        jobType = "${jobSearchFilterModel?.jobType}",
                        industries = "${jobSearchFilterModel?.industries}"
                    )
                )
            }

            deleteLocationFilterIv.setOnClickListener {
                locationFilterCv.visibility = View.GONE
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "${jobSearchFilterModel?.companyName}",
                        location = "",
                        yearsOfExperience = "${jobSearchFilterModel?.yearsOfExperience}",
                        jobType = "${jobSearchFilterModel?.jobType}",
                        industries = "${jobSearchFilterModel?.industries}"
                    )
                )
            }

            deleteExperienceFilterIv.setOnClickListener {
                experienceFilterCv.visibility = View.GONE
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "${jobSearchFilterModel?.companyName}",
                        location = "${jobSearchFilterModel?.location}",
                        yearsOfExperience = "",
                        jobType = "${jobSearchFilterModel?.jobType}",
                        industries = "${jobSearchFilterModel?.industries}"
                    )
                )
            }

            deleteJobTypeFilterIv.setOnClickListener {
                jobTypeFilterCv.visibility = View.GONE
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "${jobSearchFilterModel?.companyName}",
                        location = "${jobSearchFilterModel?.location}",
                        yearsOfExperience = "${jobSearchFilterModel?.yearsOfExperience}",
                        jobType = "",
                        industries = "${jobSearchFilterModel?.industries}"
                    )
                )
            }

            deleteIndustriesFilterIv.setOnClickListener {
                industriesFilterCv.visibility = View.GONE
                jobSeekerViewModel.setJobSearchFilterData(
                    JobSearchFilterModel(
                        companyName = "${jobSearchFilterModel?.companyName}",
                        location = "${jobSearchFilterModel?.location}",
                        yearsOfExperience = "${jobSearchFilterModel?.yearsOfExperience}",
                        jobType = "${jobSearchFilterModel?.jobType}",
                        industries = ""
                    )
                )
            }


            jobSeekerViewModel.jobSearchFilterData.observe(viewLifecycleOwner) { filterData ->
                jobSearchFilterModel = filterData
                noOfSelectedFilter = 0
                if (filterData.companyName.isNotEmpty()) {
                    noOfSelectedFilter++
                    companyNameFilterCv.visibility = View.VISIBLE
                    companyNameFilterTv.text = "Company Name: ${filterData.companyName}"
                }

                if (filterData.industries.isNotEmpty()) {
                    noOfSelectedFilter++
                    industriesFilterCv.visibility = View.VISIBLE
                    industriesFilterTv.text = "Industries: ${filterData.industries}"
                }

                if (filterData.location.isNotEmpty()) {
                    noOfSelectedFilter++
                    locationFilterCv.visibility = View.VISIBLE
                    locationFilterTv.text = "Location: ${filterData.location}"
                }

                if (filterData.yearsOfExperience.isNotEmpty()) {
                    noOfSelectedFilter++
                    experienceFilterCv.visibility = View.VISIBLE
                    experienceFilterTv.text = "Experience: ${filterData.yearsOfExperience}"
                }

                if (filterData.jobType.isNotEmpty()) {
                    noOfSelectedFilter++
                    jobTypeFilterCv.visibility = View.VISIBLE
                    jobTypeFilterTv.text = "Job Type: ${filterData.jobType}"
                }

                numberOfAppliedFilter(noOfSelectedFilter)

                jobSeekerViewModel.searchJob(
                    JobSearchRequest(
                        city = filterData.location,
                        company = filterData.companyName,
                        createdByMe = false,
                        experience = filterData.yearsOfExperience,
                        industry = filterData.industries,
                        jobType = filterData.jobType,
                        keyWord = searchView.text.trim().toString(),
                        skill = "",
                        userEmail = "$email"
                    )
                )


                if (filterData.companyName.isNotEmpty() || filterData.industries.isNotEmpty() || filterData.location.isNotEmpty() || filterData.yearsOfExperience.isNotEmpty() || filterData.jobType.isNotEmpty()
                ) {
                    isFilterEnable = true
                    selectedFiltersRow.visibility = View.VISIBLE
                    numberOfSelectedFilterCv.visibility = View.VISIBLE
                } else {
                    isFilterEnable = false
                    CustomDialog.hideLoader()
                    selectedFiltersRow.visibility = View.GONE
                    numberOfSelectedFilterCv.visibility = View.GONE
                    noResultFound.visibility = View.GONE
                }
            }


            jobSeekerViewModel.searchJobData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        if (response.data?.jobDetailsList?.isNotEmpty() == true) {
                            jobAdapter?.setData(response.data.jobDetailsList)
                            noResultFound.visibility = View.GONE
                            recyclerViewAllJob.visibility = View.VISIBLE
                        } else {
                            noResultFound.visibility = View.VISIBLE
                            recyclerViewAllJob.visibility = View.GONE
                        }
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                    }
                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    /** Cleared all filters  **/
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                    jobSeekerViewModel.setJobSearchFilterData(
                        JobSearchFilterModel(
                            companyName = "",
                            location = "",
                            yearsOfExperience = "",
                            jobType = "",
                            industries = ""
                        )
                    )
                    findNavController().navigateUp()
                }
            })

        return binding?.root
    }

    private fun navigateToJobApplyFragment() {
        jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    response.data?.let {
                        if (it.jobProfile.isNotEmpty()) {
                            val action =
                                selectedJobItem?.jobId?.let { it1 ->
                                    JobSearchFragmentDirections.actionJobSearchFragmentToJobApplyFragment(
                                        it1, true
                                    )
                                }
                            if (action != null) {
                                findNavController().navigate(action)
                            } else {

                            }
                        } else {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Confirm")
                            builder.setMessage("Seems you haven't uploaded your job profile yet.")
                            builder.setPositiveButton("Upload Profile") { dialog, which ->
                                val action =
                                    JobSearchFragmentDirections.actionJobSearchFragmentToJobProfileUploadFragment(
                                        false,
                                        0
                                    )
                                findNavController().navigate(action)
                            }
                            builder.setNegativeButton("Cancel") { dialog, which ->

                            }
                            builder.show()
                        }
                    }
                }
                is Resource.Error -> {
                }
            }
        }
    }

    private fun numberOfAppliedFilter(value: Int) {
        binding?.numberOfSelectedFilterTv?.text = value.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}