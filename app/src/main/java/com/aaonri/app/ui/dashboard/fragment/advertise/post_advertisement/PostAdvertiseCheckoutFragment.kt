package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPostAdvertiseCheckoutBinding

class PostAdvertiseCheckout : Fragment() {
    var checkoutBinding : FragmentPostAdvertiseCheckoutBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        checkoutBinding =  FragmentPostAdvertiseCheckoutBinding.inflate(inflater, container, false)
        checkoutBinding?.apply {
            checkoutBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
            }

        }
        return checkoutBinding?.root
    }

}