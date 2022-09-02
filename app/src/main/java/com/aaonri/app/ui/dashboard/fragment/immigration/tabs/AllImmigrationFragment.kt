package com.aaonri.app.ui.dashboard.fragment.immigration.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaonri.app.databinding.FragmentAllImmigrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllImmigrationFragment : Fragment() {
    var binding: FragmentAllImmigrationBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllImmigrationBinding.inflate(layoutInflater, container, false)


        binding?.apply {


        }



        return binding?.root

    }
}