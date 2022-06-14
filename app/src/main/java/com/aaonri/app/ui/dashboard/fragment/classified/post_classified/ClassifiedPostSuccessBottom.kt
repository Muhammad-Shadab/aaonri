package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.classified.viewmodel.ClassifiedCommonViewModel
import com.aaonri.app.databinding.FragmentClassifiedPostSuccessBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifiedPostSuccessBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val classifiedCommonViewModel: ClassifiedCommonViewModel by activityViewModels()
    var bottomBinding: FragmentClassifiedPostSuccessBottomBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        bottomBinding =
            FragmentClassifiedPostSuccessBottomBinding.inflate(inflater, container, false)

        classifiedCommonViewModel.addStepViewLastTick(true)

        bottomBinding?.apply {
            viewYourClassifiedBtn.setOnClickListener {
                activity?.finish()
            }
        }

        return bottomBinding?.root
    }
}