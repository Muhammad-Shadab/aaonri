package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentUpdateAndDeleteBottomBinding

class UpdateAndDeleteBottomFragment : Fragment() {
   var upadateDeleteBindig :FragmentUpdateAndDeleteBottomBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        upadateDeleteBindig =  FragmentUpdateAndDeleteBottomBinding.inflate(inflater, container, false)
        upadateDeleteBindig?.apply {

        }
        return upadateDeleteBindig?.root


    }
}