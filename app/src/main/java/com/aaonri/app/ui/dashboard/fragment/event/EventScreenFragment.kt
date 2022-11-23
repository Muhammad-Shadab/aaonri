package com.aaonri.app.ui.dashboard.fragment.event

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
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.EventStaticData
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.model.EventCategoryResponseItem
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.dashboard.fragment.classified.ClassifiedScreenFragmentDirections
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventPagerAdapter
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
class EventScreenFragment : Fragment() {
    var binding: FragmentEventScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val eventViewModel: EventViewModel by activityViewModels()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val tabTitles =
        arrayListOf("All Events", "My Events", "Recent Events")
    var isUserLogin: Boolean? = null
    var noOfSelection = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventScreenBinding.inflate(inflater, container, false)
        val fragment = this
        isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }
        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val userName =
            context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

        val searchKeyword = arguments?.get("searchKeyword")

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
                EventScreenFragmentDirections.actionEventScreenFragmentToUpdateProfileFragment(false)
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

        if (EventStaticData.getEventCategory().isEmpty()) {
            postEventViewModel.getEventCategory()
        }

        val pagerAdapter = EventPagerAdapter(fragment)

        binding?.apply {

            eventsScreenViewPager.isUserInputEnabled = false

            if (searchKeyword?.toString()?.isNotEmpty() == true) {
                callEventApi(searchKeyword.toString())
                searchView.setText(searchKeyword.toString())
            }

            profilePicCv.setOnClickListener {
                if (isUserLogin == false) {
                    activity?.finish()
                } else {
                    updateLogoutDialog.show()
                }
            }

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            context?.let {
                Glide.with(it).load(profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop().error(R.drawable.profile_pic_placeholder)
                    .into(profilePicIv)
            }

            filterIcon.setOnClickListener {
                findNavController().navigate(R.id.eventFilterScreenFragment)
            }

            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    eventsScreenTabLayout.getTabAt(0)?.select()
                    callEventApi(searchQuery = textView.text.toString())
                    eventViewModel.setSearchQuery(textView.text.toString())
                    eventViewModel.setClearAllFilter(true)
                    eventViewModel.setIsFilterEnable(true)
                }
                false
            }

            searchViewIcon.setOnClickListener {
                eventsScreenTabLayout.getTabAt(0)?.select()
                if (searchView.text.toString().isNotEmpty()) {
                    eventsScreenTabLayout.getTabAt(0)?.select()
                    callEventApi(searchView.text.toString())
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            }

            if (searchView.text.toString().isNotEmpty()) {
                cancelbutton.visibility = View.VISIBLE
                searchViewIcon.visibility = View.GONE
            } else {
                cancelbutton.visibility = View.GONE
                searchViewIcon.visibility = View.VISIBLE
            }

            cancelbutton.setOnClickListener {
                eventViewModel.setClearAllFilter(true)
                eventViewModel.setClickOnClearAllFilterBtn(true)
                callEventApi()
                cancelbutton.visibility = View.GONE
                searchView.setText("")
                searchViewIcon.visibility = View.VISIBLE
            }

            /* eventViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilterClicked ->
                if (isFilterClicked) {
                    noOfSelection = 0
                }
                searchView.setText("")
                setFilterVisibility()
            }*/

            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (searchView.hasFocus()) {
                        if (keyword.toString().isEmpty()) {
                            cancelbutton.visibility = View.GONE
                            searchViewIcon.visibility = View.VISIBLE
                            eventViewModel.setKeyClassifiedKeyboardListener(true)
                        } else {
                            cancelbutton.visibility = View.VISIBLE
                            searchViewIcon.visibility = View.GONE
                            eventViewModel.setKeyClassifiedKeyboardListener(false)
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })



            if (isUserLogin == false) {
                eventsScreenTabLayout.visibility = View.GONE
                eventsScreenViewPager.setPadding(0, 40, 0, 0)
                eventsScreenViewPager.isUserInputEnabled = false
            }
            floatingActionBtnEvents.setOnClickListener {
                if (isUserLogin == true) {
                    val intent = Intent(requireContext(), EventScreenActivity::class.java)
                    startActivityForResult(intent, 2)
                } else {
                    guestUserLoginDialog.show()
                }
            }
            eventsScreenViewPager.adapter = pagerAdapter

            TabLayoutMediator(eventsScreenTabLayout, eventsScreenViewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                eventsScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }

            eventsScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 2) {
                        selectedFilters.visibility = View.INVISIBLE
                    } else {
                        selectedFilters.visibility = View.VISIBLE
                    }
                    if (tab?.position == 1) {
                        eventViewModel.hideFloatingButtonInSecondTab.observe(viewLifecycleOwner) {
                            if (it) {
                                floatingActionBtnEvents.visibility = View.GONE
                            }
                        }
                    } else {
                        floatingActionBtnEvents.visibility = View.VISIBLE
                    }
                    if (tab?.position != 0) {
                        filterIcon.isEnabled = false
                        filterIcon.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.darkGrayColor
                            )
                        )
                        eventViewModel.setClearAllFilter(true)
                        eventViewModel.setClickOnClearAllFilterBtn(true)
                        if (searchView.text.isNotEmpty()) {
                            searchView.setText("")
                        }
                        SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                        eventViewModel.clickedOnFilter.postValue(true)
                    } else {
                        filterIcon.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.white
                            )
                        )
                        filterIcon.isEnabled = true
                        setFilterVisibility()

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }
            })



            deleteFilterIv1.setOnClickListener {
                eventViewModel.setCityFilter(
                    ""
                )
                eventViewModel.setSelectedEventLocation("")
                binding?.filterCv1?.visibility = View.GONE
                eventViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }
            deleteFilterIv2.setOnClickListener {
                eventViewModel.setZipCodeInFilterScreen(
                    ""
                )
                eventViewModel.setIsMyLocationChecked(false)
                binding?.filterCv2?.visibility = View.GONE
                eventViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }
            deleteFilterIv3.setOnClickListener {
                if (eventViewModel.isAllSelected) {
                    eventViewModel.setIsAllSelected(false)
                } else if (eventViewModel.isFreeSelected) {
                    eventViewModel.setIsFreeSelected(false)
                } else if (eventViewModel.isPaidSelected) {
                    eventViewModel.setIsPaidSelected(false)
                }
                eventViewModel.setClickedOnFilter(true)
                filterCv3.visibility = View.GONE
                onNoOfSelectedFilterItem(--noOfSelection)
            }
            deleteFilterIv4.setOnClickListener {
                postEventViewModel.setSelectedEventCategory(
                    EventCategoryResponseItem(
                        false,
                        0,
                        0,
                        ""
                    )
                )
                eventViewModel.setCategoryFilter("")
                filterCv4.visibility = View.GONE
                eventViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }
        }

        eventViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilterClicked ->
            if (isFilterClicked) {
                eventViewModel.getAllEvent(
                    AllEventRequest(
                        category = eventViewModel.categoryFilter.ifEmpty { "" },
                        city = eventViewModel.cityFilter.ifEmpty { "" },
                        from = "",
                        isPaid = "",
                        keyword = eventViewModel.searchQueryFilter.ifEmpty { "" },
                        maxEntryFee = 0,
                        minEntryFee = 0,
                        myEventsOnly = false,
                        userId = "",
                        zip = eventViewModel.zipCodeInFilterScreen.ifEmpty { "" }
                    )
                )

                eventViewModel.setClickedOnFilter(false)
                noOfSelection = 0
            }
            setFilterVisibility()
        }

        eventViewModel.clearAllFilter.observe(viewLifecycleOwner) { isClearAll ->
            if (isClearAll) {
                noOfSelection = 0
                onNoOfSelectedFilterItem(noOfSelection)
            }
        }

        postEventViewModel.eventCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    /*categoryBinding?.progressBar?.visibility = View.VISIBLE*/
                }
                is Resource.Success -> {
                    response.data?.let { EventStaticData.updateEventCategory(it) }
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        postEventViewModel.sendDataToEventDetailsScreen.observe(viewLifecycleOwner) {
            if (postEventViewModel.navigateToEventDetailScreen) {
                val action =
                    EventScreenFragmentDirections.actionEventScreenFragmentToEventDetailsScreenFragment(
                        it
                    )
                findNavController().navigate(action)
                postEventViewModel.setNavigateToEventDetailScreen(
                    value = false
                )
            }
        }

        eventViewModel.clearAllFilter.observe(viewLifecycleOwner) { clearAllFilter ->
            if (clearAllFilter) {
                eventViewModel.setCategoryFilter("")
                eventViewModel.setSelectedEventLocation("")
                eventViewModel.setIsMyLocationChecked(false)
                eventViewModel.setZipCodeInFilterScreen("")
                eventViewModel.setSearchQuery("")

                eventViewModel.setIsAllSelected(false)
                eventViewModel.setIsFreeSelected(false)
                eventViewModel.setIsPaidSelected(false)

                postEventViewModel.setSelectedEventCategory(
                    EventCategoryResponseItem(
                        false,
                        0,
                        0,
                        ""
                    )
                )
                eventViewModel.selectedEventCity.postValue(null)
                eventViewModel.setCityFilter("")
                //eventViewModel.setEventCityList(mutableListOf())
            }

            eventViewModel.setClearAllFilter(false)
        }


        eventViewModel.navigateFromEventScreenToAdvertiseWebView.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    EventScreenFragmentDirections.actionEventScreenFragmentToAdvertiseWebviewFragment(
                        eventViewModel.eventAdvertiseUrl
                    )
                findNavController().navigate(action)
                eventViewModel.setNavigateFromEventScreenToAdvertiseWebView(false)
            }
        }

        return binding?.root
    }

    private fun callEventApi(searchQuery: String = "") {
        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        eventViewModel.getAllEvent(
            AllEventRequest(
                category = "",
                city = "",
                from = "",
                isPaid = "",
                keyword = searchQuery.ifEmpty { "" },
                maxEntryFee = 0,
                minEntryFee = 0,
                myEventsOnly = false,
                userId = if (email?.isNotEmpty() == true) email else "",
                zip = ""
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        dashboardCommonViewModel.setIsFilterApplied("callEventApi")
        context?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(
                EventConstants.SEARCH_KEYWORD_FILTER,
                ""
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setFilterVisibility() {
        noOfSelection = 0
        if (eventViewModel.zipCodeInFilterScreen.isNotEmpty() || eventViewModel.cityFilter.isNotEmpty() || eventViewModel.isFreeSelected || eventViewModel.isPaidSelected || eventViewModel.isAllSelected || eventViewModel.categoryFilter.isNotEmpty()) {
            binding?.selectedFilters?.visibility = View.VISIBLE

            if (eventViewModel.categoryFilter.isNotEmpty()) {
                binding?.filterCv4?.visibility = View.VISIBLE
                binding?.filterText4?.text =
                    "Category: ${eventViewModel.categoryFilter}"
                noOfSelection++
            } else {
                binding?.filterCv4?.visibility = View.GONE
            }

            if (eventViewModel.cityFilter.isNotEmpty()) {
                binding?.filterCv1?.visibility = View.VISIBLE
                binding?.filterText1?.text =
                    "Location: ${eventViewModel.cityFilter}"
                noOfSelection++
            } else {
                binding?.filterCv1?.visibility = View.GONE
            }

            if (eventViewModel.zipCodeInFilterScreen.isNotEmpty()) {
                binding?.filterCv2?.visibility = View.VISIBLE
                binding?.filterText2?.text =
                    "ZipCode: ${eventViewModel.zipCodeInFilterScreen}"
                noOfSelection++

            } else {
                binding?.filterCv2?.visibility = View.GONE
            }
            if (eventViewModel.isAllSelected) {
                binding?.filterCv3?.visibility = View.VISIBLE
                binding?.filterText3?.text = "Fee: All"
                noOfSelection++
            } else if (eventViewModel.isFreeSelected) {
                binding?.filterCv3?.visibility = View.VISIBLE
                binding?.filterText3?.text = "Fee: Free"
                noOfSelection++
            } else if (eventViewModel.isPaidSelected) {
                binding?.filterCv3?.visibility = View.VISIBLE
                binding?.filterText3?.text = "Fee: Paid"
                noOfSelection++
            } else {
                binding?.filterCv3?.visibility = View.GONE
            }


            onNoOfSelectedFilterItem(noOfSelection)

        } else {
            binding?.selectedFilters?.visibility = View.GONE
        }
        if (eventViewModel.zipCodeInFilterScreen.isNotEmpty() && eventViewModel.cityFilter.isNotEmpty()) {
            binding?.selectedFilters?.visibility = View.VISIBLE
        }
    }

    fun onNoOfSelectedFilterItem(noOfSelection: Int) {
        if (noOfSelection >= 1) {
            binding?.numberOfSelectedFilterCv?.visibility = View.VISIBLE
            binding?.selectedFilters?.visibility = View.VISIBLE
            binding?.numberOfSelectedFilterTv?.setText(noOfSelection.toString())
        } else {
            binding?.selectedFilters?.visibility = View.GONE
            binding?.numberOfSelectedFilterCv?.visibility = View.GONE
            eventViewModel.setZipCodeInFilterScreen("")
            eventViewModel.setCategoryFilter("")
            eventViewModel.setIsAllSelected(false)
            eventViewModel.setIsPaidSelected(false)
            eventViewModel.setSelectedEventCity("")
            eventViewModel.setCategoryFilter("")
            eventViewModel.setSelectedEventLocation("")
            eventViewModel.setSearchQuery("")
            eventViewModel.setCityFilter(
                ""
            )
        }
    }
}