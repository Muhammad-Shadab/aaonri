package com.aaonri.app.data.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponse
import com.aaonri.app.data.main.repository.MainRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val allActiveAdvertise: MutableLiveData<Resource<FindAllActiveAdvertiseResponse>> =
        MutableLiveData()

    fun getAllActiveAdvertise() = viewModelScope.launch {
        try {
            allActiveAdvertise.postValue(Resource.Loading())
            val response = mainRepository.getAllActiveAdvertise()
            allActiveAdvertise.postValue(handleAllActiveAdvertiseResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            allActiveAdvertise.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleAllActiveAdvertiseResponse(response: Response<FindAllActiveAdvertiseResponse>): Resource<FindAllActiveAdvertiseResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}