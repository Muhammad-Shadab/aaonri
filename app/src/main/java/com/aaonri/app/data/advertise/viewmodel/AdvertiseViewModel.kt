package com.aaonri.app.data.advertise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getAllAdvertise(userEmail: String) = viewModelScope.launch {
        allAdvertiseData.postValue(Resource.Loading())
        val response = advertiseRepository.getAllAdvertise(userEmail)
        allAdvertiseData.postValue(handleAllAdvertiseResponse(response))
    }

    private fun handleAllAdvertiseResponse(response: Response<AllAdvertiseResponse>): Resource<AllAdvertiseResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}