package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentUpdateAndDeleteBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UpdateAndDeleteBottomFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentUpdateAndDeleteBottomBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentUpdateAndDeleteBottomBinding.inflate(inflater, container, false)


        binding?.apply {

        }


        return binding?.root


    }
}