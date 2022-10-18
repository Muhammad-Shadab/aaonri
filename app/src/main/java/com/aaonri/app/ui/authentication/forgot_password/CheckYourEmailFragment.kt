package com.aaonri.app.ui.authentication.forgot_password

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentCheckYourEmailBinding
import com.google.android.material.snackbar.Snackbar


class CheckYourEmailFragment : Fragment() {
    var binding: FragmentCheckYourEmailBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckYourEmailBinding.inflate(inflater, container, false)


        val text =
            resources.getString(R.string.spannableText1) + resources.getString(R.string.spannableText2)

        val ss = SpannableString(text)

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigateUp()
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

        ss.setSpan(UnderlineSpan(), 58, 78, 0)
        ss.setSpan(clickableSpan1, 54, 80, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding?.didYouReceive?.text = ss
        binding?.didYouReceive?.movementMethod = LinkMovementMethod.getInstance()


        binding?.apply {

            skipForNowTv.setOnClickListener {
                findNavController().navigate(R.id.action_checkYourEmailFragment_to_loginFragment)
            }
            openEmailAppBtn.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                    activity?.finish()
                } catch (e: Exception) {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Email app is not installed", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}