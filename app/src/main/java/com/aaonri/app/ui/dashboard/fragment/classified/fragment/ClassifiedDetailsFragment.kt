package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.databinding.FragmentClassifiedDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ClassifiedDetailsFragment : Fragment() {
    var classifiedDetailsBinding: FragmentClassifiedDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedDetailsBinding =
            FragmentClassifiedDetailsBinding.inflate(inflater, container, false)


        classifiedDetailsBinding?.apply {
            BottomSheetBehavior.from(classifiedDetailsBottom).apply {
                peekHeight = 200
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        }


        return classifiedDetailsBinding?.root
    }
}