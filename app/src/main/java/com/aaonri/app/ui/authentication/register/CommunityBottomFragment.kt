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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.CommunityItemAdapter
import com.aaonri.app.data.authentication.register.model.community.Community
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
    private var communityItemAdapter: CommunityItemAdapter? = null
    var communityBottomBinding: FragmentCommunityBottomBinding? = null
    var communities = listOf<Community>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communityBottomBinding = FragmentCommunityBottomBinding.inflate(inflater, container, false)
        getCommunities()

        communityItemAdapter = CommunityItemAdapter { communitiesList ->
            if (communitiesList.isNotEmpty()) {
                communities = communitiesList
                communityBottomBinding?.numberOfSelectedCommunity?.visibility = View.VISIBLE
                communityBottomBinding?.numberOfSelectedCommunity?.text =
                    "You have selected ${communitiesList.size} communities"
            }

            commonViewModel.selectedCommunityList.observe(viewLifecycleOwner) { selectedCommunitiesList ->
                communityItemAdapter?.savedCommunityList = selectedCommunitiesList as MutableList<Community>
            }
        }


        /* if (commonViewModel.selectedCommunityList.isNotEmpty()) {
             communityBottomBinding?.numberOfSelectedCommunity?.visibility = View.VISIBLE
             communityBottomBinding?.numberOfSelectedCommunity?.text =
                 "You have selected ${commonViewModel.selectedCommunityList.size} communities"
         }*/

        /*communityItemAdapter = CommunityItemAdapter { communitiesList ->
            if (commonViewModel.selectedCommunityList.isNotEmpty()) {
                val size: Int = communitiesList.size + commonViewModel.selectedCommunityList.size
                communityBottomBinding?.numberOfSelectedCommunity?.visibility = View.VISIBLE
                communityBottomBinding?.numberOfSelectedCommunity?.text =
                    "You have selected $size communities"
            }
            if (communitiesList.isNotEmpty()) {
                communities = communitiesList
                if (commonViewModel.selectedCommunityList.isEmpty()) {
                    communityBottomBinding?.numberOfSelectedCommunity?.visibility = View.VISIBLE
                    communityBottomBinding?.numberOfSelectedCommunity?.text =
                        "You have selected ${communitiesList.size} communities"
                }

            } else {
                communityBottomBinding?.numberOfSelectedCommunity?.visibility = View.GONE
            }
        }*/

        /*if (commonViewModel.selectedCommunityList.isNotEmpty()) {
            communityItemAdapter?.savedCommunityList = commonViewModel.selectedCommunityList
        }
*/

        communityBottomBinding?.apply {

            closeCommunityBtn.setOnClickListener {
                dismiss()
            }

            communitySubmitBtn.setOnClickListener {
                commonViewModel.addCommunityList(communities as MutableList<Community>)
                findNavController().navigateUp()
            }

            rvBottomFragment.layoutManager = FlexboxLayoutManager(context)
            rvBottomFragment.adapter = communityItemAdapter
        }

        return communityBottomBinding?.root
    }

    private fun getCommunities() {
        registrationViewModel.getCommunities()
        lifecycleScope.launchWhenCreated {
            registrationViewModel.communities.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        communityBottomBinding?.progressBarCommunityBottom?.visibility =
                            View.VISIBLE
                    }
                    is Resource.Success -> {
                        communityBottomBinding?.progressBarCommunityBottom?.visibility = View.GONE
                        response.data?.community?.let { communityItemAdapter?.setData(it) }
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                        Log.i("Loading", "Error: ${response.message}")
                        communityBottomBinding?.progressBarCommunityBottom?.visibility =
                            View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

}