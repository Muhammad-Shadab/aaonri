package com.aaonri.app.ui.dashboard.fragment.immigration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.immigration.ImmigrationStaticData
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigartionScreenFrgamentBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationPagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImmigrationScreenFragment : Fragment() {
    var binding: FragmentImmigartionScreenFrgamentBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()

    private val tabTitles =
        arrayListOf("All Discussions", "My Discussions", "Information center")

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

            immigrationScreenViewPager.isUserInputEnabled = false
            immigrationScreenViewPager.adapter = pagerAdapter

            context?.let { Glide.with(it).load(profile).into(profilePicIv) }

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
        }

        immigrationViewModel.getDiscussionCategory()

        immigrationViewModel.allDiscussionCategoryIsClicked.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationCategoryBottomSheet(
                        true
                    )
                findNavController().navigate(action)
                immigrationViewModel.setOnAllDiscussionCategoryIsClicked(false)
            }
        }

        immigrationViewModel.myDiscussionCategoryIsClicked.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    ImmigrationScreenFragmentDirections.actionImmigrationScreenFragmentToImmigrationCategoryBottomSheet(
                        false
                    )
                findNavController().navigate(action)
                immigrationViewModel.setOnMyDiscussionCategoryIsClicked(false)
            }
        }

        immigrationViewModel.discussionCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    response.data?.get(0)
                        ?.let {
                            immigrationViewModel.setSelectedAllDiscussionCategory(it)
                            immigrationViewModel.setSelectedMyDiscussionScreenCategory(it)
                        }

                    response.data?.let { ImmigrationStaticData.setImmigrationCategoryData(it) }
                }
                is Resource.Error -> {

                }
            }
        }



        return binding?.root
    }
}