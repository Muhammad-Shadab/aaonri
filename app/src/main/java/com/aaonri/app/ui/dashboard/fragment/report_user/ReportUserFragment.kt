package com.aaonri.app.ui.dashboard.fragment.report_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.aaonri.app.databinding.FragmentReportUserBinding

class ReportUserFragment : Fragment() {
    var binding: FragmentReportUserBinding? = null
    val args: ReportUserFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReportUserBinding.inflate(layoutInflater, container, false)

        val nameParts = args.userName.split(" ").toTypedArray()
        val firstName = nameParts[0]
        val firstNameChar = firstName[0]
        var lastName = ""
        var lastNameChar = ""
        if (nameParts.size > 1) {
            lastName = nameParts[nameParts.size - 1]
            lastNameChar = lastName[0].toString()
        }

        binding?.apply {

            firstNameBasicDetails.text = firstName
            lastNameBasicDetails.text = lastName
            emailAddressBasicDetails.text = args.email


        }


        return binding?.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}