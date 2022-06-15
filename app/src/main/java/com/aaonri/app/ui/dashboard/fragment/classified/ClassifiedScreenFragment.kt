package com.aaonri.app.ui.dashboard.fragment.classified

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedPagerAdapter
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentClassifiedScreenBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifiedScreenFragment : Fragment() {
    var classifiedScreenBinding: FragmentClassifiedScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    private val tabTitles = arrayListOf("All Classified", "My Classified", "Favorite Classified")

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
                    //removeTab(1)
                }
            }


        }


        return classifiedScreenBinding?.root
    }

    /*private fun disableTabItemAt(tabLayout: TabLayout?, tabText: String) {
        (tabLayout?.getChildAt(0) as? ViewGroup)?.children?.iterator()?.forEach {
            if((it as TabLayout.TabView).tab?.text == tabText) {
                it.isEnabled = false
                it.alpha = 0.5f
            }
        }
    }

    fun enableTabItemAt(tabLayout: TabLayout?, tabText: String) {
        (tabLayout?.getChildAt(0) as? ViewGroup)?.children?.iterator()?.forEach {
            if((it as TabLayout.TabView).tab?.text == tabText) {
                it.isEnabled = true
                it.alpha = 1f
            }
        }
    }*/
    /*fun removeTab(position: Int) {
        classifiedScreenBinding?.apply {
            if (classifiedScreenTabLayout.tabCount >= 1 && position < classifiedScreenTabLayout.tabCount) {
                classifiedScreenTabLayout.removeTabAt(position)
                classifiedPagerAdapter.removeTabPage(position, tabTitles)
            }
        }

    }*/

}