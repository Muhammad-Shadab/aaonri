package com.aaonri.app.ui.dashboard.fragment.advertise

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentAdvertiseScreenBinding
import com.aaonri.app.ui.dashboard.fragment.event.EventScreenActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvertiseScreenFragment : Fragment() {
    var advertiseBinding: FragmentAdvertiseScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
        startActivityForResult(intent, 3)

        advertiseBinding = FragmentAdvertiseScreenBinding.inflate(inflater, container, false)
        advertiseBinding?.apply {
            floatingActionBtnEvents.setOnClickListener {
                val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
                startActivityForResult(intent, 3)
            }
        }

        return advertiseBinding?.root
    }
}