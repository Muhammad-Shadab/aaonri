package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPostAdvertiseTermConditionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAdvertiseTermConditionFragment : Fragment() {
    var termConditionBinding: FragmentPostAdvertiseTermConditionBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        termConditionBinding =
            FragmentPostAdvertiseTermConditionBinding.inflate(inflater, container, false)

        findNavController().navigate(R.id.action_postAdvertiseTermConditionFragment2_to_postAdvertiseCompanyDetailsFrgament2)

        termConditionBinding?.apply {
            advertisePostNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertiseTermConditionFragment2_to_postAdvertiseCompanyDetailsFrgament2)
            }
        }
        return termConditionBinding?.root
    }


}