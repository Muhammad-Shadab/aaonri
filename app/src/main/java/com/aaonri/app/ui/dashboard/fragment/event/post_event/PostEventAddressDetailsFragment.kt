package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentPostEventAddressDetailsBinding
import com.aaonri.app.databinding.FragmentPostEventBasicDetailsBinding
import com.google.android.material.snackbar.Snackbar


class PostEventAddressDetailsFragment : Fragment() {
    var postEventAddressBinding: FragmentPostEventAddressDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postEventAddressBinding = FragmentPostEventAddressDetailsBinding.inflate(inflater, container, false)

        Toast.makeText(context, "${postEventViewModel.isEventOffline}", Toast.LENGTH_SHORT).show()

        postEventAddressBinding?.apply {
            classifiedDetailsNextBtn.setOnClickListener {

             if(cityNameEt.text.toString().isNotEmpty() && cityNameEt.text.toString().length < 3) {
                 showAlert("Please enter valid city name")
             }
             else{
                 if(zipCodeEt.text.toString().isNotEmpty() && zipCodeEt.text.toString().length >= 5)
                 {
                         if(landmarkEt.text.toString().isNotEmpty() && landmarkEt.text.toString().length <3 )
                         {
                             showAlert("Please enter valid landmark")
                         }
                     else{
                         if(stateEt.text.toString().isNotEmpty() && stateEt.text.toString().length < 3)
                         {
                             showAlert("Please enter valid state ")
                         }
                             else{
                             findNavController().navigate(R.id.action_postEventAddressDetailsFragment_to_eventPostSuccessfulBottom)
                             }
                     }
                 }
                 else{
                     showAlert("Please enter valid zip code")
                 }
             }

            }
        }
        return postEventAddressBinding?.root
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }
}