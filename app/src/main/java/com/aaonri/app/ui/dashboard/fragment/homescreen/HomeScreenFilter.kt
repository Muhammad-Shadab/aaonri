package com.aaonri.app.ui.dashboard.fragment.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaonri.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFilter : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen_filter, container, false)
    }
}