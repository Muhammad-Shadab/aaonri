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
import com.aaonri.app.databinding.FragmentResetPasswordInfoBinding

class ResetPasswordInfoFragment : Fragment() {
    val args: ResetPasswordInfoFragmentArgs by navArgs()
    var resetPasswordBinding: FragmentResetPasswordInfoBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = FragmentResetPasswordInfoBinding.inflate(inflater, container, false)

        Toast.makeText(context, "id = ${args.id} email = ${args.email}", Toast.LENGTH_SHORT).show()

        resetPasswordBinding?.apply {
            resetPasswordBtn.setOnClickListener {
                val action =
                    ResetPasswordInfoFragmentDirections.actionResetPasswordInfoFragmentToCreateNewPasswordFragment(
                        args.email
                    )
                findNavController().navigate(action)
            }
        }

        return resetPasswordBinding?.root
    }
}