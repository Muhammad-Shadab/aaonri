package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentClassifiedFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ClassifiedFilterFragmentBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var classifiedFilterBinding: FragmentClassifiedFilterBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        classifiedFilterBinding =
            FragmentClassifiedFilterBinding.inflate(inflater, container, false)

        classifiedFilterBinding?.apply {
            closeClassifiedBtn.setOnClickListener {
                dismiss()
            }
        }

        return classifiedFilterBinding?.root
    }

}