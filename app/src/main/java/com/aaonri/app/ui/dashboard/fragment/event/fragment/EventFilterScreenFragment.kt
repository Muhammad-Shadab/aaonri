package com.aaonri.app.ui.dashboard.fragment.event.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentEventFilterScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFilterScreenFragment : Fragment() {
    var binding: FragmentEventFilterScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEventFilterScreenBinding.inflate(layoutInflater, container, false)

        binding?.apply {

        }

        return binding?.root
    }
}