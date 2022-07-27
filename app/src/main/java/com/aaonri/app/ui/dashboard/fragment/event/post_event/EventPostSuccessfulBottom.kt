package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventPostSuccessfulBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EventPostSuccessfulBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var eventBottomBinding: FragmentEventPostSuccessfulBottomBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        eventBottomBinding =
            FragmentEventPostSuccessfulBottomBinding.inflate(inflater, container, false)

        postEventViewModel.addStepViewLastTick(true)

        val text = "If a user wishes to make an ad Popular/Hot\n" +
                "Please email to admin@aaonri.com"

        val ss = SpannableString(text)


        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                val emailIntent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "admin@aaonri.com", null
                    )
                )
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }

        eventBottomBinding?.apply {

            if (postEventViewModel.isUpdateEvent) {
                successful.text = "You have successfully updated your Event"
            } else {
                successful.text = "You have successfully posted your Event"
            }
            textView6.text = ss
            textView6.movementMethod = LinkMovementMethod.getInstance()
            bottomLoginBtn.setOnClickListener {
                val intent = Intent()
                intent.putExtra("callEventApi", true)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }

        }

        return eventBottomBinding?.root
    }

}