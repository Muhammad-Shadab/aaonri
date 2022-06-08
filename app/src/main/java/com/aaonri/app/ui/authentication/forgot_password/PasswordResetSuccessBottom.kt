package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPasswordResetSuccessBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PasswordResetSuccessBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var passResetBinding: FragmentPasswordResetSuccessBottomBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        passResetBinding =
            FragmentPasswordResetSuccessBottomBinding.inflate(inflater, container, false)

        passResetBinding?.apply {
            bottomLoginBtn.setOnClickListener {
                findNavController().popBackStack(R.id.loginFragment, true)
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.loginFragment, true)
                }
            })

        return passResetBinding?.root
    }
}