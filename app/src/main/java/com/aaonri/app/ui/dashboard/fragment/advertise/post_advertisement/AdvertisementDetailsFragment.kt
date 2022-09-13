package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.annotation.SuppressLint
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
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.model.AdvertiseDetailsResponse
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.databinding.FragmentAdvertisementDetailsBinding
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AdvertisementDetailsFragment : Fragment() {
    var binding: FragmentAdvertisementDetailsBinding? = null
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    val args: AdvertisementDetailsFragmentArgs by navArgs()
    var isAdApproved: Boolean? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvertisementDetailsBinding.inflate(inflater, container, false)

        advertiseViewModel.getAdvertiseDetailsById(args.advertiseId)

        binding?.apply {

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
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.classifiedDetailsBottom?.visibility = View.GONE
                }
                is Resource.Success -> {
                    AdvertiseStaticData.updateAdvertiseDetails(response.data)

                    setData(response.data)

                    binding?.classifiedDetailsBottom?.visibility = View.VISIBLE
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
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

        return binding?.root
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(data: AdvertiseDetailsResponse?) {

        val vasCodes = mutableListOf<String>()

        isAdApproved = data?.approved

        if (isAdApproved == true) {
            binding?.moreClassifiedOption?.visibility = View.GONE
        }

        binding?.addImage?.let {
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${data?.advertisementDetails?.adImage}")
                    .into(it)
            }
        }
        binding?.advertiseNameTv?.text = data?.advertisementDetails?.adTitle

        binding?.postedOnDate?.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .parse(data?.fromDate?.split("T")?.get(0))
        )
        binding?.postedOnDate?.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .parse(data?.toDate?.split("T")?.get(0))
        )

        binding?.advertiseLocationTv?.text = data?.advertisementDetails?.location

        if (data?.advertisementDetails?.url?.isNotEmpty() == true) {
            binding?.advertiseLinkTv?.visibility = View.VISIBLE
        } else {
            binding?.advertiseLinkTv?.visibility = View.GONE
        }

        binding?.advertiseLinkTv?.setOnClickListener {
            if (URLUtil.isValidUrl(data?.advertisementDetails?.url)) {
                /*activity?.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(data?.advertisementDetails?.url)
                    )
                )*/

                val action =
                    data?.advertisementDetails?.let { it1 ->
                        AdvertisementDetailsFragmentDirections.actionAdvertisementDetailsFragmentToAdvertiseWebviewFragment(
                            it1.url
                        )
                    }
                if (action != null) {
                    findNavController().navigate(action)
                }
            } else {
                activity?.let { it1 ->
                    Snackbar.make(
                        it1.findViewById(android.R.id.content),
                        "Invalid link", Snackbar.LENGTH_LONG
                    ).show()
                }
            }


        }


        binding?.companyDescTv?.textSize = 14f
        if (!data?.advertisementDetails?.companyDescription.isNullOrEmpty()) {
            binding?.companyDescTv?.fromHtml(data?.advertisementDetails?.companyDescription?.trim())
            binding?.compnyDetails?.visibility = View.VISIBLE
            binding?.companyDescTv?.visibility = View.VISIBLE
        }
        binding?.companyNameTv?.text = data?.advertisementDetails?.companyName
        binding?.companyContactTv?.text =
            data?.advertisementDetails?.contactNo?.replace("""[(,), ]""".toRegex(), "")
                ?.replace("-", "")?.replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")
        binding?.companyEmailTv?.text = data?.advertisementDetails?.emailId
        if (data?.advertisementDetails?.productServices?.isNotEmpty() == true) {
            binding?.companyServicesTv?.text = data.advertisementDetails.productServices
        } else {
            binding?.companyServicesTv?.text = "-"
        }

        binding?.postedOnDate?.text =
            DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data?.fromDate?.split("T")?.get(0))
            )
        binding?.validUpToDate?.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .parse(data?.toDate?.split("T")?.get(0))
        )

        binding?.companyStartDateTv?.text =
            DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data?.fromDate?.split("T")?.get(0))
            )
        binding?.companyEndDateTv?.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .parse(data?.toDate?.split("T")?.get(0))
        )

        binding?.companyPlanTv?.text = data?.locationPlanRate?.days.toString()
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
            binding?.companyVasTv?.text = "-"
        }
        binding?.companyLocationTv?.text = data?.advertisementDetails?.location
        binding?.companyAdTitleTv?.text = data?.advertisementDetails?.adTitle
        if (data?.approved == true) {

            binding?.verifiedTv?.visibility = View.VISIBLE
            binding?.pendingTv?.visibility = View.GONE
        } else {
            binding?.verifiedTv?.visibility = View.GONE
            binding?.pendingTv?.visibility = View.VISIBLE
        }

        binding?.companyTemplateTv?.text = data?.template?.name
        binding?.companyAdPageTv?.text =
            data?.advertisementPageLocation?.advertisementPage?.pageName
        binding?.companyPageLocationNameTv?.text =
            data?.advertisementPageLocation?.locationName
    }

    override fun onDestroy() {
        super.onDestroy()
        advertiseViewModel.advertiseDetailsData.value = null
        advertiseViewModel.cancelAdvertiseData.value = null
        binding = null
    }

}