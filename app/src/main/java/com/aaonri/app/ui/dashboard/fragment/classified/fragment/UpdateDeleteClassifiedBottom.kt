package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentClassifiedFilterBinding
import com.aaonri.app.databinding.FragmentUpdateDeleteClassifiedBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UpdateDeleteClassifiedBottom : BottomSheetDialogFragment()  {
    var updateDeleteClassifiedBottom: FragmentUpdateDeleteClassifiedBottomBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        FragmentUpdateDeleteClassifiedBottomBinding.inflate(inflater, container, false)
        updateDeleteClassifiedBottom?.apply {

        }
        return updateDeleteClassifiedBottom?.root
    }
}