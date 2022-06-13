package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentClassifiedBasicDetailsBinding
import com.aaonri.app.databinding.FragmentClassifiedDetailsBinding

class ClassifiedBasicDetailsFragment : Fragment() {
    var classifiedDetailsBinding: FragmentClassifiedBasicDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedDetailsBinding =
            FragmentClassifiedBasicDetailsBinding.inflate(inflater, container, false)

        classifiedDetailsBinding?.apply {

            classifiedDescEt.addTextChangedListener { editable ->
                descLength.text = "${editable.toString().length}/2000"
            }
        }

        return classifiedDetailsBinding?.root
    }
}