package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentBasicDetailsBinding

class BasicDetailsFragment : Fragment() {
    var basicDetailsBinding: FragmentBasicDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        basicDetailsBinding = FragmentBasicDetailsBinding.inflate(inflater, container, false)
        basicDetailsBinding?.apply {
            basicDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_basicDetailsFragment2_to_addressDetailsFragment2)
            }
        }
        return basicDetailsBinding?.root
    }
}