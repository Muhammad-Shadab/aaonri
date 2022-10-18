package com.aaonri.app.ui.dashboard.fragment.immigration

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.immigration.ImmigrationStaticData
import com.aaonri.app.data.immigration.model.ImmigrationFilterModel
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigartionScreenFrgamentBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationPagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
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
class ImmigrationScreenFragment : Fragment() {
    var binding: FragmentImmigartionScreenFrgamentBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    var immigrationFilterModel: ImmigrationFilterModel? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var noOfSelectedFilter = 2
    var isFilterEnable = false
    private val tabTitles =
        arrayListOf("All Discussions", "My Discussions", "Information center")

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentImmigartionScreenFrgamentBinding.inflate(layoutInflater, container, false)

        val searchKeyword = arguments?.get("searchKeyword")

        val pagerAdapter = ImmigrationPagerAdapter(this, searchKeyword.toString())

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val userName =
            context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

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
            Glide.with(it).load(profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .circleCrop().error(R.drawable.profile_pic_placeholder)
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

        editProfileBtn.setOnClickListener {
            updateLogoutDialog.dismiss()
            val action =
                ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToUpdateProfileFragment()
            findNavController().navigate(action)
        }

        closeDialogBtn.setOnClickListener {
            updateLogoutDialog.dismiss()
        }


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
                    .centerCrop().error(R.drawable.profile_pic_placeholder)
                    .into(profilePicIv)
            }
            immigrationScreenViewPager.isUserInputEnabled = false
            immigrationScreenViewPager.adapter = pagerAdapter

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            filterImmigration.setOnClickListener {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationFilterFragment()
                findNavController().navigate(action)
            }

            profilePicCv.setOnClickListener {
                if (isUserLogin == false) {
                    activity?.finish()
                } else {
                    updateLogoutDialog.show()
                }
            }

            floatingActionBtnImmigration.setOnClickListener {
                if (isUserLogin == true) {
                    val action =
                        ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToPostImmigrationFragment(
                            false
                        )
                    findNavController().navigate(action)
                } else {
                    guestUserLoginDialog.show()
                }
            }

            deleteDateRangeFilter.setOnClickListener {
                numberOfAppliedFilter(--noOfSelectedFilter)
                binding?.dateRangeCv?.visibility = View.GONE
                immigrationViewModel.setFilterData(
                    ImmigrationFilterModel(
                        fifteenDaysSelected = false,
                        threeMonthSelected = false,
                        oneYearSelected = false,
                        activeDiscussion = immigrationFilterModel!!.activeDiscussion,
                        atLeastOnDiscussion = immigrationFilterModel!!.atLeastOnDiscussion
                    )
                )
            }

            /*deleteActiveDiscussionFilterIv.setOnClickListener {
                binding?.activeDiscussionFilterCv?.visibility = View.GONE
                immigrationViewModel.setFilterData(
                    ImmigrationFilterModel(
                        startDate = null,
                        endDate = null,
                        activeDiscussion = false,
                        atLeastOnDiscussion = immigrationFilterModel!!.atLeastOnDiscussion
                    )
                )
            }*/

            deleteAtLeastOneResponseFilterIv.setOnClickListener {
                numberOfAppliedFilter(--noOfSelectedFilter)
                binding?.atLeastOneResponseFilterCv?.visibility = View.GONE
                immigrationViewModel.setFilterData(
                    ImmigrationFilterModel(
                        fifteenDaysSelected = immigrationFilterModel!!.fifteenDaysSelected,
                        threeMonthSelected = immigrationFilterModel!!.threeMonthSelected,
                        oneYearSelected = immigrationFilterModel!!.oneYearSelected,
                        activeDiscussion = immigrationFilterModel!!.activeDiscussion,
                        atLeastOnDiscussion = false
                    )
                )
            }

