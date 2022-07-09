package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentUpdateDeleteClassifiedBottomBinding
import com.aaonri.app.ui.dashboard.fragment.classified.ClassifiedActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UpdateDeleteClassifiedBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var updateDeleteBinding: FragmentUpdateDeleteClassifiedBottomBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateDeleteBinding =
            FragmentUpdateDeleteClassifiedBottomBinding.inflate(inflater, container, false)

        updateDeleteBinding?.apply {

            updateClassified.setOnClickListener {
                val intent = Intent(requireContext(), ClassifiedActivity::class.java)
                startActivity(intent)
                dismiss()

            }

        }

        return updateDeleteBinding?.root
    }
}