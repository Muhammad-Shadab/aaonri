package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertiseTermConditionBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAdvertiseTermConditionFragment : Fragment() {
    var termConditionBinding: FragmentPostAdvertiseTermConditionBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        termConditionBinding =
            FragmentPostAdvertiseTermConditionBinding.inflate(inflater, container, false)

        postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_TERMS_AND_CONDITION)

        if (postAdvertiseViewModel.isRenewAdvertise) {
            findNavController().navigate(R.id.action_postAdvertiseTermConditionFragment2_to_postAdvertiseCheckout)
        }

        Toast.makeText(context, "${postAdvertiseViewModel.advertiseId}", Toast.LENGTH_SHORT).show()

        termConditionBinding?.apply {
            advertisePostNextBtn.setOnClickListener {
                if (agreeCheckboxClassified.isChecked) {
                    findNavController().navigate(R.id.action_postAdvertiseTermConditionFragment2_to_postAdvertiseCompanyDetailsFrgament2)
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please accept terms & condition", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        return termConditionBinding?.root
    }


}