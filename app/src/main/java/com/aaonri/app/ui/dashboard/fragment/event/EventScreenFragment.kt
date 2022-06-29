package com.aaonri.app.ui.dashboard.fragment.event

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.MainActivity
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentEventScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventScreenFragment : Fragment() {
    var eventScreenBinding: FragmentEventScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventScreenBinding = FragmentEventScreenBinding.inflate(inflater, container, false)

        eventScreenBinding?.apply {
            floatingActionBtnEvents.setOnClickListener {
                val intent = Intent(requireContext(), EventScreenActivity::class.java)
                startActivity(intent)
            }
        }

        return eventScreenBinding?.root
    }
}