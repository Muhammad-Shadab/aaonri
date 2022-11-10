package com.aaonri.app.utils

import android.animation.LayoutTransition
import android.content.Context
import android.os.Build
import android.os.Handler
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.aaonri.app.R

class ReadMoreTextView(
    var context: Context? = null,
    val textLength: Int = 0,
    val textLengthType: Int = 0,
    val moreLabel: String? = null,
    val lessLabel: String? = null,
) {

    private constructor(builder: Builder) : this(
        builder.context,
        builder.textLength,
        builder.textLengthType,
        builder.moreLabel,
        builder.lessLabel,
    )

    fun addReadMoreTo(textView: TextView, text: CharSequence) {
        if (textLengthType == ReadMoreTextView.TYPE_CHARACTER) {
            if (text.length <= textLength) {
                textView.text = text
                return
            }
        } else {
            // If TYPE_LINE
            textView.setLines(textLength)
            textView.text = text
        }
        textView.post(Runnable {
            var textLengthNew = textLength
            if (textLengthType == ReadMoreTextView.TYPE_LINE) {
                if (textView.layout.lineCount <= textLength) {
                    textView.text = text
                    return@Runnable
                }
                val lp = textView.layoutParams as ViewGroup.MarginLayoutParams
                val subString = text.toString().substring(
                    textView.layout.getLineStart(0),
                    textView.layout.getLineEnd(textLength - 1)
                )
                textLengthNew = subString.length - (moreLabel!!.length + 4 + lp.rightMargin / 6)
            }
            val spannableStringBuilder = SpannableStringBuilder(text.subSequence(0, textLengthNew))
                .append("...")
                .append(moreLabel)
            val ss = SpannableString.valueOf(spannableStringBuilder)
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    addReadLess(textView, text)
                }

                @RequiresApi(Build.VERSION_CODES.Q)
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true

                    ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                }

            }
            ss.setSpan(
                clickableSpan,
                ss.length - moreLabel!!.length,
                ss.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val layoutTransition = LayoutTransition()
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            (textView.parent as ViewGroup).layoutTransition = layoutTransition
            textView.text = ss
            textView.movementMethod = LinkMovementMethod.getInstance()
        })
    }

    private fun addReadLess(textView: TextView, text: CharSequence) {
        textView.maxLines = Int.MAX_VALUE
        val spannableStringBuilder = SpannableStringBuilder(text)
            .append(lessLabel)
        val ss = SpannableString.valueOf(spannableStringBuilder)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Handler().post { addReadMoreTo(textView, text) }
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true

                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }

        }
        ss.setSpan(
            clickableSpan,
            ss.length - lessLabel!!.length,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    class Builder(c: Context) {
        val context: Context = c

        var textLength = 100
            private set
        var textLengthType = ReadMoreTextView.TYPE_CHARACTER
            private set
        var moreLabel = "mais"
            private set
        var lessLabel = "menos"
            private set


        fun textLength(length: Int?, typeLine: Int) = apply { textLength = length ?: 100 }
        fun textLengthType(type: Int) = apply { textLengthType = type }
        fun moreLabel(more: String) = apply { moreLabel = more }
        fun lessLabel(less: String) = apply { lessLabel = less }

        fun build() = ReadMoreTextView(this)
    }

    companion object {
        private val TAG = ReadMoreTextView::class.java.simpleName
        const val TYPE_LINE = 1
        const val TYPE_CHARACTER = 2
    }

}