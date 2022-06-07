package com.aaonri.app.ui.authentication.forgot_password

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentCheckYourEmailBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


class CheckYourEmailFragment : Fragment() {
    var checkYourEmailBinding: FragmentCheckYourEmailBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkYourEmailBinding = FragmentCheckYourEmailBinding.inflate(inflater, container, false)

        checkYourEmailBinding?.apply {
            skipForNowTv.setOnClickListener {
                findNavController().navigate(R.id.action_checkYourEmailFragment_to_resetPasswordInfoFragment)
            }
            openEmailAppBtn.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                } catch (e: Exception) {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Gmail app is not installed", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        return checkYourEmailBinding?.root
    }
}