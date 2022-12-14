package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedPostSuccessBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifiedPostSuccessBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var binding: FragmentClassifiedPostSuccessBottomBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding =
            FragmentClassifiedPostSuccessBottomBinding.inflate(inflater, container, false)

        postClassifiedViewModel.addStepViewLastTick(true)

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
        ss.setSpan(clickableSpan1, 59, 75, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding?.apply {
            if (postClassifiedViewModel.isUpdateClassified) {
                enterYourEmailToSendLinkTv.text =
                    "Congratulations \n You have successfully updated your Classified"
            } else {
                enterYourEmailToSendLinkTv.text =
                    "Congratulations \n You have successfully posted your Classified"
            }
            textView6.text = ss
            textView6.movementMethod = LinkMovementMethod.getInstance()
            viewYourClassifiedBtn.setOnClickListener {
                val intent = Intent()
                intent.putExtra("callClassifiedApi", true)
                if (postClassifiedViewModel.isUpdateClassified){
                    intent.putExtra("isClassifiedUpdate", true)
                }
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}