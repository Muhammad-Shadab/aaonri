package com.aaonri.app.ui.dashboard.fragment.classified

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
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedPagerAdapter
import com.aaonri.app.data.classified.ClassifiedStaticData
import com.aaonri.app.data.classified.model.ClassifiedFilterModel
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
    var filterModel: ClassifiedFilterModel? = null

    var addId = 0
    var isUserLogin: Boolean? = null
    private val tabTitles =
        arrayListOf("All Classifieds", "My Classifieds", "Favorites")
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
                    classifiedScreenTabLayout.getTabAt(0)?.select()
                    postClassifiedViewModel.setSearchQuery(textView.text.toString())
                    postClassifiedViewModel.setSelectedClassifiedCategory("")
                    postClassifiedViewModel.setSelectedSubClassifiedCategory("")
                    postClassifiedViewModel.classifiedFilterModel.postValue(
                        ClassifiedFilterModel(
                            selectedCategory = "",
                            selectedSubCategory = "",
                            minPriceRange = "",
                            maxPriceRange = "",
                            zipCode = "",
                            zipCodeCheckBox = false,
                            isDatePublishedSelected = false,
                            isPriceHighToLowSelected = false,
                            isPriceLowToHighSelected = false
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
                postClassifiedViewModel.setSearchQuery("")
                postClassifiedViewModel.classifiedFilterModel.postValue(
                    ClassifiedFilterModel(
                        selectedCategory = "",
                        selectedSubCategory = "",
                        minPriceRange = "",
                        maxPriceRange = "",
                        zipCode = "",
                        zipCodeCheckBox = false,
                        isDatePublishedSelected = false,
                        isPriceHighToLowSelected = false,
                        isPriceLowToHighSelected = false
                    )
                )
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
                    postClassifiedViewModel.setSearchQuery(searchView.text.toString())
                    postClassifiedViewModel.classifiedFilterModel.postValue(
                        ClassifiedFilterModel(
                            selectedCategory = "",
                            selectedSubCategory = "",
                            minPriceRange = "",
                            maxPriceRange = "",
                            zipCode = "",
                            zipCodeCheckBox = false,
                            isDatePublishedSelected = false,
                            isPriceHighToLowSelected = false,
                            isPriceLowToHighSelected = false
                        )
                    )
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            }

            deleteCategoryFilterIv.setOnClickListener {
                binding?.categoryFilterCv?.visibility = View.GONE
                postClassifiedViewModel.setSelectedClassifiedCategory("")
                postClassifiedViewModel.classifiedFilterModel.postValue(
                    ClassifiedFilterModel(
                        selectedCategory = "",
                        selectedSubCategory = "${filterModel?.selectedSubCategory}",
                        minPriceRange = "${filterModel?.minPriceRange}",
                        maxPriceRange = "${filterModel?.maxPriceRange}",
                        zipCode = "${filterModel?.zipCode}",
                        zipCodeCheckBox = filterModel?.zipCodeCheckBox!!,
                        isDatePublishedSelected = filterModel?.isDatePublishedSelected!!,
                        isPriceHighToLowSelected = filterModel?.isPriceHighToLowSelected!!,
                        isPriceLowToHighSelected = filterModel?.isPriceLowToHighSelected!!
                    )
                )
            }

            deleteSubCategoryFilterIv.setOnClickListener {
                binding?.subCategoryFilterCv?.visibility = View.GONE
                postClassifiedViewModel.setSelectedSubClassifiedCategory("")
                postClassifiedViewModel.classifiedFilterModel.postValue(
                    ClassifiedFilterModel(
                        selectedCategory = "${filterModel?.selectedCategory}",
                        selectedSubCategory = "",
                        minPriceRange = "${filterModel?.minPriceRange}",
                        maxPriceRange = "${filterModel?.maxPriceRange}",
                        zipCode = "${filterModel?.zipCode}",
                        zipCodeCheckBox = filterModel?.zipCodeCheckBox!!,
                        isDatePublishedSelected = filterModel?.isDatePublishedSelected!!,
                        isPriceHighToLowSelected = filterModel?.isPriceHighToLowSelected!!,
                        isPriceLowToHighSelected = filterModel?.isPriceLowToHighSelected!!
                    )
                )
            }

            deletePriceRangeIv.setOnClickListener {
                binding?.priceRangeCv?.visibility = View.GONE
                postClassifiedViewModel.classifiedFilterModel.postValue(
                    ClassifiedFilterModel(
                        selectedCategory = "${filterModel?.selectedCategory}",
                        selectedSubCategory = "${filterModel?.selectedSubCategory}",
                        minPriceRange = "",
                        maxPriceRange = "",
                        zipCode = "${filterModel?.zipCode}",
                        zipCodeCheckBox = filterModel?.zipCodeCheckBox!!,
                        isDatePublishedSelected = filterModel?.isDatePublishedSelected!!,
                        isPriceHighToLowSelected = filterModel?.isPriceHighToLowSelected!!,
                        isPriceLowToHighSelected = filterModel?.isPriceLowToHighSelected!!
                    )
                )
            }

            deleteZipCodeIv.setOnClickListener {
                binding?.zipCodeCv?.visibility = View.GONE
                postClassifiedViewModel.classifiedFilterModel.postValue(
                    ClassifiedFilterModel(
                        selectedCategory = "${filterModel?.selectedCategory}",
                        selectedSubCategory = "${filterModel?.selectedSubCategory}",
                        minPriceRange = "${filterModel?.minPriceRange}",
                        maxPriceRange = "${filterModel?.maxPriceRange}",
                        zipCode = "",
                        zipCodeCheckBox = filterModel?.zipCodeCheckBox!!,
                        isDatePublishedSelected = filterModel?.isDatePublishedSelected!!,
                        isPriceHighToLowSelected = filterModel?.isPriceHighToLowSelected!!,
                        isPriceLowToHighSelected = filterModel?.isPriceLowToHighSelected!!
                    )
                )
            }

            deleteChangeSortFilterIv.setOnClickListener {
                binding?.changeSortFilterCv?.visibility = View.GONE
                postClassifiedViewModel.classifiedFilterModel.postValue(
                    ClassifiedFilterModel(
                        selectedCategory = "${filterModel?.selectedCategory}",
                        selectedSubCategory = "${filterModel?.selectedSubCategory}",
                        minPriceRange = "${filterModel?.minPriceRange}",
                        maxPriceRange = "${filterModel?.maxPriceRange}",
                        zipCode = "${filterModel?.zipCode}",
                        zipCodeCheckBox = filterModel?.zipCodeCheckBox!!,
                        isDatePublishedSelected = false,
                        isPriceHighToLowSelected = false,
                        isPriceLowToHighSelected = false
                    )
                )
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
                    }
                    if (tab?.position != 0) {
                        filterClassified.isEnabled = false
                        filterClassified.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.darkGrayColor
                            )
                        )
                        searchView.setText("")
                        postClassifiedViewModel.setSearchQuery("")
                        postClassifiedViewModel.classifiedFilterModel.postValue(
                            ClassifiedFilterModel(
                                selectedCategory = "",
                                selectedSubCategory = "",
                                minPriceRange = "",
                                maxPriceRange = "",
                                zipCode = "",
                                zipCodeCheckBox = false,
                                isDatePublishedSelected = false,
                                isPriceHighToLowSelected = false,
                                isPriceLowToHighSelected = false
                            )
                        )
                        //postClassifiedViewModel.setClearAllFilter(true)
                        if (searchView.text.isNotEmpty()) {
                            searchView.setText("")
                            postClassifiedViewModel.setSearchQuery("")
                            postClassifiedViewModel.classifiedFilterModel.postValue(
                                ClassifiedFilterModel(
                                    selectedCategory = "",
                                    selectedSubCategory = "",
                                    minPriceRange = "",
                                    maxPriceRange = "",
                                    zipCode = "",
                                    zipCodeCheckBox = false,
                                    isDatePublishedSelected = false,
                                    isPriceHighToLowSelected = false,
                                    isPriceLowToHighSelected = false
                                )
                            )
                            //postClassifiedViewModel.setClickOnClearAllFilterBtn(true)
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

        /*classifiedViewModel.searchQueryFromHomeScreen.observe(viewLifecycleOwner) {
            if (it != null) {
                callGetAllClassifiedApi(searchQuery = it)
                binding?.searchView?.setText(it)
                classifiedViewModel.searchQueryFromHomeScreen.postValue(null)
            }
        }*/


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
            }
        }

        postClassifiedViewModel.classifiedFilterModel.observe(viewLifecycleOwner) { filterValue ->
            filterModel = filterValue
            noOfSelection = 0

            if (filterValue.selectedCategory.isNotEmpty()) {
                noOfSelection++
                binding?.categoryFilterTv?.text = filterValue.selectedCategory
                binding?.categoryFilterCv?.visibility = View.VISIBLE
            }

            if (filterValue.selectedSubCategory.isNotEmpty()) {
                noOfSelection++
                binding?.subCategoryFilterTv?.text = filterValue.selectedSubCategory
                binding?.subCategoryFilterCv?.visibility = View.VISIBLE
            }

            if (filterValue.minPriceRange.isNotEmpty() && filterValue.maxPriceRange.isNotEmpty()) {
                noOfSelection++
                binding?.priceRangeTv?.text =
                    "Range: \$${filterValue.minPriceRange} - \$${filterValue.maxPriceRange}"
                binding?.priceRangeCv?.visibility = View.VISIBLE
            }

            if (filterValue.zipCode.isNotEmpty()) {
                noOfSelection++
                binding?.zipCodeTv?.text = "ZipCode: ${filterValue.zipCode}"
                binding?.zipCodeCv?.visibility = View.VISIBLE
            }

            if (filterValue.isDatePublishedSelected) {
                noOfSelection++
                binding?.changeSortFilterTv?.text = "Sort: Date Published"
                binding?.changeSortFilterCv?.visibility = View.VISIBLE
            }

            if (filterValue.isPriceLowToHighSelected) {
                noOfSelection++
                binding?.changeSortFilterTv?.text = "Sort: Low to High"
                binding?.changeSortFilterCv?.visibility = View.VISIBLE
            }

            if (filterValue.isPriceHighToLowSelected) {
                noOfSelection++
                binding?.changeSortFilterTv?.text = "Sort: High to Low"
                binding?.changeSortFilterCv?.visibility = View.VISIBLE
            }

            classifiedViewModel.getClassifiedByUser(
                GetClassifiedByUserRequest(
                    category = filterValue.selectedCategory,
                    email = if (email?.isNotEmpty() == true) email else "",
                    fetchCatSubCat = true,
                    keywords = if (postClassifiedViewModel.searchQueryFilter.isNotEmpty()) postClassifiedViewModel.searchQueryFilter else "",
                    location = "",
                    maxPrice = if (filterValue.maxPriceRange.isNotEmpty()) filterValue.maxPriceRange.toInt() else 0,
                    minPrice = if (filterValue.minPriceRange.isNotEmpty()) filterValue.minPriceRange.toInt() else 0,
                    myAdsOnly = false,
                    popularOnAoonri = null,
                    subCategory = filterValue.selectedSubCategory,
                    zipCode = filterValue.zipCode
                )
            )

            onNoOfSelectedFilterItem(noOfSelection)

        }

        /* postClassifiedViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilterClicked ->
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
         }*/

        /*postClassifiedViewModel.clearAllFilterBtn.observe(viewLifecycleOwner) {
            if (it) {
                callGetAllClassifiedApi()
                binding?.searchView?.setText("")
                postClassifiedViewModel.setClickOnClearAllFilterBtn(false)
            }
        }
*/
        /*postClassifiedViewModel.clearAllFilter.observe(viewLifecycleOwner) { clearAllFilter ->
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
        }*/

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

        /*noOfSelection = 0
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
        }*/

    }


    /*private fun callGetAllClassifiedApi(searchQuery: String = "") {
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
    }*/


    fun onNoOfSelectedFilterItem(noOfSelection: Int) {
        if (noOfSelection >= 1) {
            binding?.numberOfSelectedFilterCv?.visibility = View.VISIBLE
            binding?.selectedFilters?.visibility = View.VISIBLE
            binding?.numberOfSelectedFilterTv?.text = noOfSelection.toString()
        } else {
            binding?.selectedFilters?.visibility = View.GONE
            binding?.numberOfSelectedFilterCv?.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}