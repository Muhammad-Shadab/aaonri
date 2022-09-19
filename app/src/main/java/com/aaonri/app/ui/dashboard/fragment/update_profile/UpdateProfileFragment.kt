package com.aaonri.app.ui.dashboard.fragment.update_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentUpdateProfileBinding
import com.aaonri.app.ui.dashboard.fragment.update_profile.adapter.UpdateProfilePagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {
    var binding: FragmentUpdateProfileBinding? = null
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()

    private val tabTitles =
        arrayListOf("Personal", "Address", "Origin", "Interest")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater, container, false)

        val fragment = this
        val updateProfilePagerAdapter = UpdateProfilePagerAdapter(fragment)
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        binding?.apply {
            updateProfileViewPager.isUserInputEnabled = false

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            updateProfileViewPager.adapter = updateProfilePagerAdapter
            TabLayoutMediator(
                updateProfileTabLayout,
                updateProfileViewPager
            ) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                updateProfileTabLayout.getTabAt(i)?.customView =
                    textView
            }
        }

        email?.let { authCommonViewModel.findByEmail(it) }

        authCommonViewModel.setIsUpdateProfile(true)

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        authCommonViewModel.setIsUpdateProfile(false)
    }

}