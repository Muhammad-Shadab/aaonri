package com.aaonri.app.ui.dashboard.fragment.classified

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedPagerAdapter
import com.aaonri.app.data.classified.adapter.FilterAdapter
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentClassifiedScreenBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClassifiedScreenFragment : Fragment() {
    var classifiedScreenBinding: FragmentClassifiedScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    private val tabTitles = arrayListOf("All Classified", "My Classified", "Favorite Classified")
    var text1 = ""
    var text2 = ""
    var text3 = ""
    var text4 = ""
    var deleteElement = ""

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedScreenBinding =
            FragmentClassifiedScreenBinding.inflate(inflater, container, false)

        val fragment = this
        val classifiedPagerAdapter = ClassifiedPagerAdapter(fragment)

        classifiedScreenBinding?.apply {

            classifiedScreenViewPager.isUserInputEnabled = false

            deleteFilterIv1.setOnClickListener {
                postClassifiedViewModel.setMinMaxValue("", "")
            }

            deleteFilterIv2.setOnClickListener {
                postClassifiedViewModel.setMinValue("")
            }

            deleteFilterIv3.setOnClickListener {

            }
            deleteFilterIv4.setOnClickListener {

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
                    } else {
                        classifiedScreenBinding?.floatingActionBtnClassified?.visibility =
                            View.VISIBLE
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
                    floatingActionBtnClassified.visibility = View.GONE
                    classifiedScreenTabLayout.visibility = View.GONE
                    classifiedScreenViewPager.setPadding(0, 40, 0, 0)
                    classifiedScreenViewPager.isUserInputEnabled = false
                }
            }

            postClassifiedViewModel.navigateToAllClassified.observe(viewLifecycleOwner){
                if (it) {
                    classifiedScreenTabLayout.getTabAt(0)?.select()
                    postClassifiedViewModel.setNavigateToAllClassified(false)
                }
            }

        }
        postClassifiedViewModel.minMaxValueInFilter.observe(viewLifecycleOwner) {
            text1 = it
            if (it.equals("Range: \$-\$")) {
                classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                classifiedScreenBinding?.filterCv1?.visibility = View.GONE
            } else {
                classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterCv1?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText1?.text = it
            }
        }

        postClassifiedViewModel.minValueInFilterScreen.observe(viewLifecycleOwner) {
            text2 = it
            if (it.isNotEmpty()) {
                classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterCv2?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText2?.text = it

            } else {
                classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                classifiedScreenBinding?.filterCv2?.visibility = View.GONE
            }
        }

        postClassifiedViewModel.maxValueInFilterScreen.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
                classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterCv3?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText3?.text = it
            }
        }

        postClassifiedViewModel.zipCodeInFilterScreen.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterCv4?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText4?.text = it
            } else {
                classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                classifiedScreenBinding?.filterCv4?.visibility = View.GONE
            }
        }


        /*postClassifiedViewModel.zipCodeInFilterScreen.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                classifiedScreenBinding?.selectedFilters?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterCv3?.visibility = View.VISIBLE
                classifiedScreenBinding?.filterText3?.text = it

            } else {
                classifiedScreenBinding?.selectedFilters?.visibility = View.GONE
                classifiedScreenBinding?.filterCv3?.visibility = View.GONE
            }
        }*/



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

        /* postClassifiedViewModel.navigateToMyClassified.observe(viewLifecycleOwner) {
             Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
             if (it) {
                 classifiedScreenBinding?.classifiedScreenTabLayout?.getTabAt(1)?.select()
                 postClassifiedViewModel.setNavigateToMyClassified(false)
             }
         }*/

        return classifiedScreenBinding?.root
    }

    /*private fun selectedFilterDataObserver() {
        postClassifiedViewModel.filterSelectedDataList.observe(viewLifecycleOwner) { selectedFilter ->

            if (selectedFilter.contains(deleteElement)) {
                selectedFilter.remove(deleteElement)
                deleteElement = ""
            }

            filterAdapter?.setData(selectedFilter)
            if (selectedFilter.isNotEmpty()) {
                classifiedScreenBinding?.moreTextView?.visibility = View.VISIBLE
                classifiedScreenBinding?.numberOfSelectedFilterCv?.visibility = View.VISIBLE
                classifiedScreenBinding?.numberOfSelectedFilterTv?.text =
                    selectedFilter.size.toString()
            } else {
                classifiedScreenBinding?.moreTextView?.visibility = View.GONE
                classifiedScreenBinding?.numberOfSelectedFilterCv?.visibility = View.GONE
            }
        }
    }*/


}