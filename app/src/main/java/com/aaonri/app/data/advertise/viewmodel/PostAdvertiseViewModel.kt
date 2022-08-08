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
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostAdvertiseViewModel @Inject constructor(private val advertiseRepository: AdvertiseRepository) :
    ViewModel() {

    var companyBasicDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    val postedAdvertiseData: MutableLiveData<Resource<PostAdvertiseResponse>> = MutableLiveData()


    fun advertiseBasicDetails(
        companyName: String,
        location: String,
        phoneNumber: String,
        email: String,
        services: String,
        link: String,
        description: String,
    ) {
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_NAME] = companyName
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_LOCATION] = location
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_PHONE_NUMBER] = phoneNumber
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_EMAIL] = email
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_PRODUCT_SERVICES_DETAILS] = services
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_LINK] = link
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_DESCRIPTION] = description
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

}