package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPostEventBasicDetailsBinding
import com.google.android.material.snackbar.Snackbar

class PostEventBasicDetailsFragment : Fragment() {
    var postEventBinding: FragmentPostEventBasicDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postEventBinding = FragmentPostEventBasicDetailsBinding.inflate(inflater, container, false)

        postEventBinding?.apply {


           /* isFreeEntryCheckBox.setOnCheckedChangeListener { compoundButton, b ->
                Toast.makeText(c, "", Toast.LENGTH_SHORT).show()
            }*/

                    if (titleEvent.text.toString().isNotEmpty() && titleEvent.text.trim()
                            .toString().length >= 3
                    ) {

                         if(selectCategoryEvent.text.toString().isNotEmpty())
                         {
                             if(selectstartDate.text.toString().isNotEmpty())
                             {
                                 if(selectStartTime.text.toString().isNotEmpty())
                                 {
                                     if(selectEndDate.text.toString().isNotEmpty())
                                     {
                                         if(selectEndTime.text.toString().isNotEmpty())
                                         {

                                             if(askingFee.text.toString().isNotEmpty()&&askingFee.text.toString().trim().toDouble()>0)
                                             {
                                                 if (eventDescEt.text.isNotEmpty()) {

                                                 }
                                                 else {
                                                     showAlert("Please enter valid event description")
                                                 }
                                             }
                                             else{
                                                 showAlert("Please enter valid fee")
                                             }
                                         }
                                         else{
                                             showAlert("Please enter valid end time")
                                         }
                                     }
                                     else{
                                         showAlert("Please enter valid end date")
                                     }
                                 }
                                 else{
                                     showAlert("Please enter valid start time")
                                 }
                             }
                             else{
                                 showAlert("Please enter valid start date")
                             }
                         }
                        else{
                             showAlert("Please select valid category")
                        }

                    } else {
                        showAlert("Please enter valid event title")
                    }
        }

        return postEventBinding?.root
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