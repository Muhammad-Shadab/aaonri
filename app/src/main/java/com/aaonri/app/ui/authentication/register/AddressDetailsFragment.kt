package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentAddressDetailsBinding

class AddressDetailsFragment : Fragment() {
    var addressDetailsBinding: FragmentAddressDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addressDetailsBinding = FragmentAddressDetailsBinding.inflate(inflater, container, false)

        addressDetailsBinding?.apply {
            addressDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_addressDetailsFragment2_to_locationDetailsFragment2)
            }
        }

        return addressDetailsBinding?.root
    }

}