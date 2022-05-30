package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentSelectCountryBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectCountryBottomFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var selectCountryBottomBinding: FragmentSelectCountryBottomBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectCountryBottomBinding =
            FragmentSelectCountryBottomBinding.inflate(inflater, container, false)



        return selectCountryBottomBinding?.root
    }
}