            TabLayoutMediator(
                immigrationScreenTabLayout,
                immigrationScreenViewPager
            ) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                immigrationScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }

            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    immigrationViewModel.setFilterData(
                        ImmigrationFilterModel(
                            fifteenDaysSelected = false,
                            threeMonthSelected = false,
                            oneYearSelected = false,
                            activeDiscussion = false,
                            atLeastOnDiscussion = false
                        )
                    )
                    immigrationScreenTabLayout.getTabAt(0)?.select()
                    immigrationViewModel.setSearchQuery(textView.text.toString())
                }
                false
            }

            /*searchViewIcon.setOnClickListener {
                immigrationScreenTabLayout.getTabAt(0)?.select()
                immigrationViewModel.setSearchQuery(searchView.text.toString())
            }*/


            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (keyword.toString().isEmpty()) {
                        cancelbutton.visibility = View.GONE
                        searchViewIcon.visibility = View.VISIBLE
                    } else {
                        cancelbutton.visibility = View.VISIBLE
                        searchViewIcon.visibility = View.GONE
                    }
                    if (searchView.hasFocus()) {
                        if (keyword.toString().isEmpty()) {
                            immigrationViewModel.setSearchQuery(searchView.text.toString())
                        } else {

                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

            cancelbutton.setOnClickListener {
                searchView.setText("")
                cancelbutton.visibility = View.GONE
                searchViewIcon.visibility = View.VISIBLE
                immigrationViewModel.setSearchQuery(searchView.text.toString())
            }


            immigrationScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position != 0) {
                        filterImmigration.isEnabled = false
                        filterImmigration.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.darkGrayColor
                            )
                        )
                        selectedFilters.visibility = View.GONE
                        numberOfSelectedFilterCv.visibility = View.GONE
                        SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                    } else {
                        filterImmigration.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.white
                            )
                        )
                        filterImmigration.isEnabled = true
                        if (isFilterEnable) {
                            selectedFilters.visibility = View.VISIBLE
                            numberOfSelectedFilterCv.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }
            })
            dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
                if (it) {
                    profilePicIv.visibility = View.GONE
                    //bellIconIv.visibility = View.GONE
                    immigrationScreenTabLayout.visibility = View.GONE
                    immigrationScreenViewPager.setPadding(0, 40, 0, 0)
                    immigrationScreenViewPager.isUserInputEnabled = false
                }
            }
        }


        immigrationViewModel.discussionCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    response.data?.get(0)
                        ?.let {
                            /** setting for both category for index 0 for only once **/
                            if (immigrationViewModel.setCategoryForFirstIndexForOnce) {
                                immigrationViewModel.setSelectedAllDiscussionCategory(it)
                                immigrationViewModel.setSelectedMyDiscussionScreenCategory(it)
                                immigrationViewModel.setCategoryFirstIndexForOnce(false)
                            }
                        }

                    response.data?.let { ImmigrationStaticData.setImmigrationCategoryData(it) }
                }
                is Resource.Error -> {

                }
            }
        }

        immigrationViewModel.navigateFromAllImmigrationToDetailScreen.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationDetailsFragment(
                        true,
                        0
                    )
                findNavController().navigate(action)
                immigrationViewModel.setNavigateFromAllImmigrationToDetailScreen(false)
            }
        }


        immigrationViewModel.navigateFromImmigrationCenterToCenterDetailScreen.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationCenterDetails()
                findNavController().navigate(action)
                immigrationViewModel.setNavigateFromImmigrationCenterToCenterDetailScreen(false)
            }
        }

        immigrationViewModel.navigateFromMyImmigrationToDetailScreen.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationDetailsFragment(
                        false,
                        0
                    )
                findNavController().navigate(action)
                immigrationViewModel.setNavigateFromMyImmigrationToDetailScreen(false)
            }
        }

        immigrationViewModel.navigateFromMyImmigrationToUpdateScreen.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToPostImmigrationFragment(
                        true
                    )
                findNavController().navigate(action)
                immigrationViewModel.setNavigateFromMyImmigrationToUpdateScreen(false)
            }
        }

        immigrationViewModel.allDiscussionCategoryIsClicked.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationCategoryBottomSheet(
                        "FromAllDiscussionScreen"
                    )
                findNavController().navigate(action)
                immigrationViewModel.setOnAllDiscussionCategoryIsClicked(false)
            }
        }

        immigrationViewModel.myDiscussionCategoryIsClicked.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationCategoryBottomSheet(
                        "FromMyDiscussionScreen"
                    )
                findNavController().navigate(action)
                immigrationViewModel.setOnMyDiscussionCategoryIsClicked(false)
            }
        }

        immigrationViewModel.immigrationFilterData.observe(viewLifecycleOwner) { filterData ->
            noOfSelectedFilter = 0
            immigrationFilterModel = filterData
            if (filterData.fifteenDaysSelected) {
                noOfSelectedFilter++
                binding?.dateRangeCv?.visibility = View.VISIBLE
                binding?.dateRangeTv?.text = "Range: 15 Days"
            }

            if (filterData.threeMonthSelected) {
                noOfSelectedFilter++
                binding?.dateRangeCv?.visibility = View.VISIBLE
                binding?.dateRangeTv?.text = "Range: 3 Months"
            }

            if (filterData.oneYearSelected) {
                noOfSelectedFilter++
                binding?.dateRangeCv?.visibility = View.VISIBLE
                binding?.dateRangeTv?.text = "Range: 1 Year"
            }

            /*if (filterData.activeDiscussion) {
                binding?.activeDiscussionFilterTv?.text = "Active Discussions"
                binding?.activeDiscussionFilterCv?.visibility = View.VISIBLE
            }*/

            if (filterData.atLeastOnDiscussion) {
                noOfSelectedFilter++
                binding?.atLeastOneResponseFilterTv?.text = "Sort: At Least One Response"
                binding?.atLeastOneResponseFilterCv?.visibility = View.VISIBLE
            }

            if (filterData.fifteenDaysSelected || filterData.threeMonthSelected || filterData.oneYearSelected || filterData.atLeastOnDiscussion) {
                isFilterEnable = true
                binding?.selectedFilters?.visibility = View.VISIBLE
                binding?.numberOfSelectedFilterCv?.visibility = View.VISIBLE
            } else {
                isFilterEnable = false
                binding?.numberOfSelectedFilterCv?.visibility = View.GONE
                binding?.selectedFilters?.visibility = View.GONE
            }

            numberOfAppliedFilter(noOfSelectedFilter)
        }

        immigrationViewModel.clearSearchViewText.observe(viewLifecycleOwner) {
            if (it) {
                binding?.searchView?.setText("")
                immigrationViewModel.setClearSearchViewText(false)
            }
        }

        if (searchKeyword?.toString()?.isNotEmpty() == true) {
            binding?.searchView?.setText(searchKeyword.toString())
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()

                }
            })

        return binding?.root
    }

    private fun numberOfAppliedFilter(value: Int) {
        binding?.numberOfSelectedFilterTv?.text = value.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        immigrationViewModel.immigrationSearchQuery.postValue(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}