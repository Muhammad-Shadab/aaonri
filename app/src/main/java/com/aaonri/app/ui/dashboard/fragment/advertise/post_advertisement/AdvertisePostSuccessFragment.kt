package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentAdvertisePostSuccessBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AdvertisePostSuccessFragment  : BottomSheetDialogFragment() {
  var successBotomBinding : FragmentAdvertisePostSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        successBotomBinding =  FragmentAdvertisePostSuccessBinding.inflate(inflater, container, false)
        successBotomBinding?.apply {

        }
        return successBotomBinding?.root
    }


}