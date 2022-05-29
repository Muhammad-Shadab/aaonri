package com.aaonri.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.authentication.register.adapter.CommunityItemAdapter
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentCommunityBottomBinding
import com.example.newsapp.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityBottomFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val registrationViewModel: RegistrationViewModel by viewModels()
    val commonViewModel: CommonViewModel by activityViewModels()
    val communityItemAdapter by lazy { CommunityItemAdapter() }
    var communityBottomBinding: FragmentCommunityBottomBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communityBottomBinding = FragmentCommunityBottomBinding.inflate(inflater, container, false)
        registrationViewModel.getCommunities()


        lifecycleScope.launchWhenCreated {
            registrationViewModel.communities.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        communityBottomBinding?.progressBarCommunityBottom?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        communityBottomBinding?.progressBarCommunityBottom?.visibility = View.GONE
                        response.data?.community?.let { communityItemAdapter.setData(it) }
                    }
                    else -> {}
                }
            }
        }

        communityBottomBinding?.apply {

            closeCommunityBts.setOnClickListener {
                dismiss()
            }

            communitySubmitBtn.setOnClickListener {
                findNavController().navigate(R.id.action_communityBottomFragment_to_locationDetailsFragment2)
            }

            rvBottomFragment.layoutManager = FlexboxLayoutManager(context)
            rvBottomFragment.adapter = communityItemAdapter
        }

        return communityBottomBinding?.root
    }
}