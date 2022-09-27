package com.aaonri.app.data.home_filter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.register.model.services.ServicesResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.home_filter.repository.HomeFilterRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeFilterViewModel @Inject constructor(private val homeFilterRepository: HomeFilterRepository) :
    ViewModel() {

    var service: MutableLiveData<Resource<ServicesResponse>> = MutableLiveData()
    var selectedCategoryFilter: MutableLiveData<ServicesResponseItem> = MutableLiveData()

    fun getServices() = viewModelScope.launch {
        service.postValue(Resource.Loading())
        val response = homeFilterRepository.getAllServicesInterest()
        service.postValue(handleServiceResponse(response))
    }

    private fun handleServiceResponse(response: Response<ServicesResponse>): Resource<ServicesResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setSelectedServiceCategory(value: ServicesResponseItem) {
        selectedCategoryFilter.postValue(value)
    }

}