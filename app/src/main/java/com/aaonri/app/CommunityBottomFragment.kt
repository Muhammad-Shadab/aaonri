package com.aaonri.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackgroundTintList
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.authentication.register.adapter.HomeRecyclerViewAdapter
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.CommunityItemBinding
import com.aaonri.app.databinding.FragmentCommunityBottomBinding
import com.aaonri.app.ui.authentication.register.recyclerview.CommunityRecyclerViewItem
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommunityBottomFragment : BottomSheetDialogFragment() {
    val commonViewModel: CommonViewModel by activityViewModels()
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var communityBottomBinding: FragmentCommunityBottomBinding? = null
    var homeRecyclerViewAdapter: HomeRecyclerViewAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communityBottomBinding = FragmentCommunityBottomBinding.inflate(inflater, container, false)

        homeRecyclerViewAdapter = HomeRecyclerViewAdapter()

        homeRecyclerViewAdapter?.items = commonViewModel.homeList

        homeRecyclerViewAdapter?.itemClickListener = { view, item, position ->
            when (item) {
                is CommunityRecyclerViewItem.CommunityItem -> {
                    commonViewModel.addToCommunityList(item.title)
                }
                is CommunityRecyclerViewItem.SelectedCommunityItem -> TODO()
            }
        }

        communityBottomBinding?.apply {

            if (commonViewModel.selectedCommunityList.isNotEmpty()) {
                numberOfSelectedCommunity.text =
                    "You have selected ${commonViewModel.selectedCommunityList.size} communities"
                homeRecyclerViewAdapter?.items = commonViewModel.homeList

            } else {
                numberOfSelectedCommunity.visibility = View.GONE
            }

            closeCommunityBts.setOnClickListener {
                dismiss()
            }

            communitySubmitBtn.setOnClickListener {
                findNavController().navigate(R.id.action_communityBottomFragment_to_locationDetailsFragment2)
            }

            /*communityItemAdapter?.setData(
                data = listOf(
                    "Shadab Saif",
                    "Aryaf",
                    "Assd",
                    "frfsve",
                    "assadsjd",
                    "asdassd",
                    "Asdsdvsd",
                    "Assdvsdvdad",
                    "frfsdvsdve",
                    "Computer Hardware",
                )
            )*/

            rvBottomFragment.layoutManager = FlexboxLayoutManager(context)
            rvBottomFragment.adapter = homeRecyclerViewAdapter
        }

        return communityBottomBinding?.root
    }
}