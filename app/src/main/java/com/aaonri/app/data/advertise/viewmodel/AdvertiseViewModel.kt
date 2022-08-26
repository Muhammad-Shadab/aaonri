package com.aaonri.app.data.advertise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.advertise.model.AdvertiseDetailsResponse
import com.aaonri.app.data.advertise.model.AllAdvertiseResponse
import com.aaonri.app.data.advertise.repository.AdvertiseRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AdvertiseViewModel @Inject constructor(private val advertiseRepository: AdvertiseRepository) :
    ViewModel() {

    val allAdvertiseData: MutableLiveData<Resource<AllAdvertiseResponse>> = MutableLiveData()

    val cancelAdvertiseData: MutableLiveData<Resource<String>> = MutableLiveData()

    val advertiseDetailsData: MutableLiveData<Resource<AdvertiseDetailsResponse>> =
        MutableLiveData()

    val callAdvertiseApi: MutableLiveData<Boolean> = MutableLiveData()

    val callAdvertiseDetailsApiAfterUpdating: MutableLiveData<Boolean> = MutableLiveData()

    fun getAllAdvertise(userEmail: String) = viewModelScope.launch {
        try {
            allAdvertiseData.postValue(Resource.Loading())
            val response = advertiseRepository.getAllAdvertise(userEmail)
            allAdvertiseData.postValue(handleAllAdvertiseResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleAllAdvertiseResponse(response: Response<AllAdvertiseResponse>): Resource<AllAdvertiseResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAdvertiseDetailsById(advertiseId: Int) = viewModelScope.launch {
        try {
            advertiseDetailsData.postValue(Resource.Loading())
            val response = advertiseRepository.getAdvertiseDetailsById(advertiseId)
            advertiseDetailsData.postValue(handleAdvertiseDetailsByIdResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleAdvertiseDetailsByIdResponse(response: Response<AdvertiseDetailsResponse>): Resource<AdvertiseDetailsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun cancelAdvertise(advertiseId: Int) = viewModelScope.launch {
        try {
            cancelAdvertiseData.postValue(Resource.Loading())
            val response = advertiseRepository.cancelAdvertise(advertiseId)
            cancelAdvertiseData.postValue(handleCancelAdvertiseResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleCancelAdvertiseResponse(response: Response<String>): Resource<String>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun callAdvertiseApiAfterCancel(value: Boolean) {
        callAdvertiseApi.postValue(value)
    }

    fun setCallAdvertiseDetailsApiAfterUpdating(value: Boolean) {
        callAdvertiseDetailsApiAfterUpdating.postValue(value)
    }

}