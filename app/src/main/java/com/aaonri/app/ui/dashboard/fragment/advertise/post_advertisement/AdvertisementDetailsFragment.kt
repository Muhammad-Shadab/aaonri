package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.AdvertiseDetailsResponse
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.databinding.FragmentAdvertisementDetailsBinding
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AdvertisementDetailsFragment : Fragment() {
    var detailsBinding: FragmentAdvertisementDetailsBinding? = null
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    val args: AdvertisementDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsBinding = FragmentAdvertisementDetailsBinding.inflate(inflater, container, false)


        detailsBinding?.apply {


        }

        advertiseViewModel.advertiseDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    detailsBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    setData(response.data)
                    detailsBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    detailsBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        return detailsBinding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(data: AdvertiseDetailsResponse?) {
        detailsBinding?.addImage?.let {
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${data?.advertisementDetails?.adImage}")
                    .into(it)
            }

        }
        detailsBinding?.advertiseNameTv?.text = data?.advertisementDetails?.adTitle
        detailsBinding?.advertiseDateTv?.text = "From ${
            DateTimeFormatter.ofPattern("MMM dd,yyyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data?.fromDate?.split("T")?.get(0))
            )
        } To ${
            DateTimeFormatter.ofPattern("MMM dd,yyyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data?.toDate?.split("T")?.get(0))
            )
        }"
        detailsBinding?.advertiseLocationTv?.text = data?.advertisementDetails?.location
        detailsBinding?.advertiseLinkTv?.text = data?.advertisementDetails?.url
        detailsBinding?.companyDescTv?.text = data?.advertisementDetails?.companyDescription
        detailsBinding?.companyNameTv?.text = data?.advertisementDetails?.companyName
//        detailsBinding?.companyLocationTv?.text = data?.advertisementDetails?.
        detailsBinding?.companyContactTv?.text = data?.advertisementDetails?.contactNo
        detailsBinding?.companyEmailTv?.text = data?.advertisementDetails?.emailId
        detailsBinding?.companyServicesTv?.text = data?.advertisementDetails?.productServices
        detailsBinding?.companyUriTv?.text = data?.advertisementDetails?.url
        detailsBinding?.companyStartDateTv?.text =
            DateTimeFormatter.ofPattern("MMM dd,yyyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data?.fromDate?.split("T")?.get(0))
            )
        detailsBinding?.companyEndDateTv?.text = DateTimeFormatter.ofPattern("MMM dd,yyyy").format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .parse(data?.toDate?.split("T")?.get(0))
        )
        detailsBinding?.companyPlanTv?.text = data?.locationPlanRate?.days.toString()
        detailsBinding?.companyVasTv?.text = data?.advertisementVasMap.toString()
        detailsBinding?.companyAdTitleTv?.text = data?.advertisementDetails?.adTitle
        detailsBinding?.companyTemplateTv?.text = data?.template?.name
        detailsBinding?.companyAdPageTv?.text =
            data?.advertisementPageLocation?.advertisementPage?.pageName
        detailsBinding?.companyPageLocationNameTv?.text =
            data?.advertisementPageLocation?.locationName

    }

    override fun onResume() {
        super.onResume()
        advertiseViewModel.getAdvertiseDetailsById(args.advertiseId)
    }

    override fun onDestroy() {
        super.onDestroy()
        advertiseViewModel.advertiseDetailsData.value = null
    }

}