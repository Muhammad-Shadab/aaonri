package com.aaonri.app.ui.authentication.register.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.HomeRecyclerViewAdapter
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.FragmentLocationDetailsBinding
import com.aaonri.app.ui.authentication.register.recyclerview.CommunityRecyclerViewItem
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    val commonViewModel: CommonViewModel by activityViewModels()
    var locationDetailsBinding: FragmentLocationDetailsBinding? = null
    var homeRecyclerViewAdapter: HomeRecyclerViewAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationDetailsBinding = FragmentLocationDetailsBinding.inflate(inflater, container, false)

        homeRecyclerViewAdapter?.items

        homeRecyclerViewAdapter = HomeRecyclerViewAdapter()

        val homeList = mutableListOf<CommunityRecyclerViewItem>()
        homeList.add(CommunityRecyclerViewItem.SelectedCommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.SelectedCommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.SelectedCommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.SelectedCommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.SelectedCommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.SelectedCommunityItem("Shadab"))


        locationDetailsBinding?.apply {

            homeRecyclerViewAdapter?.items = homeList

            /*if (commonViewModel.selectedCommunityList.isNotEmpty()) {
                *//*communityItemAdapter!!.setData(commonViewModel.selectedCommunityList)*//*


                selectCommunityEt.visibility = View.GONE
                selectedCommunitySizeTv.text =
                    "Your selected community (${commonViewModel.selectedCommunityList.size})"
            } else {
                selectedCardView.visibility = View.GONE
            }*/

            selectMoreCommunityIv.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_communityBottomFragment)
            }

            locationDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_servicesCategoryFragment2)
            }
            selectCommunityEt.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_communityBottomFragment)
            }

            rvLocationDetails.layoutManager = FlexboxLayoutManager(context)
            rvLocationDetails.adapter = homeRecyclerViewAdapter
        }

        return locationDetailsBinding?.root
    }

}