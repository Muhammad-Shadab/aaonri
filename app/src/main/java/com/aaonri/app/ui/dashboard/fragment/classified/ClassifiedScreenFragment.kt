package com.aaonri.app.ui.dashboard.fragment.classified

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.ClassifiedPagerAdapter
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentClassifiedScreenBinding
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClassifiedScreenFragment : Fragment() {
    var classifiedScreenBinding: FragmentClassifiedScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    private val tabTitles =
        arrayListOf("All Classifieds", "My Classifieds", "My Favorite Classifieds")


    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedScreenBinding =
            FragmentClassifiedScreenBinding.inflate(inflater, container, false)

        context?.let { it1 -> PreferenceManager<Boolean>(it1) }
            ?.set(
                ClassifiedConstant.MY_LOCATION_CHECKBOX, false
            )
        context?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(
                ClassifiedConstant.MIN_VALUE_FILTER,
                ""
            )
        context?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(
                ClassifiedConstant.MAX_VALUE_FILTER,
                ""
            )
        context?.let { it1 -> PreferenceManager<String>(it1) }
            ?.set(
                ClassifiedConstant.ZIPCODE_FILTER,
                ""
            )

        classifiedScreenBinding?.apply {
            deleteFilterIv1.setOnClickListener {
                classifiedScreenBinding?.filterCv1?.visibility = View.GONE
                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(
                        ClassifiedConstant.MIN_VALUE_FILTER, ""
                    )
                postClassifiedViewModel.setClickedOnFilter(false)
            }

            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(
                            ClassifiedConstant.SEARCH_KEYWORD_FILTER, textView.text.toString()
                        )
                    setClassifiedViewPager(true)
                }
                false
            }

            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (keyword.toString().isEmpty()) {
                        setClassifiedViewPager(false)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
            deleteFilterIv2.setOnClickListener {
                classifiedScreenBinding?.filterCv2?.visibility = View.GONE
                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(
                        ClassifiedConstant.MAX_VALUE_FILTER, ""
                    )
                postClassifiedViewModel.setClickedOnFilter(false)
            }

            deleteFilterIv3.setOnClickListener {
                classifiedScreenBinding?.filterCv3?.visibility = View.GONE
                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(
                        ClassifiedConstant.ZIPCODE_FILTER,
                        ""
                    )
                postClassifiedViewModel.setClickedOnFilter(false)
            }

            filterClassified.setOnClickListener {
                findNavController().navigate(R.id.action_classifiedScreenFragment_to_classifiedFilterFragmentBottom)
            }

            moreTextView.setOnClickListener {
                findNavController().navigate(R.id.action_classifiedScreenFragment_to_classifiedFilterFragmentBottom)
            }

            floatingActionBtnClassified.setOnClickListener {
                val intent = Intent(requireContext(), ClassifiedActivity::class.java)
                startActivity(intent)
            }

            dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
                if (it) {
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
        }

        setClassifiedViewPager(false)
        postClassifiedViewModel.clickedOnFilter.observe(viewLifecycleOwner) { isFilerBtnClicked ->

            /*val minMaxValue =
                    context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MIN_MAX_FILTER, ""] }*/
            val minValue =
                context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MIN_VALUE_FILTER, ""] }
            val maxValue =
                context?.let { PreferenceManager<String>(it)[ClassifiedConstant.MAX_VALUE_FILTER, ""] }
            val zipCodeValue =
                context?.let { PreferenceManager<String>(it)[ClassifiedConstant.ZIPCODE_FILTER, ""] }

            if (isFilerBtnClicked) {
                if (minValue?.isNotEmpty() == true || maxValue?.isNotEmpty() == true || zipCodeValue?.isNotEmpty() == true) {
                    classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
                    classifiedScreenBinding?.moreTextView?.visibility = View.VISIBLE

                    if (minValue?.isNotEmpty() == true) {
                        classifiedScreenBinding?.filterCv1?.visibility = View.VISIBLE
                        classifiedScreenBinding?.filterText1?.text = "Range: \$$minValue"
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
                    } else {
                        classifiedScreenBinding?.filterCv3?.visibility = View.GONE
                    }

                } else {
                    classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                    classifiedScreenBinding?.moreTextView?.visibility = View.GONE
                }
            }
            if (minValue?.isEmpty() == true && maxValue?.isEmpty() == true && zipCodeValue?.isEmpty() == true) {
                classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                classifiedScreenBinding?.moreTextView?.visibility = View.GONE
            }
            setClassifiedViewPager(true)
        }


        postClassifiedViewModel.sendDataToClassifiedDetailsScreen.observe(viewLifecycleOwner) {
            if (postClassifiedViewModel.navigateToClassifiedDetail) {
                findNavController().navigate(R.id.action_classifiedScreenFragment_to_classifiedDetailsFragment)
                postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(false)
            }
        }

        postClassifiedViewModel.sendFavoriteDataToClassifiedDetails.observe(viewLifecycleOwner) {
            if (postClassifiedViewModel.navigateToClassifiedDetail) {
                findNavController().navigate(R.id.action_classifiedScreenFragment_to_classifiedDetailsFragment)
                postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(false)
            }
        }

        return classifiedScreenBinding?.root
    }


    private fun setClassifiedViewPager(isFilterEnabled: Boolean) {
        val fragment = this
        val classifiedPagerAdapter = ClassifiedPagerAdapter(fragment, isFilterEnabled)

        classifiedScreenBinding?.apply {

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
                        .inflate(R.layout.tab_title_text, null) as TextView
                classifiedScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }

            classifiedScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 2) {
                        classifiedScreenBinding?.floatingActionBtnClassified?.visibility = View.GONE
                        classifiedScreenBinding?.searchView?.visibility = View.GONE
                        classifiedScreenBinding?.filterClassified?.visibility = View.GONE
                    } else {
                        classifiedScreenBinding?.floatingActionBtnClassified?.visibility =
                            View.VISIBLE
                        classifiedScreenBinding?.searchView?.visibility = View.VISIBLE
                        classifiedScreenBinding?.filterClassified?.visibility = View.VISIBLE
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
    }

}