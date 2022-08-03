package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPostAdvertisementbasicDetailsBinding


class PostAdvertisementbasicDetailsFragment : Fragment() {
   var advertiseBinding : FragmentPostAdvertisementbasicDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        advertiseBinding=  FragmentPostAdvertisementbasicDetailsBinding.inflate(inflater, container, false)

        advertiseBinding?.apply {
            advertiseDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_postAdvertiseCheckout)
            }

            previewAdvertiseBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_reviewAdvertiseFragment)

            }

            advertiseDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_postAdvertiseCheckout)

            }

        }
        return  advertiseBinding?.root
    }


}