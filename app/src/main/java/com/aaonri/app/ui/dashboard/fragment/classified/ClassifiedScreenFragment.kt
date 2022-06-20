package com.aaonri.app.ui.dashboard.fragment.classified

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
    var filterAdapter: FilterAdapter? = null
    var deleteElement = ""

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedScreenBinding =
            FragmentClassifiedScreenBinding.inflate(inflater, container, false)

        filterAdapter = FilterAdapter { element ->
            deleteElement = element
            selectedFilterDataObserver()
        }

        val fragment = this
        val classifiedPagerAdapter = ClassifiedPagerAdapter(fragment)

        classifiedScreenBinding?.apply {

            classifiedScreenViewPager.isUserInputEnabled = false

            filterClassified.setOnClickListener {
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
            selectedFilters.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            selectedFilters.adapter = filterAdapter

        }

        selectedFilterDataObserver()

        postClassifiedViewModel.sendDataToClassifiedDetailsScreen.observe(viewLifecycleOwner) {
            if (postClassifiedViewModel.navigateToClassifiedDetail) {
                findNavController().navigate(R.id.action_classifiedScreenFragment_to_classifiedDetailsFragment)
                postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(false)
            }
        }

        return classifiedScreenBinding?.root
    }

    private fun selectedFilterDataObserver() {
        postClassifiedViewModel.filterSelectedDataList.observe(viewLifecycleOwner) { selectedFilter ->

            if (selectedFilter.contains(deleteElement)){
                selectedFilter.remove(deleteElement)
                deleteElement = ""
            }

            filterAdapter?.setData(selectedFilter)
            if (selectedFilter.isNotEmpty()) {
                classifiedScreenBinding?.numberOfSelectedFilterCv?.visibility = View.VISIBLE
                classifiedScreenBinding?.numberOfSelectedFilterTv?.text =
                    selectedFilter.size.toString()
            } else {
                classifiedScreenBinding?.numberOfSelectedFilterCv?.visibility = View.GONE
            }
        }
    }


}