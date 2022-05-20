package com.aaonri.com.ui.authentication.register.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.aaonri.com.R

class BasicDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_basic_details, container, false)

        val textView = view.findViewById<TextView>(R.id.fragment1)
        textView.setOnClickListener {
            findNavController().navigate(R.id.action_basicDetailsFragment_to_addressDetailsFragment)
        }

        return view
    }
}