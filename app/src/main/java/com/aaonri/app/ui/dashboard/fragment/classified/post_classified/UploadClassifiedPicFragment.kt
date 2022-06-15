package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentUploadClassifiedPicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadClassifiedPicFragment : Fragment() {
    var uploadClassifiedBinding: FragmentUploadClassifiedPicBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uploadClassifiedBinding =
            FragmentUploadClassifiedPicBinding.inflate(inflater, container, false)

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.UPLOAD_PIC_SCREEN)

        uploadClassifiedBinding?.apply {
            classifiedUploadPicNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_uploadClassifiedPicFragment_to_addressDetailsClassifiedFragment)
            }
        }

        return uploadClassifiedBinding?.root
    }
}