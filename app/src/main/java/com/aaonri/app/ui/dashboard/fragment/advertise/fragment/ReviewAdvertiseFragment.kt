package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentReviewAdvertiseBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ReviewAdvertiseFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var reviewBinding: FragmentReviewAdvertiseBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        reviewBinding = FragmentReviewAdvertiseBinding.inflate(inflater, container, false)

        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date = dateFormat.format(calender.time)


        reviewBinding?.apply {

            closeCommunityBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            context?.let {
                Glide.with(it)
                    .load(postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI])
                    .into(advertisementImage)
            }

            advertiseNameTv.text =
                postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_ADD_TITLE]

            advertiseLocationTv.text =
                postAdvertiseViewModel.companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LOCATION]

            postedDate1.text = date
            advertiseLinkTv.text =
                postAdvertiseViewModel.companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK]

        }

        return reviewBinding?.root
    }

}