package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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

        if (postAdvertiseViewModel.isUpdateAdvertise) {
            val action =
                PostAdvertiseTermConditionFragmentDirections.actionPostAdvertiseTermConditionFragment2ToPostAdvertiseCompanyDetailsFrgament2()
            findNavController().navigate(action)
        }

        val text1 = SpannableString(
            resources.getString(R.string.after_submit_request_text)
        )
        val text2 = SpannableString(
            resources.getString(R.string.once_the_ad)
        )
        val text3 = SpannableString(
            resources.getString(R.string.please_review_details)
        )
        val text4 = SpannableString(
            resources.getString(R.string.for_any_other)
        )

        val aoonrilink: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
//                Toast.makeText(context, "privacy", Toast.LENGTH_SHORT).show()

            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.underlineColor =
                    context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }


        text1.setSpan(aoonrilink, 48, 58, 0)
        text2.setSpan(aoonrilink, 25, 35, 0)
        text3.setSpan(aoonrilink, 133, 143, 0)
        val advertiseLink: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
//                Toast.makeText(context, "privacy", Toast.LENGTH_SHORT).show()

            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.underlineColor =
                    context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }
        text4.setSpan(advertiseLink, 46, 66, 0)



        termConditionBinding?.apply {


            textTv1.movementMethod = LinkMovementMethod.getInstance()
            textTv1.setText(text1, TextView.BufferType.SPANNABLE)
            textTv1.isSelected = true

            textTv2.movementMethod = LinkMovementMethod.getInstance()
            textTv2.setText(text2, TextView.BufferType.SPANNABLE)
            textTv2.isSelected = true

            textTv3.movementMethod = LinkMovementMethod.getInstance()
            textTv3.setText(text3, TextView.BufferType.SPANNABLE)
            textTv3.isSelected = true

            textTv4.movementMethod = LinkMovementMethod.getInstance()
            textTv4.setText(text4, TextView.BufferType.SPANNABLE)
            textTv4.isSelected = true





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