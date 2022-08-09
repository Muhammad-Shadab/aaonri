package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertiseCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAdvertiseCheckout : Fragment() {
    var checkoutBinding: FragmentPostAdvertiseCheckoutBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        checkoutBinding = FragmentPostAdvertiseCheckoutBinding.inflate(inflater, container, false)
        checkoutBinding?.apply {
            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_CHECKOUT)
            checkoutBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
            }

        }
        return checkoutBinding?.root
    }

}