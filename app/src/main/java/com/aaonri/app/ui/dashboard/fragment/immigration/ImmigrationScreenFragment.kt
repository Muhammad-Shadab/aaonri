package com.aaonri.app.ui.dashboard.fragment.immigration

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.immigration.ImmigrationStaticData
import com.aaonri.app.data.immigration.model.ImmigrationFilterModel
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigartionScreenFrgamentBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationPagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImmigrationScreenFragment : Fragment() {
    var binding: FragmentImmigartionScreenFrgamentBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationFilterModel: ImmigrationFilterModel? = null

    private val tabTitles =
        arrayListOf("All Discussions", "My Discussions", "Information center")

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImmigartionScreenFrgamentBinding.inflate(layoutInflater, container, false)
        val pagerAdapter = ImmigrationPagerAdapter(this)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.PROFILE_USER, ""] }

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        binding?.apply {

            context?.let { Glide.with(it).load(profile).into(profilePicIv) }
            immigrationScreenViewPager.isUserInputEnabled = false
            immigrationScreenViewPager.adapter = pagerAdapter

            filterImmigration.setOnClickListener {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationFilterFragment()
                findNavController().navigate(action)
            }

            floatingActionBtnImmigration.setOnClickListener {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToPostImmigrationFragment()
                findNavController().navigate(action)
            }

            deleteDateRangeFilter.setOnClickListener {
                binding?.dateRangeCv?.visibility = View.GONE
                immigrationViewModel.setFilterData(
                    ImmigrationFilterModel(
                        startDate = null,
                        endDate = null,
                        activeDiscussion = immigrationFilterModel!!.activeDiscussion,
                        atLeastOnDiscussion = immigrationFilterModel!!.atLeastOnDiscussion
                    )
                )
            }

            deleteActiveDiscussionFilterIv.setOnClickListener {
                binding?.activeDiscussionFilterCv?.visibility = View.GONE
                immigrationViewModel.setFilterData(
                    ImmigrationFilterModel(
                        startDate = null,
                        endDate = null,
                        activeDiscussion = false,
                        atLeastOnDiscussion = immigrationFilterModel!!.atLeastOnDiscussion
                    )
                )
            }

            deleteAtLeastOneResponseFilterIv.setOnClickListener {
                binding?.atLeastOneResponseFilterCv?.visibility = View.GONE
                immigrationViewModel.setFilterData(
                    ImmigrationFilterModel(
                        startDate = null,
                        endDate = null,
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
                    if (searchView.hasFocus()) {
                        if (keyword.toString().isEmpty()) {
                            cancelbutton.visibility = View.GONE
                            searchViewIcon.visibility = View.VISIBLE
                            immigrationViewModel.setKeyClassifiedKeyboardListener(true)
                        } else {
                            cancelbutton.visibility = View.VISIBLE
                            searchViewIcon.visibility = View.GONE
                            immigrationViewModel.setKeyClassifiedKeyboardListener(false)
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
                    if (tab?.position == 2) {
                        //floatingActionBtnImmigration.visibility = View.GONE
                        selectedFilters.visibility = View.GONE
                        numberOfSelectedFilterCv.visibility = View.GONE
                    } else {
                        //binding?.floatingActionBtnImmigration?.visibility = View.VISIBLE
                        //binding?.searchViewll?.visibility = View.VISIBLE
                    }

                    if (tab?.position != 0) {
                        filterImmigration.isEnabled = false
                        filterImmigration.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.graycolor
                            )
                        )
                        SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                    } else {
                        filterImmigration.setColorFilter(
                            ContextCompat.getColor(
                                context!!,
                                R.color.white
                            )
                        )
                        filterImmigration.isEnabled = true
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
                        true
                    )
                findNavController().navigate(action)
                immigrationViewModel.setNavigateFromAllImmigrationToDetailScreen(false)
            }
        }


        immigrationViewModel.navigateFromImmigrationCenterToCenterDetailScreen.observe(viewLifecycleOwner) {
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
                        false
                    )
                findNavController().navigate(action)
                immigrationViewModel.setNavigateFromMyImmigrationToDetailScreen(false)
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
            immigrationFilterModel = filterData

            if (filterData.startDate?.isNotEmpty() == true || filterData.endDate?.isNotEmpty() == true || filterData.activeDiscussion || filterData.atLeastOnDiscussion) {
                binding?.selectedFilters?.visibility = View.VISIBLE
            } else {
                binding?.selectedFilters?.visibility = View.GONE
            }

            if (filterData.startDate?.isNotEmpty() == true && filterData.endDate?.isNotEmpty() == true) {
                binding?.dateRangeCv?.visibility = View.VISIBLE
                binding?.dateRangeTv?.text =
                    "Range: ${filterData.startDate} - ${filterData.endDate}"
            }

            if (filterData.activeDiscussion) {
                binding?.activeDiscussionFilterTv?.text = "Active Discussions"
                binding?.activeDiscussionFilterCv?.visibility = View.VISIBLE
            }

            if (filterData.atLeastOnDiscussion) {
                binding?.atLeastOneResponseFilterTv?.text = "Discussion With at Least One Response"
                binding?.atLeastOneResponseFilterCv?.visibility = View.VISIBLE
            }
        }


        return binding?.root
    }
}