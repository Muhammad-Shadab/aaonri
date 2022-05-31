package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.FragmentBasicDetailsBinding

class BasicDetailsFragment : Fragment() {
    var basicDetailsBinding: FragmentBasicDetailsBinding? = null
    val commonViewModel: CommonViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        basicDetailsBinding = FragmentBasicDetailsBinding.inflate(inflater, container, false)

        val firstName = basicDetailsBinding?.firstNameBasicDetails?.text
        val lastName = basicDetailsBinding?.lastNameBasicDetails?.text
        val emailAddress = basicDetailsBinding?.emailAddressBasicDetails?.text
        val password = basicDetailsBinding?.passwordBasicDetails?.text

        basicDetailsBinding?.apply {

            basicDetailsNextBtn.setOnClickListener {
                if (firstName?.isNotEmpty() == true && lastName?.isNotEmpty() == true && emailAddress?.isNotEmpty() == true && password?.isNotEmpty() == true) {
                    commonViewModel.addBasicDetails(
                        firstName.toString(),
                        lastName.toString(),
                        emailAddress.toString(),
                        password.toString()
                    )
                }
                findNavController().navigate(R.id.action_basicDetailsFragment2_to_locationDetailsFragment22)
            }
        }
        return basicDetailsBinding?.root
    }

}