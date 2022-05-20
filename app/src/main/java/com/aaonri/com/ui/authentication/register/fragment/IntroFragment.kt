package com.aaonri.com.ui.authentication.register.fragment

import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.com.R
import com.aaonri.com.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {
    var introBinding: FragmentIntroBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        introBinding = FragmentIntroBinding.inflate(inflater, container, false)

        return introBinding?.root
    }
}