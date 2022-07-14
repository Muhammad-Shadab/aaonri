package com.aaonri.app.data.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.GetClassifiedsByUserResponse
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.event.model.EventResponse
import com.aaonri.app.data.home.model.InterestResponse
import com.aaonri.app.data.home.model.PoplarClassifiedResponse
import com.aaonri.app.data.home.repository.HomeRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {

    var homeEventData: MutableLiveData<Resource<EventResponse>> = MutableLiveData()
        private set

    var allInterestData: MutableLiveData<Resource<InterestResponse>> = MutableLiveData()
        private set

    /*val classifiedByUserData: MutableLiveData<Resource<GetClassifiedsByUserResponse>> =
        MutableLiveData()*/

    val popularClassifiedData: MutableLiveData<Resource<PoplarClassifiedResponse>> =
        MutableLiveData()

    fun getAllInterest() = viewModelScope.launch {
        allInterestData.postValue(Resource.Loading())
        val response = homeRepository.getAllInterest()
        allInterestData.postValue(handleAllInterestResponse(response))
    }

    private fun handleAllInterestResponse(response: Response<InterestResponse>): Resource<InterestResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getHomeEvent() = viewModelScope.launch {
        homeEventData.postValue(Resource.Loading())
        val response = homeRepository.getHomeEvents()
        homeEventData.postValue(handleHomeEventResponse(response))
    }

    private fun handleHomeEventResponse(response: Response<EventResponse>): Resource<EventResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    /*fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedByUserRequest) =
        viewModelScope.launch {
            classifiedByUserData.postValue(Resource.Loading())
            val response = homeRepository.getClassifiedByUser(getClassifiedsByUserRequest)
            classifiedByUserData.postValue(handleGetClassifiedUserResponse(response))
        }

    private fun handleGetClassifiedUserResponse(response: Response<GetClassifiedsByUserResponse>): Resource<GetClassifiedsByUserResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }*/

    fun getPopularClassified() = viewModelScope.launch {
        popularClassifiedData.postValue(Resource.Loading())
        val response = homeRepository.getPopularClassified()
        popularClassifiedData.postValue(handlePopularClassifiedResponse(response))
    }

    private fun handlePopularClassifiedResponse(response: Response<PoplarClassifiedResponse>): Resource<PoplarClassifiedResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


}