package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Bundle
import android.view.Gravity
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
    var binding: FragmentReviewAdvertiseBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding = FragmentReviewAdvertiseBinding.inflate(inflater, container, false)

        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date = dateFormat.format(calender.time)

        binding?.apply {

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
            postedDate2.text = getCalculatedDate("MM-dd-yyyy", 7)

            if (postAdvertiseViewModel.companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK]?.isNotEmpty() == true) {
                advertiseLinkTv.text =
                    postAdvertiseViewModel.companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK]
                advertiseLinkTv.visibility = View.VISIBLE
            }
            advertiseDesc.textSize = 12f

            advertiseDesc.fromHtml(postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_AD_DESCRIPTION])
            companyDesc.fromHtml(postAdvertiseViewModel.companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_DESCRIPTION])

            when (postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_TEMPLATE_CODE]) {
                "IMTB" -> {
                    advertiseDesc.gravity = Gravity.CENTER or Gravity.BOTTOM
                }
                "IMTL" -> {
                    advertiseDesc.gravity = Gravity.START or Gravity.BOTTOM
                }
            }

        }

        return binding?.root
    }

    fun getCalculatedDate(dateFormat: String?, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}