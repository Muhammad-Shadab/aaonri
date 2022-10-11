package com.aaonri.app.ui.dashboard.fragment.classified

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
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
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedPagerAdapter
import com.aaonri.app.data.classified.ClassifiedStaticData
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponseItem
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentClassifiedScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
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
class ClassifiedScreenFragment : Fragment() {
    var binding: FragmentClassifiedScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    lateinit var mGoogleSignInClient: GoogleSignInClient

    var addId = 0
    var isUserLogin: Boolean? = null
    private val tabTitles =
        arrayListOf("All Classifieds", "My Classifieds", "Fav Classifieds")
    var noOfSelection = 0

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentClassifiedScreenBinding.inflate(inflater, container, false)
        isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }
        val fragment = this
        val classifiedPagerAdapter = ClassifiedPagerAdapter(fragment)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val userName =
            context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        if (ClassifiedStaticData.getCategoryList().isEmpty()) {
            postClassifiedViewModel.getClassifiedCategory()
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
                ClassifiedScreenFragmentDirections.actionClassifiedScreenFragmentToUpdateProfileFragment()
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

        /*MainStaticData.getClassifiedFooterTextOnly().forEach {
            Toast.makeText(
                context,
                it.advertisementPageLocation.locationName,
                Toast.LENGTH_SHORT
            )
                .show()
        }*/


        /*if (postClassifiedViewModel.categoryFilter.isNotEmpty() ||
            postClassifiedViewModel.subCategoryFilter.isNotEmpty() ||
                postClassifiedViewModel.minValueInFilterScreen.isNotEmpty() ||
                postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty() ||
                postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty() ||
                ) {
            postClassifiedViewModel.setClickedOnFilter(true)
        }*/

        binding?.apply {

            profilePicCv.setOnClickListener {
                if (isUserLogin == false) {
                    activity?.finish()
                } else {
                    updateLogoutDialog.show()
                }
            }

            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    callGetAllClassifiedApi(searchQuery = textView.text.toString())
                    classifiedScreenTabLayout.getTabAt(0)?.select()
                    postClassifiedViewModel.setSearchQuery(textView.text.toString())
                    postClassifiedViewModel.setClearAllFilter(true)
                    postClassifiedViewModel.setIsFilterEnable(true)
                    //postClassifiedViewModel.setClickedOnFilter(true)
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
                            postClassifiedViewModel.setKeyClassifiedKeyboardListener(true)
                        } else {
                            postClassifiedViewModel.setKeyClassifiedKeyboardListener(false)
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

            cancelbutton.setOnClickListener {
                cancelbutton.visibility = View.GONE
                searchViewIcon.visibility = View.VISIBLE
                postClassifiedViewModel.setClearAllFilter(true)
                postClassifiedViewModel.setClickOnClearAllFilterBtn(true)
            }

            context?.let {
                Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop().error(R.drawable.profile_pic_placeholder)
                    .into(profilePicIv)
            }

            searchViewIcon.setOnClickListener {
                if (searchView.text.toString().isNotEmpty()) {
                    classifiedScreenTabLayout.getTabAt(0)?.select()
                    callGetAllClassifiedApi(searchView.text.toString())
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            }

            deleteFilterIv1.setOnClickListener {
                binding?.filterCv1?.visibility = View.GONE

                postClassifiedViewModel.setMaxValue("")
                postClassifiedViewModel.setMinValue("")
                postClassifiedViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }

            deleteFilterIv2.setOnClickListener {
                binding?.filterCv2?.visibility = View.GONE

                postClassifiedViewModel.setSelectedClassifiedCategory(
                    ClassifiedCategoryResponseItem(
                        emptyList(),
                        0,
                        0,
                        0,
                        ""
                    )
                )
                postClassifiedViewModel.setCategoryFilter("")
                postClassifiedViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }

            deleteFilterIv3.setOnClickListener {
                binding?.filterCv3?.visibility = View.GONE

                postClassifiedViewModel.setZipCodeInFilterScreen("")
                postClassifiedViewModel.setIsMyLocationChecked(false)

                postClassifiedViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }

            deleteFilterIv4.setOnClickListener {
                postClassifiedViewModel.setSelectedSubClassifiedCategory(
                    ClassifiedSubcategoryX(
                        0,
                        0,
                        0,
                        ""
                    )
                )
                filterCv4.visibility = View.GONE
                postClassifiedViewModel.setSubCategoryFilter("")
                postClassifiedViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }

            deleteFilterIv5.setOnClickListener {
                filterCv5.visibility = View.GONE
                postClassifiedViewModel.setChangeSortTripletFilter(
                    datePublished = false,
                    priceLowToHigh = false,
                    priceHighToLow = false
                )
                postClassifiedViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }

            filterClassified.setOnClickListener {
                findNavController().navigate(R.id.action_classifiedScreenFragment_to_classifiedFilterFragmentBottom)
            }

            /*moreTextView.setOnClickListener {
                findNavController().navigate(R.id.action_classifiedScreenFragment_to_classifiedFilterFragmentBottom)
            }*/

            floatingActionBtnClassified.setOnClickListener {
                if (isUserLogin == true) {
                    val intent = Intent(requireContext(), ClassifiedActivity::class.java)
                    //intent.putExtra("updateClassified", false)
                    startActivityForResult(intent, 1)
                    //startActivity(intent
                } else {
                    guestUserLoginDialog.show()
                }
            }

            if (isUserLogin == false) {
                //bellIconIv.visibility = View.GONE
                classifiedScreenTabLayout.visibility = View.GONE
                classifiedScreenViewPager.setPadding(0, 40, 0, 0)
                classifiedScreenViewPager.isUserInputEnabled = false
            }

            postClassifiedViewModel.navigateToAllClassified.observe(viewLifecycleOwner) {
                if (it) {
                    classifiedScreenTabLayout.getTabAt(0)?.select()
                    postClassifiedViewModel.setNavigateToAllClassified(false)
                }
            }

            classifiedScreenViewPager.isUserInputEnabled = false

            classifiedScreenViewPager.adapter = classifiedPagerAdapter
            TabLayoutMediator(
                classifiedScreenTabLayout,
                classifiedScreenViewPager
            ) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                classifiedScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }

            classifiedScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 2) {
                        binding?.floatingActionBtnClassified?.visibility = View.GONE
                        binding?.selectedFilters?.visibility = View.GONE
                        binding?.numberOfSelectedFilterCv?.visibility = View.GONE
                    } else {
                        binding?.floatingActionBtnClassified?.visibility =
                            View.VISIBLE
                        binding?.searchViewll?.visibility = View.VISIBLE
                        onNoOfSelectedFilterItem(noOfSelection)
                    }
                    if (tab?.position != 0) {
                        filterClassified.isEnabled = false
                        filterClassified.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.darkGrayColor
                            )
                        )
                        postClassifiedViewModel.setClearAllFilter(true)
                        if (searchView.text.isNotEmpty()) {
                            searchView.setText("")
                            postClassifiedViewModel.setClickOnClearAllFilterBtn(true)
                        }
                        SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                    } else {
                        filterClassified.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.white
                            )
                        )
                        filterClassified.isEnabled = true
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }
            })

        }
        /*postClassifiedViewModel.clearAllFilter.observe(viewLifecycleOwner) { isClearAll ->
            if (isClearAll) {
                noOfSelection = 0
                onNoOfSelectedFilterItem(noOfSelection)
            }
        }*/


        /*postClassifiedViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilerBtnClicked ->

            if (isFilerBtnClicked) {
                noofSelection = 0
                if (minValue?.isNotEmpty() == true || maxValue?.isNotEmpty() == true || zipCodeValue?.isNotEmpty() == true) {
                    classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
                    // classifiedScreenBinding?.moreTextView?.visibility = View.VISIBLE

                    if (minValue?.isNotEmpty() == true) {
                        classifiedScreenBinding?.filterCv1?.visibility = View.VISIBLE
                        classifiedScreenBinding?.filterText1?.text =
                            "Range: \$$minValue - \$$maxValue"
                        noofSelection++

                    } else {
                        classifiedScreenBinding?.filterCv1?.visibility = View.GONE
                    }

                    if (maxValue?.isNotEmpty() == true) {
                        classifiedScreenBinding?.filterCv2?.visibility = View.VISIBLE
                        classifiedScreenBinding?.filterText2?.text = "Range: \$$maxValue"
                    } else {
                        classifiedScreenBinding?.filterCv2?.visibility = View.GONE
                    }

                    if (zipCodeValue?.isNotEmpty() == true) {
                        classifiedScreenBinding?.filterCv3?.visibility = View.VISIBLE
                        classifiedScreenBinding?.filterText3?.text =
                            "ZipCode: $zipCodeValue"
                        noofSelection++

                    } else {
                        classifiedScreenBinding?.filterCv3?.visibility = View.GONE
                    }

                    OnNoOfSelectedFilterItem(noofSelection)

                } else {
                    classifiedScreenBinding?.selectedFilters?.visibility = View.GONE

                    //classifiedScreenBinding?.moreTextView?.visibility = View.GONE
                }
            }
            if (minValue?.isEmpty() == true && maxValue?.isEmpty() == true && zipCodeValue?.isEmpty() == true) {
                classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                //classifiedScreenBinding?.moreTextView?.visibility = View.GONE
            }
            setClassifiedViewPager(true)
        }*/

        classifiedViewModel.searchQueryFromHomeScreen.observe(viewLifecycleOwner) {
            if (it != null) {
                callGetAllClassifiedApi(searchQuery = it)
                binding?.searchView?.setText(it)
                classifiedViewModel.searchQueryFromHomeScreen.postValue(null)
            }
        }


        postClassifiedViewModel.sendDataToClassifiedDetailsScreen.observe(viewLifecycleOwner) {
            if (postClassifiedViewModel.navigateToClassifiedDetail) {
                if (postClassifiedViewModel.navigateToMyClassifiedScreen) {
                    val action =
                        ClassifiedScreenFragmentDirections.actionClassifiedScreenFragmentToClassifiedDetailsFragment(
                            it,
                            true
                        )
                    findNavController().navigate(action)
                } else {
                    val action =
                        ClassifiedScreenFragmentDirections.actionClassifiedScreenFragmentToClassifiedDetailsFragment(
                            it,
                            false
                        )
                    findNavController().navigate(action)
                }
                postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(
                    value = false,
                    isMyClassifiedScreen = false
                )
            }
        }

        postClassifiedViewModel.classifiedCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    response.data?.let { ClassifiedStaticData.updateCategoryList(it) }
                }
                is Resource.Error -> {
                }
                else -> {

                }
            }
        }

        postClassifiedViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilterClicked ->
            if (isFilterClicked) {
                classifiedViewModel.getClassifiedByUser(
                    GetClassifiedByUserRequest(
                        category = postClassifiedViewModel.categoryFilter.ifEmpty { "" },
                        email = if (email?.isNotEmpty() == true) email else "",
                        fetchCatSubCat = true,
                        keywords = postClassifiedViewModel.searchQueryFilter.ifEmpty { "" },
                        location = "",
                        maxPrice = if (postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty()) postClassifiedViewModel.maxValueInFilterScreen.toInt() else 0,
                        minPrice = if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty()) postClassifiedViewModel.minValueInFilterScreen.toInt() else 0,
                        myAdsOnly = false,
                        popularOnAoonri = null,
                        subCategory = postClassifiedViewModel.subCategoryFilter.ifEmpty { "" },
                        zipCode = postClassifiedViewModel.zipCodeInFilterScreen.ifEmpty { "" }
                    )
                )
                postClassifiedViewModel.setClickedOnFilter(false)
                noOfSelection = 0
            }
            setFilterVisibility()
            /*if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty() || postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty() || postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty()) {
                classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
                // classifiedScreenBinding?.moreTextView?.visibility = View.VISIBLE
                if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty()) {
                    classifiedScreenBinding?.filterCv1?.visibility = View.VISIBLE
                    classifiedScreenBinding?.filterText1?.text =
                        "Range: \$${postClassifiedViewModel.minValueInFilterScreen} - \$${postClassifiedViewModel.maxValueInFilterScreen}"
                    noOfSelection++

                } else {
                    classifiedScreenBinding?.filterCv1?.visibility = View.GONE
                }

                *//*if (postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty()) {
                    classifiedScreenBinding?.filterCv2?.visibility = View.VISIBLE
                    classifiedScreenBinding?.filterText2?.text =
                        "Range: \$${postClassifiedViewModel.maxValueInFilterScreen}"
                } else {
                    classifiedScreenBinding?.filterCv2?.visibility = View.GONE
                }*//*

                if (postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty()) {
                    classifiedScreenBinding?.filterCv3?.visibility = View.VISIBLE
                    classifiedScreenBinding?.filterText3?.text =
                        "ZipCode: ${postClassifiedViewModel.zipCodeInFilterScreen}"
                    noOfSelection++
                } else {
                    classifiedScreenBinding?.filterCv3?.visibility = View.GONE
                }
                onNoOfSelectedFilterItem(noOfSelection)
            } else {
                classifiedScreenBinding?.selectedFilters?.visibility = View.GONE

                //classifiedScreenBinding?.moreTextView?.visibility = View.GONE
            }*/
            /* if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty() && postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty() && postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty()) {
                 classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                 //classifiedScreenBinding?.moreTextView?.visibility = View.GONE
             }*/
        }

        postClassifiedViewModel.clearAllFilterBtn.observe(viewLifecycleOwner) {
            if (it) {
                callGetAllClassifiedApi()
                binding?.searchView?.setText("")
                postClassifiedViewModel.setClickOnClearAllFilterBtn(false)
            }
        }

        postClassifiedViewModel.clearAllFilter.observe(viewLifecycleOwner) { clearAllFilter ->
            if (clearAllFilter) {
                postClassifiedViewModel.setMinValue("")
                postClassifiedViewModel.setMaxValue("")
                postClassifiedViewModel.setIsMyLocationChecked(false)
                postClassifiedViewModel.setZipCodeInFilterScreen("")
                postClassifiedViewModel.setClickedOnFilter(false)
                postClassifiedViewModel.setCategoryFilter("")
                postClassifiedViewModel.setSubCategoryFilter("")

                postClassifiedViewModel.setChangeSortTripletFilter(
                    datePublished = false,
                    priceLowToHigh = false,
                    priceHighToLow = false
                )

                postClassifiedViewModel.setSelectedClassifiedCategory(
                    ClassifiedCategoryResponseItem(
                        emptyList(),
                        0,
                        0,
                        0,
                        ""
                    )
                )

                postClassifiedViewModel.setSelectedSubClassifiedCategory(
                    ClassifiedSubcategoryX(
                        0,
                        0,
                        0,
                        ""
                    )
                )
                postClassifiedViewModel.clickedOnFilter.postValue(true)
            }
            postClassifiedViewModel.setClearAllFilter(false)
        }

        classifiedViewModel.navigateFromClassifiedScreenToAdvertiseWebView.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                val action =
                    ClassifiedScreenFragmentDirections.actionClassifiedScreenFragmentToAdvertiseWebviewFragment(
                        classifiedViewModel.classifiedAdvertiseUrl
                    )
                findNavController().navigate(action)
                classifiedViewModel.setNavigateFromClassifiedScreenToAdvertiseWebView(false)
            }
        }


        return binding?.root
    }

    private fun setFilterVisibility() {

        noOfSelection = 0
        if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty() ||
            postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty() ||
            postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty() ||
            postClassifiedViewModel.categoryFilter.isNotEmpty() ||
            postClassifiedViewModel.subCategoryFilter.isNotEmpty() ||
            postClassifiedViewModel.changeSortTriplet.first ||
            postClassifiedViewModel.changeSortTriplet.second ||
            postClassifiedViewModel.changeSortTriplet.third
        ) {
            binding?.selectedFilters?.visibility = View.VISIBLE

            if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty()) {
                binding?.filterCv1?.visibility = View.VISIBLE
                binding?.filterText1?.text =
                    "Range: \$${postClassifiedViewModel.minValueInFilterScreen} - \$${postClassifiedViewModel.maxValueInFilterScreen}"
                noOfSelection++

            } else {
                binding?.filterCv1?.visibility = View.GONE
            }
            if (postClassifiedViewModel.categoryFilter.isNotEmpty()) {
                binding?.filterCv2?.visibility = View.VISIBLE
                binding?.filterText2?.text =
                    "Category: ${postClassifiedViewModel.categoryFilter}"
                noOfSelection++
            } else {
                binding?.filterCv2?.visibility = View.GONE
            }
            if (postClassifiedViewModel.subCategoryFilter.isNotEmpty()) {
                binding?.filterCv4?.visibility = View.VISIBLE
                binding?.filterText4?.text =
                    "Sub Category: ${postClassifiedViewModel.subCategoryFilter}"
                noOfSelection++
            } else {
                binding?.filterCv4?.visibility = View.GONE
            }

            if (postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty()) {
                binding?.filterCv3?.visibility = View.VISIBLE
                binding?.filterText3?.text =
                    "ZipCode: ${postClassifiedViewModel.zipCodeInFilterScreen}"
                noOfSelection++

            } else {
                binding?.filterCv3?.visibility = View.GONE
            }

            if (postClassifiedViewModel.changeSortTriplet.first) {
                binding?.filterCv5?.visibility = View.VISIBLE
                binding?.filterText5?.text = "Sort: Date Published"
                noOfSelection++
            } else if (postClassifiedViewModel.changeSortTriplet.second) {
                binding?.filterCv5?.visibility = View.VISIBLE
                binding?.filterText5?.text = "Sort: Low to High"
                noOfSelection++
            } else if (postClassifiedViewModel.changeSortTriplet.third) {
                binding?.filterCv5?.visibility = View.VISIBLE
                binding?.filterText5?.text = "Sort: High to Low"
                noOfSelection++
            } else {
                binding?.filterCv5?.visibility = View.GONE
            }

            onNoOfSelectedFilterItem(noOfSelection)

        } else {
            binding?.selectedFilters?.visibility = View.GONE
            //classifiedScreenBinding?.moreTextView?.visibility = View.GONE
        }
        if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty() &&
            postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty() &&
            postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty() &&
            postClassifiedViewModel.categoryFilter.isNotEmpty() &&
            postClassifiedViewModel.subCategoryFilter.isNotEmpty() &&
            postClassifiedViewModel.changeSortTriplet.first &&
            postClassifiedViewModel.changeSortTriplet.second &&
            postClassifiedViewModel.changeSortTriplet.third
        ) {
            binding?.selectedFilters?.visibility = View.VISIBLE
            //classifiedScreenBinding?.moreTextView?.visibility = View.GONE
        }
    }


    private fun callGetAllClassifiedApi(searchQuery: String = "") {
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        classifiedViewModel.getClassifiedByUser(
            GetClassifiedByUserRequest(
                category = "",
                email = if (email?.isNotEmpty() == true) email else "",
                fetchCatSubCat = true,
                keywords = searchQuery,
                location = "",
                maxPrice = 0,
                minPrice = 0,
                myAdsOnly = false,
                popularOnAoonri = null,
                subCategory = "",
                zipCode = ""
            )
        )
    }


    fun onNoOfSelectedFilterItem(noOfSelection: Int) {
        if (noOfSelection >= 1) {
            binding?.numberOfSelectedFilterCv?.visibility = View.VISIBLE
            binding?.selectedFilters?.visibility = View.VISIBLE
            binding?.numberOfSelectedFilterTv?.text = noOfSelection.toString()
        } else {
            binding?.selectedFilters?.visibility = View.GONE
            binding?.numberOfSelectedFilterCv?.visibility = View.GONE
            postClassifiedViewModel.setMaxValue("")
            postClassifiedViewModel.setMinValue("")
            postClassifiedViewModel.setZipCodeInFilterScreen("")
            postClassifiedViewModel.setIsMyLocationChecked(false)
            postClassifiedViewModel.setSearchQuery("")
            postClassifiedViewModel.setCategoryFilter("")
            postClassifiedViewModel.setSubCategoryFilter("")
        }
    }

    /*private fun dynamicSetTabLayoutMode(tabLayout: TabLayout) {
        val tabWidth = calculateTabWidth(tabLayout)
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        if (tabWidth <= screenWidth) {
            tabLayout.tabMode = TabLayout.MODE_AUTO
        } else {
            tabLayout.tabMode = TabLayout.MODE_FIXED
        }
    }

    private fun calculateTabWidth(tabLayout: TabLayout): Int {
        var tabWidth = 0
        for (i in 0 until tabLayout.childCount) {
            val view = tabLayout.getChildAt(i)
            view.measure(0, 0)
            tabWidth += view.measuredWidth
        }
        return tabWidth
    }*/

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}