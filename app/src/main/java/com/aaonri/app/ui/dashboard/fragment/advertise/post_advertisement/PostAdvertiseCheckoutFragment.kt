package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.model.AdvertisePageLocationResponseItem
import com.aaonri.app.data.advertise.model.AdvertisementDetails
import com.aaonri.app.data.advertise.model.PostAdvertiseRequest
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertiseCheckoutBinding
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class PostAdvertiseCheckout : Fragment() {
    var checkoutBinding: FragmentPostAdvertiseCheckoutBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    var advertisePageLocationResponseItem: AdvertisePageLocationResponseItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        checkoutBinding = FragmentPostAdvertiseCheckoutBinding.inflate(inflater, container, false)
        checkoutBinding?.apply {
            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_CHECKOUT)

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
                    } else {
                        postAdvertiseViewModel.postAdvertise(
                            PostAdvertiseRequest(
                                active = true,
                                advertisementDetails = AdvertisementDetails(
                                    adDescription = if (companyContactDetailsMap[AdvertiseConstant.ADVERTISE_AD_DESCRIPTION]?.isNotEmpty() == true) companyContactDetailsMap[AdvertiseConstant.ADVERTISE_AD_DESCRIPTION]!! else "",
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
                                planId = 1,
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

        postAdvertiseViewModel.selectedTemplatePageName.observe(viewLifecycleOwner) {

        }

        postAdvertiseViewModel.postedAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                    /*  if (postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI]?.isNotEmpty() == true) {
                          response.data?.advertisementId?.let {
                              callUploadAdvertisePicApi(it)
                          }
                      } else {

                      }*/
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
                    findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                    /*  if (postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI]?.isNotEmpty() == true) {
                          response.data?.advertisementId?.let {
                              callUploadAdvertisePicApi(it)
                          }
                      } else {

                      }*/
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
                    findNavController().navigate(R.id.action_postAdvertiseCheckout_to_advertisePostSuccessFragment)
                }
                is Resource.Error -> {

                }
            }
        }

        postAdvertiseViewModel.selectedTemplateLocation.observe(viewLifecycleOwner) {
            advertisePageLocationResponseItem = it
        }

        /*requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (postAdvertiseViewModel.isRenewAdvertise) {
                        lifecycleScope.launch {
                            activity?.finish()
                        }
                    } else {
                        lifecycleScope.launch {
                            findNavController().navigateUp()
                        }
                    }
                }
            })*/

        return checkoutBinding?.root
    }

    private fun setDataForUpdating() {
        val advertiseData = AdvertiseStaticData.getAddDetails()
        checkoutBinding?.apply {
            addPageLocationTv.text = advertiseData?.advertisementPageLocation?.locationName
            startdDateTv.text = advertiseData?.fromDate
            endDateTv.text = advertiseData?.toDate
        }
    }

    private fun callUploadAdvertisePicApi(id: Int) {

        val file = File(
            postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI].toString()
                .replace("file:", "")
        )

        val addId = id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage = MultipartBody.Part.createFormData("files", file.name, requestFile)

        postAdvertiseViewModel.uploadAdvertiseImage(addId, requestImage)
    }

}