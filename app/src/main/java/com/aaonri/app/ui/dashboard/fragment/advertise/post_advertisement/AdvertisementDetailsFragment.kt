package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.model.AdvertiseDetailsResponse
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentAdvertisementDetailsBinding
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AdvertisementDetailsFragment : Fragment() {
    var detailsBinding: FragmentAdvertisementDetailsBinding? = null
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    val args: AdvertisementDetailsFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsBinding = FragmentAdvertisementDetailsBinding.inflate(inflater, container, false)

        advertiseViewModel.getAdvertiseDetailsById(args.advertiseId)

        detailsBinding?.apply {

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            moreClassifiedOption.setOnClickListener {
                val action =
                    AdvertisementDetailsFragmentDirections.actionAdvertisementDetailsFragmentToUpdateAndDeleteBottomFragment(
                        args.advertiseId
                    )
                findNavController().navigate(action)
            }

        }

        advertiseViewModel.advertiseDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    detailsBinding?.progressBar?.visibility = View.VISIBLE
                    detailsBinding?.classifiedDetailsBottom?.visibility = View.GONE
                }
                is Resource.Success -> {
                    AdvertiseStaticData.updateAdvertiseDetails(response.data)

                    setData(response.data)

                    detailsBinding?.classifiedDetailsBottom?.visibility = View.VISIBLE
                    detailsBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    detailsBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        advertiseViewModel.cancelAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    findNavController().navigateUp()
                    advertiseViewModel.callAdvertiseApiAfterCancel(true)
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        advertiseViewModel.callAdvertiseDetailsApiAfterUpdating.observe(viewLifecycleOwner) {
            if (it) {
                advertiseViewModel.getAdvertiseDetailsById(args.advertiseId)
                advertiseViewModel.setCallAdvertiseDetailsApiAfterUpdating(false)
            }
        }

        return detailsBinding?.root
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(data: AdvertiseDetailsResponse?) {

        val vasCodes = mutableListOf<String>()

        detailsBinding?.addImage?.let {
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${data?.advertisementDetails?.adImage}")
                    .into(it)
            }
        }
        detailsBinding?.advertiseNameTv?.text = data?.advertisementDetails?.adTitle
        detailsBinding?.advertiseDateTv?.text = "From ${
            DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data?.fromDate?.split("T")?.get(0))
            )
        } To ${
            DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data?.toDate?.split("T")?.get(0))
            )
        }"
        detailsBinding?.advertiseLocationTv?.text = data?.advertisementDetails?.location

        if (data?.advertisementDetails?.url?.isNotEmpty() == true) {
            detailsBinding?.advertiseLinkTv?.visibility = View.VISIBLE
        } else {
            detailsBinding?.advertiseLinkTv?.visibility = View.GONE
        }

        detailsBinding?.advertiseLinkTv?.setOnClickListener {
            if (URLUtil.isValidUrl(data?.advertisementDetails?.url)) {
                activity?.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(data?.advertisementDetails?.url)
                    )
                )
            } else {
                activity?.let { it1 ->
                    Snackbar.make(
                        it1.findViewById(android.R.id.content),
                        "Invalid link", Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }


        detailsBinding?.companyDescTv?.textSize = 14f
        if (!data?.advertisementDetails?.adDescription.isNullOrEmpty()) {
            detailsBinding?.companyDescTv?.fromHtml(data?.advertisementDetails?.adDescription)
            detailsBinding?.compnyDetails?.visibility = View.VISIBLE
            detailsBinding?.companyDescTv?.visibility = View.VISIBLE
        }
        detailsBinding?.companyNameTv?.text = data?.advertisementDetails?.companyName
        detailsBinding?.companyContactTv?.text = data?.advertisementDetails?.contactNo
        detailsBinding?.companyEmailTv?.text = data?.advertisementDetails?.emailId
        if (data?.advertisementDetails?.productServices?.isNotEmpty() == true) {
            detailsBinding?.companyServicesTv?.text = data.advertisementDetails.productServices
        } else {
            detailsBinding?.companyServicesTv?.text = "-"
        }
        if (data?.advertisementDetails?.url?.isNotEmpty() == true) {
            detailsBinding?.companyUriTv?.text = data.advertisementDetails.url
        } else {
            detailsBinding?.companyUriTv?.text = "-"
        }
        detailsBinding?.companyStartDateTv?.text =
            DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data?.fromDate?.split("T")?.get(0))
            )
        detailsBinding?.companyEndDateTv?.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .parse(data?.toDate?.split("T")?.get(0))
        )
        detailsBinding?.companyPlanTv?.text = data?.locationPlanRate?.days.toString()
        data?.advertisementVasMap?.forEach {
            when (it.code) {
                "EPAD" -> {
                    if (!vasCodes.contains("Email Promotional Ads")) {
                        vasCodes.add("Email Promotional Ads")
                    }
                }
                "FLAD" -> {
                    if (!vasCodes.contains("Flashing Advertisement")) {
                        vasCodes.add("Flashing Advertisement")
                    }
                }
            }
        }
        if (vasCodes.isNotEmpty()) {
            //detailsBinding?.companyVasTv?.text = vasCodes.toString().replace("[", "").replace("]", "")
        } else {
            detailsBinding?.companyVasTv?.text = "-"
        }
        detailsBinding?.companyLocationTv?.text = data?.advertisementDetails?.location
        detailsBinding?.companyAdTitleTv?.text = data?.advertisementDetails?.adTitle
        if(data?.approved == true)
        {

        detailsBinding?.verifiedTv?.visibility = View.VISIBLE
            detailsBinding?.pendingTv?.visibility = View.GONE
        }else{
            detailsBinding?.verifiedTv?.visibility = View.GONE
            detailsBinding?.pendingTv?.visibility = View.VISIBLE
        }

        detailsBinding?.companyTemplateTv?.text = data?.template?.name
        detailsBinding?.companyAdPageTv?.text =
            data?.advertisementPageLocation?.advertisementPage?.pageName
        detailsBinding?.companyPageLocationNameTv?.text =
            data?.advertisementPageLocation?.locationName
    }

    override fun onDestroy() {
        super.onDestroy()
        advertiseViewModel.advertiseDetailsData.value = null
        advertiseViewModel.cancelAdvertiseData.value = null
    }

}