package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPasswordResetSuccessBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PasswordResetSuccessBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentPasswordResetSuccessBottomBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding =
            FragmentPasswordResetSuccessBottomBinding.inflate(inflater, container, false)

        binding?.apply {
            bottomLoginBtn.setOnClickListener {
                findNavController().navigate(R.id.action_passwordResetSuccessBottom_to_loginFragment)
            }
        }

        /*requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Toast.makeText(context, "back cliked", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_passwordResetSuccessBottom_to_loginFragment)
                }
            })*/

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}