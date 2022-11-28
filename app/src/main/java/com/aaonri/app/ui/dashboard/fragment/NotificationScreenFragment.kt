package com.aaonri.app.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaonri.app.databinding.FragmentNotificationScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationScreen : Fragment() {
    var binding: FragmentNotificationScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotificationScreenBinding.inflate(layoutInflater, container, false)

        binding?.apply {

        }


        return binding?.root


    }
}