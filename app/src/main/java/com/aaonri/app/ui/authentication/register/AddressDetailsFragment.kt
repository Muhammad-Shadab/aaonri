package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.FragmentAddressDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressDetailsFragment : Fragment() {
    val commonViewModel: CommonViewModel by activityViewModels()
    var addressDetailsBinding: FragmentAddressDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addressDetailsBinding = FragmentAddressDetailsBinding.inflate(inflater, container, false)

        addressDetailsBinding?.apply {

            addressDetailsNextBtn.setOnClickListener {

                val address1 = address1.text
                val address2 = address2.text
                val phoneNumber = phoneNumberAddressDetails.text

                if (address1.toString().isNotEmpty() && address2.toString()
                        .isNotEmpty() && phoneNumber.toString().isNotEmpty()
                ) {
                    commonViewModel.addAddressDetails(
                        address1.toString(),
                        address2.toString(),
                        phoneNumber.toString()
                    )
                }
                findNavController().navigate(R.id.action_addressDetailsFragment_to_locationDetailsFragment)
            }
        }

        return addressDetailsBinding?.root
    }

}