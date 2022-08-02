package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentReviewAdvertiseBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReviewAdvertiseFragment  : BottomSheetDialogFragment() {
  var reviewBinding : FragmentReviewAdvertiseBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        reviewBinding =  FragmentReviewAdvertiseBinding.inflate(inflater, container, false)
        return reviewBinding?.root
    }

}