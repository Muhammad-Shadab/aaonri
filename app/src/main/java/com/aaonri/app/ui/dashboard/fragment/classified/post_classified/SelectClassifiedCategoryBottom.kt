package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentSelectClassifiedCategoryBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectClassifiedCategoryBottom : BottomSheetDialogFragment() {
    var selectClassifiedCategoryBottom: FragmentSelectClassifiedCategoryBottomBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        selectClassifiedCategoryBottom =
            FragmentSelectClassifiedCategoryBottomBinding.inflate(inflater, container, false)


        return selectClassifiedCategoryBottom?.root
    }
}