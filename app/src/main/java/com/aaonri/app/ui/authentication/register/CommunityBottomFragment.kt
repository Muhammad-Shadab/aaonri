package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.CommunityItemAdapter
import com.aaonri.app.data.authentication.register.model.community.Community
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
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
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    private var communityItemAdapter: CommunityItemAdapter? = null
    var communityBottomBinding: FragmentCommunityBottomBinding? = null
    var communities = mutableListOf<Community>()
    var selectedCommunitiesSize = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        communityBottomBinding = FragmentCommunityBottomBinding.inflate(inflater, container, false)
        getCommunities()



        communityItemAdapter = CommunityItemAdapter { communitiesList ->
            if (communitiesList.isNotEmpty()) {
                authCommonViewModel.addCommunityList(communitiesList as MutableList<Community>)
                communityBottomBinding?.numberOfSelectedCommunity?.visibility = View.VISIBLE
                communityBottomBinding?.numberOfSelectedCommunity?.text =
                    "You have selected ${communitiesList.size + selectedCommunitiesSize} communities"
            } else {
                authCommonViewModel.addCommunityList(communitiesList as MutableList<Community>)
                communityBottomBinding?.numberOfSelectedCommunity?.visibility = View.GONE
            }
        }

        authCommonViewModel.selectedCommunityList.observe(viewLifecycleOwner) { selectedCommunitiesList ->
            selectedCommunitiesSize = selectedCommunitiesList.size
            communityBottomBinding?.numberOfSelectedCommunity?.visibility = View.VISIBLE
            communityBottomBinding?.numberOfSelectedCommunity?.text =
                "You have selected ${selectedCommunitiesList.size} communities"
            communityItemAdapter?.setDataSavedList(selectedCommunitiesList)
            communityItemAdapter?.savedCommunityList =
                selectedCommunitiesList as MutableList<Community>
            communityItemAdapter?.selectedCommunityList =
                selectedCommunitiesList as MutableList<Community>

        }


        communityBottomBinding?.apply {

            closeCommunityBtn.setOnClickListener {
                dismiss()
            }

            communitySubmitBtn.setOnClickListener {

                findNavController().navigateUp()
            }

            rvBottomFragment.layoutManager = FlexboxLayoutManager(context)
            rvBottomFragment.adapter = communityItemAdapter
        }

        return communityBottomBinding?.root
    }

    private fun getCommunities() {
        authCommonViewModel.getCommunities()
        authCommonViewModel.communitiesList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    response.data?.community?.let { communityItemAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                    Log.i("Loading", "Error: ${response.message}")
                }
                else -> {}
            }
        }
    }

}