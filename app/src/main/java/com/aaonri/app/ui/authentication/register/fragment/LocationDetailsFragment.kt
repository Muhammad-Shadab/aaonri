package com.aaonri.app.ui.authentication.register.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.SelectedCommunityAdapter
import com.aaonri.app.data.authentication.register.model.Community
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLocationDetailsBinding
import com.example.newsapp.utils.Resource
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

            selectedCommunityAdapter!!.setData(commonViewModel.selectedCommunityList)

            locationDetailsBinding?.selectCommunityEt?.visibility = View.GONE
            locationDetailsBinding?.selectMoreCommunityIv?.visibility = View.VISIBLE
        } else {
            locationDetailsBinding?.selectCommunityEt?.visibility = View.VISIBLE
        }


        locationDetailsBinding?.apply {

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