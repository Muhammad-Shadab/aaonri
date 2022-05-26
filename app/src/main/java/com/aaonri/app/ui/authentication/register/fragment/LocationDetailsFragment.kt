package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentLocationDetailsBinding

class LocationDetailsFragment : Fragment() {
    var locationDetailsBinding: FragmentLocationDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationDetailsBinding = FragmentLocationDetailsBinding.inflate(inflater, container, false)

        locationDetailsBinding?.apply {
            locationDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_servicesCategoryFragment2)
            }
            selectCommunityEt.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment2_to_communityBottomFragment)
            }
        }

        return locationDetailsBinding?.root
    }

}