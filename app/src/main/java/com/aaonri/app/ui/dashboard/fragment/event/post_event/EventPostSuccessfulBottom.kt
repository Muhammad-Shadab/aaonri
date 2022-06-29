package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentEventPostSuccessfulBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EventPostSuccessfulBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var eventBottomBinding: FragmentEventPostSuccessfulBottomBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        eventBottomBinding =
            FragmentEventPostSuccessfulBottomBinding.inflate(inflater, container, false)

        eventBottomBinding?.apply {

            bottomLoginBtn.setOnClickListener {
                activity?.finish()
            }

        }

        return eventBottomBinding?.root
    }
}