package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.model.*
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertiseCheckoutBinding
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class PostAdvertiseCheckoutFragment : Fragment() {
    var checkoutBinding: FragmentPostAdvertiseCheckoutBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    var advertisePageLocationResponseItem: AdvertisePageLocationResponseItem? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        checkoutBinding = FragmentPostAdvertiseCheckoutBinding.inflate(inflater, container, false)

        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date = dateFormat.format(calender.time)

        checkoutBinding?.apply {
            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_CHECKOUT)

            startdDateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
                    .parse(date)
            )
            endDateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
                    .parse(getCalculatedDate("MM/dd/yyyy", 7))
            )

            checkoutBtn.setOnClickListener {
                postAdvertiseViewModel.apply {

                    if (postAdvertiseViewModel.isRenewAdvertise) {
                        val advertiseData = AdvertiseStaticData.getAddDetails()
                        findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                        /*postAdvertiseViewModel.renewAdvertise(
                            RenewAdvertiseRequest(
                                advertiseData?.advertisementId!!,
                                advertiseData.locationPlanRate.days
                            )
                        )*/
                    } else if (postAdvertiseViewModel.isUpdateAdvertise) {
                        AdvertiseStaticData.getAddDetails()?.advertisementDetails?.advertisementDetailsId?.let { it1 ->
                            AdvertisementDetailsXXXX(
                                adDescription = if (companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_AD_DESCRIPTION]?.isNotEmpty() == true) companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_AD_DESCRIPTION]!! else "",
                                adTitle = companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_ADD_TITLE]!!,
                                advertisementDetailsId = it1,
                                companyDescription = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_DESCRIPTION]!!,
                                companyName = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_NAME]!!,
                                contactNo = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PHONE_NUMBER]!!,
                                emailId = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_EMAIL]!!,
                                location = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LOCATION]!!,
                                productServices = if (companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PRODUCT_SERVICES_DETAILS]?.isNotEmpty() == true) companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PRODUCT_SERVICES_DETAILS]!! else "",
                                url = if (companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK]?.isNotEmpty() == true) companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK]!! else "",
                            )
                        }?.let { it2 ->
                            UpdateAdvertiseRequest(
                                it2,
                                advertisementId = postAdvertiseViewModel.advertiseId,
                                codes = postAdvertiseViewModel.vasList
                            )
                        }?.let { it3 ->
                            postAdvertiseViewModel.updateAdvertise(
                                it3
                            )
                        }
                    } else {
                        postAdvertiseViewModel.postAdvertise(
                            PostAdvertiseRequest(
                                active = true,
                                advertisementDetails = AdvertisementDetails(
                                    adDescription = if (companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_AD_DESCRIPTION]?.isNotEmpty() == true) companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_AD_DESCRIPTION]!! else "",
                                    adTitle = companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_ADD_TITLE]!!,
                                    companyDescription = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_DESCRIPTION]!!,
                                    companyName = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_NAME]!!,
                                    contactNo = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PHONE_NUMBER]!!,
                                    emailId = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_EMAIL]!!,
                                    location = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LOCATION]!!,
                                    productServices = if (companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PRODUCT_SERVICES_DETAILS]?.isNotEmpty() == true) companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PRODUCT_SERVICES_DETAILS]!! else "",
                                    url = if (companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK]?.isNotEmpty() == true) companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK]!! else "",
                                ),
                                emailId = companyContactDetailsMap[AdvertiseConstant.ADVERTISE_EMAIL]!!,
                                locationId = if (advertisePageLocationResponseItem?.locationId.toString()
                                        .isNotEmpty()
                                ) advertisePageLocationResponseItem!!.locationId else 0,
                                paymentStatus = "SUCCESS",
                                planId = if (advertisePageLocationResponseItem?.locationPlanRate?.planId.toString()
                                        .isNotEmpty()
                                ) advertisePageLocationResponseItem!!.locationPlanRate.planId else 0,
                                rate = 0,
                                templateCode = if (companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_TEMPLATE_CODE]?.isNotEmpty() == true) companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_TEMPLATE_CODE]!! else "",
                                vasCodes = postAdvertiseViewModel.vasList
                            )
                        )
                    }
                }
            }
        }

        if (postAdvertiseViewModel.isUpdateAdvertise) {
            setDataForUpdating()
        }

        postAdvertiseViewModel.selectedTemplatePageName.observe(viewLifecycleOwner) { activePageResponse ->
            checkoutBinding?.addPageTv?.text = activePageResponse.pageName
        }

        postAdvertiseViewModel.postedAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI]?.isNotEmpty() == true) {
                        response.data?.advertisementId?.let {
                            findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                            callUploadAdvertisePicApi(it)
                        }
                    } else {
                        findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                    }
                }
                is Resource.Error -> {

                }
            }
        }

        postAdvertiseViewModel.updateAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI]?.isNotEmpty() == true && postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI]?.startsWith(
                            "http:"
                        ) != true
                    ) {
                        response.data?.advertisementId?.let {
                            findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                            callUploadAdvertisePicApi(it)
                        }
                    } else {
                        findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                    }
                }
                is Resource.Error -> {

                }
            }
        }

        postAdvertiseViewModel.renewAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    /*  if (postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI]?.isNotEmpty() == true) {
                          *//*response.data?.advertisementId?.let {
                            callUploadAdvertisePicApi(it)
                        }*//*
                    } else {
                    }*/
                    findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                }
                is Resource.Error -> {

                }
            }
        }

        postAdvertiseViewModel.uploadAdvertiseImageData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    //findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                }
                is Resource.Error -> {

                }
            }
        }

        postAdvertiseViewModel.selectedTemplateLocation.observe(viewLifecycleOwner) {
            advertisePageLocationResponseItem = it
            checkoutBinding?.addPageLocationTv?.text = it.locationName
        }

        return checkoutBinding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDataForUpdating() {
        val advertiseData = AdvertiseStaticData.getAddDetails()
        checkoutBinding?.apply {
            addPageLocationTv.text = advertiseData?.advertisementPageLocation?.locationName
            startdDateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(advertiseData?.fromDate?.split("T")?.get(0))
            )
            endDateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(advertiseData?.toDate?.split("T")?.get(0))
            )
        }
    }

    private fun callUploadAdvertisePicApi(id: Int) {

        val file = File(
            postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI].toString()
                .replace("file:", "")
        )

        val addId = id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage = MultipartBody.Part.createFormData("file", file.name, requestFile)

        postAdvertiseViewModel.uploadAdvertiseImage(id, requestImage)
    }

    fun getCalculatedDate(dateFormat: String?, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

}