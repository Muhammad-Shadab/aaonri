package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentResetPasswordInfoBinding

class ResetPasswordInfoFragment : Fragment() {
    var resetPasswordBinding: FragmentResetPasswordInfoBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = FragmentResetPasswordInfoBinding.inflate(inflater, container, false)

        resetPasswordBinding?.apply {
            resetPasswordBtn.setOnClickListener {
                findNavController().navigate(R.id.action_resetPasswordInfoFragment_to_createNewPasswordFragment)
            }
        }

        return resetPasswordBinding?.root
    }
}