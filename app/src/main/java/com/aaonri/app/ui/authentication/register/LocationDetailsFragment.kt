package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.SelectedCommunityAdapter
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.FragmentLocationDetailsBinding
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    val commonViewModel: CommonViewModel by activityViewModels()
    var locationDetailsBinding: FragmentLocationDetailsBinding? = null
    var selectedCommunityAdapter: SelectedCommunityAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationDetailsBinding = FragmentLocationDetailsBinding.inflate(inflater, container, false)

        selectedCommunityAdapter = SelectedCommunityAdapter()

        if (commonViewModel.selectedCommunityList.isNotEmpty()) {
            locationDetailsBinding?.selectedCommunitySizeTv?.text =
                "Your selected community ${commonViewModel.selectedCommunityList.size}"
            selectedCommunityAdapter!!.setData(commonViewModel.selectedCommunityList)
            locationDetailsBinding?.selectCommunityEt?.visibility = View.GONE
            locationDetailsBinding?.selectMoreCommunityIv?.visibility = View.VISIBLE
        } else {
            locationDetailsBinding?.selectCommunityEt?.visibility = View.VISIBLE
            locationDetailsBinding?.selectedCardView?.visibility = View.GONE
        }


        locationDetailsBinding?.apply {

            if (commonViewModel.selectedCountry?.first?.isNotEmpty() == true) {
                countryFlagIcon.load(commonViewModel.selectedCountry?.second)
                selectCountryOrigin.text = commonViewModel.selectedCountry?.first
                countryFlagIcon.visibility = View.VISIBLE
            } else {
                countryFlagIcon.visibility = View.GONE
            }

            selectMoreCommunityIv.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_communityBottomFragment)
            }

            locationDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_servicesCategoryFragment2)
            }
            selectCommunityEt.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_communityBottomFragment)
            }
            selectCountryOrigin.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_selectCountryBottomFragment)
            }

            rvLocationDetails.layoutManager = FlexboxLayoutManager(context)
            rvLocationDetails.adapter = selectedCommunityAdapter
        }

        return locationDetailsBinding?.root
    }

}