package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentReviewAdvertiseBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewAdvertiseFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var reviewBinding: FragmentReviewAdvertiseBinding? = null
    val postClassifiedViewModel: PostAdvertiseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        reviewBinding = FragmentReviewAdvertiseBinding.inflate(inflater, container, false)

        reviewBinding?.apply {

            closeCommunityBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            context?.let {
                Glide.with(it)
                    .load(postClassifiedViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI])
                    .into(advertisementImage)
            }


        }

        return reviewBinding?.root
    }

}