package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPostAdvertiseCompanyDetailsFrgamentBinding

class PostAdvertiseCompanyDetailsFrgament : Fragment() {
 var detailsBinding : FragmentPostAdvertiseCompanyDetailsFrgamentBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        detailsBinding= FragmentPostAdvertiseCompanyDetailsFrgamentBinding.inflate(inflater, container, false)
        detailsBinding?.apply {
            advertiseDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertiseCompanyDetailsFrgament_to_postAdvertisementbasicDetailsFragment)
            }
        }
    return  detailsBinding?.root
    }

}