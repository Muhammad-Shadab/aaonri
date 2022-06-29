package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPostEventAddressDetailsBinding
import com.aaonri.app.databinding.FragmentPostEventBasicDetailsBinding


class PostEventAddressDetailsFragment : Fragment() {
    var postEventAddressBinding: FragmentPostEventAddressDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postEventAddressBinding = FragmentPostEventAddressDetailsBinding.inflate(inflater, container, false)

        postEventAddressBinding?.apply {
            classifiedDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
            }
        }

        return postEventAddressBinding?.root
    }

}