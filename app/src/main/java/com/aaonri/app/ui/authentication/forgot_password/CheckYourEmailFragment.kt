package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentCheckYourEmailBinding

class CheckYourEmailFragment : Fragment() {
    var checkYourEmailBinding: FragmentCheckYourEmailBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkYourEmailBinding = FragmentCheckYourEmailBinding.inflate(inflater, container, false)

        checkYourEmailBinding?.apply {
            openEmailAppBtn.setOnClickListener {
                findNavController().navigate(R.id.action_checkYourEmailFragment_to_resetPasswordInfoFragment)
            }
        }

        return checkYourEmailBinding?.root
    }
}