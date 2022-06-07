package com.aaonri.app.ui.authentication.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentCreateNewPasswordBinding

class CreateNewPasswordFragment : Fragment() {
    var createNewPasswordBinding: FragmentCreateNewPasswordBinding? = null
    val args: CreateNewPasswordFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createNewPasswordBinding =
            FragmentCreateNewPasswordBinding.inflate(inflater, container, false)



        createNewPasswordBinding?.apply {
            createNewPasswordBtn.setOnClickListener {
                findNavController().navigate(R.id.action_createNewPasswordFragment_to_passwordResetSuccessBottom)
            }
        }

        return createNewPasswordBinding?.root

    }
}