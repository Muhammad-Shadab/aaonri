package com.aaonri.app.ui.dashboard.fragment.classified

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClassifiedScreenFragment : Fragment() {
    var classifiedScreenBinding: FragmentClassifiedScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    var addId = 0
    private val tabTitles =
        arrayListOf("All Classifieds", "My Classifieds", "My Favorite Classifieds")

    var noOfSelection = 0

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedScreenBinding =
            FragmentClassifiedScreenBinding.inflate(inflater, container, false)

        val fragment = this
        val classifiedPagerAdapter = ClassifiedPagerAdapter(fragment)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.PROFILE_USER, ""] }

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        if (ClassifiedStaticData.getCategoryList().isEmpty()) {
            postClassifiedViewModel.getClassifiedCategory()
        }

        /*if (postClassifiedViewModel.categoryFilter.isNotEmpty() ||
            postClassifiedViewModel.subCategoryFilter.isNotEmpty() ||
                postClassifiedViewModel.minValueInFilterScreen.isNotEmpty() ||
                postClassifiedViewModel.maxValueInFilterScreen.isNotEmpty() ||
                postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty() ||
                ) {
            postClassifiedViewModel.setClickedOnFilter(true)
        }*/

        classifiedScreenBinding?.apply {

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
                    if (searchView.hasFocus()) {
                        if (keyword.toString().isEmpty()) {
                            cancelbutton.visibility = View.GONE
                            searchViewIcon.visibility = View.VISIBLE
                            postClassifiedViewModel.setKeyClassifiedKeyboardListener(true)
                        } else {
                            cancelbutton.visibility = View.VISIBLE
                            searchViewIcon.visibility = View.GONE
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

            context?.let { Glide.with(it).load(profile).into(profilePicIv) }

            searchViewIcon.setOnClickListener {
                if (searchView.text.toString().isNotEmpty()) {
                    classifiedScreenTabLayout.getTabAt(0)?.select()
                    callGetAllClassifiedApi(searchView.text.toString())
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            }

            deleteFilterIv1.setOnClickListener {
                classifiedScreenBinding?.filterCv1?.visibility = View.GONE

                postClassifiedViewModel.setMaxValue("")
                postClassifiedViewModel.setMinValue("")
                postClassifiedViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }

            deleteFilterIv2.setOnClickListener {
                classifiedScreenBinding?.filterCv2?.visibility = View.GONE

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
                classifiedScreenBinding?.filterCv3?.visibility = View.GONE

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
                classifiedScreenBinding?.filterCv4?.visibility = View.GONE
                postClassifiedViewModel.setSubCategoryFilter("")
                postClassifiedViewModel.setClickedOnFilter(true)
                onNoOfSelectedFilterItem(--noOfSelection)
            }

            deleteFilterIv5.setOnClickListener {
                classifiedScreenBinding?.filterCv5?.visibility = View.GONE
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
                val intent = Intent(requireContext(), ClassifiedActivity::class.java)
                //intent.putExtra("updateClassified", false)
                startActivityForResult(intent, 1)
                //startActivity(intent)
            }

            dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
                if (it) {
                    profilePicIv.visibility = View.GONE
                    bellIconIv.visibility = View.GONE
                    floatingActionBtnClassified.visibility = View.GONE
                    classifiedScreenTabLayout.visibility = View.GONE
                    classifiedScreenViewPager.setPadding(0, 40, 0, 0)
                    classifiedScreenViewPager.isUserInputEnabled = false
                }
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
                        classifiedScreenBinding?.floatingActionBtnClassified?.visibility = View.GONE
                        classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                        classifiedScreenBinding?.numberOfSelectedFilterCv?.visibility = View.GONE
                    } else {
                        classifiedScreenBinding?.floatingActionBtnClassified?.visibility =
                            View.VISIBLE
                        classifiedScreenBinding?.searchViewll?.visibility = View.VISIBLE
                        onNoOfSelectedFilterItem(noOfSelection)
                    }
                    if (tab?.position != 0) {
                        filterClassified.isEnabled = false
                        filterClassified.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.graycolor
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
                classifiedScreenBinding?.searchView?.setText("")
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

        return classifiedScreenBinding?.root
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
            classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE

            if (postClassifiedViewModel.minValueInFilterScreen.isNotEmpty()) {
                classifiedScreenBinding?.filterCv1?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText1?.text =
                    "Range: \$${postClassifiedViewModel.minValueInFilterScreen} - \$${postClassifiedViewModel.maxValueInFilterScreen}"
                noOfSelection++

            } else {
                classifiedScreenBinding?.filterCv1?.visibility = View.GONE
            }
            if (postClassifiedViewModel.categoryFilter.isNotEmpty()) {
                classifiedScreenBinding?.filterCv2?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText2?.text =
                    "Category: ${postClassifiedViewModel.categoryFilter}"
                noOfSelection++
            } else {
                classifiedScreenBinding?.filterCv2?.visibility = View.GONE
            }
            if (postClassifiedViewModel.subCategoryFilter.isNotEmpty()) {
                classifiedScreenBinding?.filterCv4?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText4?.text =
                    "Sub Category: ${postClassifiedViewModel.subCategoryFilter}"
                noOfSelection++
            } else {
                classifiedScreenBinding?.filterCv4?.visibility = View.GONE
            }

            if (postClassifiedViewModel.zipCodeInFilterScreen.isNotEmpty()) {
                classifiedScreenBinding?.filterCv3?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText3?.text =
                    "ZipCode: ${postClassifiedViewModel.zipCodeInFilterScreen}"
                noOfSelection++

            } else {
                classifiedScreenBinding?.filterCv3?.visibility = View.GONE
            }

            if (postClassifiedViewModel.changeSortTriplet.first) {
                classifiedScreenBinding?.filterCv5?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText5?.text = "Sort: Date Published"
                noOfSelection++
            } else if (postClassifiedViewModel.changeSortTriplet.second) {
                classifiedScreenBinding?.filterCv5?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText5?.text = "Sort: Low to High"
                noOfSelection++
            } else if (postClassifiedViewModel.changeSortTriplet.third) {
                classifiedScreenBinding?.filterCv5?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText5?.text = "Sort: High to Low"
                noOfSelection++
            } else {
                classifiedScreenBinding?.filterCv5?.visibility = View.GONE
            }

            onNoOfSelectedFilterItem(noOfSelection)

        } else {
            classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
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
            classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
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
            classifiedScreenBinding?.numberOfSelectedFilterCv?.visibility = View.VISIBLE
            classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
            classifiedScreenBinding?.numberOfSelectedFilterTv?.text = noOfSelection.toString()
        } else {
            classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
            classifiedScreenBinding?.numberOfSelectedFilterCv?.visibility = View.GONE
            postClassifiedViewModel.setMaxValue("")
            postClassifiedViewModel.setMinValue("")
            postClassifiedViewModel.setZipCodeInFilterScreen("")
            postClassifiedViewModel.setIsMyLocationChecked(false)
            postClassifiedViewModel.setSearchQuery("")
            postClassifiedViewModel.setCategoryFilter("")
            postClassifiedViewModel.setSubCategoryFilter("")
        }
    }


}