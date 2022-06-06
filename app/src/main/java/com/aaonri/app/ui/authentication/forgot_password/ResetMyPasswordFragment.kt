package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentResetPasswordBinding

class ResetMyPasswordFragment : Fragment() {
    var resetPasswordBinding: FragmentResetPasswordBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = FragmentResetPasswordBinding.inflate(inflater, container, false)

        resetPasswordBinding?.apply {
            resetPasswordBtn.setOnClickListener {
                findNavController().navigate(R.id.action_resetPassword_to_checkYourEmailFragment)
            }
        }

        return resetPasswordBinding?.root

    }
}