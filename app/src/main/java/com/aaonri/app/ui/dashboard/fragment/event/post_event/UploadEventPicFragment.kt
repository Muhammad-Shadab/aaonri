package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentCreateNewPasswordBinding
import com.aaonri.app.databinding.FragmentUploadEventPicBinding

class UploadEventPicFragment : Fragment() {
    var uploadEventPicBinding: FragmentUploadEventPicBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        uploadEventPicBinding = FragmentUploadEventPicBinding.inflate(inflater, container, false)

        uploadEventPicBinding?.apply {
            eventUploadPicNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_uploadEventPicFragment_to_postEventAddressDetailsFragment)
            }
        }

        return uploadEventPicBinding?.root
    }
}