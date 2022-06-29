package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPostEventAddressDetailsBinding
import com.aaonri.app.databinding.FragmentPostEventBasicDetailsBinding


class PostEventAddressDetailsFragment : Fragment() {
    var postEventAddressBinding: FragmentPostEventAddressDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        postEventAddressBinding = FragmentPostEventAddressDetailsBinding.inflate(inflater, container, false)
        postEventAddressBinding.apply {

        }
        return postEventAddressBinding?.root
    }

}