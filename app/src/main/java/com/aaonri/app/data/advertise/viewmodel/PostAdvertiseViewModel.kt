package com.aaonri.app.data.advertise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.model.PostAdvertiseRequest
import com.aaonri.app.data.advertise.model.PostAdvertiseResponse
import com.aaonri.app.data.advertise.repository.AdvertiseRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostAdvertiseViewModel @Inject constructor(private val advertiseRepository: AdvertiseRepository) :
    ViewModel() {

    var companyContactDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var companyBasicDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var selectTemplateName = ""
        private set

    var selectTemplateLocation = ""
        private set

    val postedAdvertiseData: MutableLiveData<Resource<PostAdvertiseResponse>> = MutableLiveData()

    val uploadAdvertiseImageData: MutableLiveData<Resource<String>> = MutableLiveData()

    fun addCompanyContactDetails(
        companyName: String,
        location: String,
        phoneNumber: String,
        email: String,
        services: String,
        link: String,
        description: String,
    ) {
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_NAME] = companyName
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LOCATION] = location
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PHONE_NUMBER] = phoneNumber
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_EMAIL] = email
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PRODUCT_SERVICES_DETAILS] = services
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK] = link
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_DESCRIPTION] = description
    }

    fun addCompanyBasicDetailsMap(
        addTitle: String,
        location: String,
        phoneNumber: String,
        email: String,
        services: String,
        link: String,
        description: String,
    ) {
        //companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_NAME] = companyName
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LOCATION] = location
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PHONE_NUMBER] = phoneNumber
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_EMAIL] = email
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PRODUCT_SERVICES_DETAILS] = services
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK] = link
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_DESCRIPTION] = description
    }

    fun postAdvertise(postAdvertiseRequest: PostAdvertiseRequest) = viewModelScope.launch {
        postedAdvertiseData.postValue(Resource.Loading())
        val response = advertiseRepository.postAdvertise(postAdvertiseRequest)
        postedAdvertiseData.postValue(handlePostAdvertiseResponse(response))
    }

    private fun handlePostAdvertiseResponse(response: Response<PostAdvertiseResponse>): Resource<PostAdvertiseResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun uploadAdvertiseImage(advertiseId: Int, file: MultipartBody.Part) = viewModelScope.launch {
        uploadAdvertiseImageData.postValue(Resource.Loading())
        val response = advertiseRepository.uploadAdvertiseImage(advertiseId, file)
        uploadAdvertiseImageData.postValue(handleUploadImageResponse(response))
    }

    private fun handleUploadImageResponse(response: Response<String>): Resource<String>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setTemplateName(value: String) {
        selectTemplateName = value
    }

    fun setTemplateLocation(value: String) {
        selectTemplateLocation = value
    }


}