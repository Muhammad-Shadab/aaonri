package com.aaonri.app.data.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.event.model.EventResponse
import com.aaonri.app.data.home.model.InterestResponse
import com.aaonri.app.data.home.model.PoplarClassifiedResponse
import com.aaonri.app.data.home.repository.HomeRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {

    var homeEventData: MutableLiveData<Resource<EventResponse>> = MutableLiveData()
        private set

    var allInterestData: MutableLiveData<Resource<InterestResponse>> = MutableLiveData()
        private set

    var homeClassifiedInlineAds: MutableLiveData<FindAllActiveAdvertiseResponseItem> =
        MutableLiveData()

    var homeEventInlineAds: MutableLiveData<FindAllActiveAdvertiseResponseItem> =
        MutableLiveData()

    /*val classifiedByUserData: MutableLiveData<Resource<GetClassifiedsByUserResponse>> =
        MutableLiveData()*/

    val popularClassifiedData: MutableLiveData<Resource<PoplarClassifiedResponse>> =
        MutableLiveData()

    var adsBelowFirstSection: MutableLiveData<MutableList<FindAllActiveAdvertiseResponseItem>> =
        MutableLiveData()

    var adsAbovePopularItem: MutableLiveData<MutableList<FindAllActiveAdvertiseResponseItem>> =
        MutableLiveData()

    fun getAllInterest() = viewModelScope.launch {
        allInterestData.postValue(Resource.Loading())
        homeRepository.getAllInterest().onSuccess {
            allInterestData.postValue(Resource.Success(it))
        }
            .onFailure {
                allInterestData.postValue(Resource.Error(it.localizedMessage))
            }

        /*val response = homeRepository.getAllInterest()
        allInterestData.postValue(handleAllInterestResponse(response))*/
    }

    /*private fun handleAllInterestResponse(response: Response<InterestResponse>): Resource<InterestResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }*/

    fun getHomeEvent() = viewModelScope.launch {
        homeEventData.postValue(Resource.Loading())
        homeRepository.getHomeEvents().onSuccess {
            homeEventData.postValue(Resource.Success(it))
        }
            .onFailure {
                homeEventData.postValue(Resource.Error(it.localizedMessage))
            }
        /*val response = homeRepository.getHomeEvents()
        homeEventData.postValue(handleHomeEventResponse(response))*/
    }

    /*private fun handleHomeEventResponse(response: Response<EventResponse>): Resource<EventResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }*/

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
        homeRepository.getPopularClassified().onSuccess {
            popularClassifiedData.postValue(Resource.Success(it))
        }
            .onFailure {
                popularClassifiedData.postValue(Resource.Error(it.localizedMessage))
            }
        /*val response = homeRepository.getPopularClassified()
        popularClassifiedData.postValue(handlePopularClassifiedResponse(response))*/
    }

    /*private fun handlePopularClassifiedResponse(response: Response<PoplarClassifiedResponse>): Resource<PoplarClassifiedResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }*/

    @JvmName("setHomeClassifiedInlineAds1")
    fun setHomeClassifiedInlineAds(value: FindAllActiveAdvertiseResponseItem) {
        homeClassifiedInlineAds.postValue(value)
    }

    fun setAdsBelowFirstSection(value: MutableList<FindAllActiveAdvertiseResponseItem>) {
        adsBelowFirstSection.postValue(value)
    }

    fun setAdsAbovePopularItem(value: MutableList<FindAllActiveAdvertiseResponseItem>) {
        adsAbovePopularItem.postValue(value)
    }

    fun setHomeEventInlineAds(value: FindAllActiveAdvertiseResponseItem) {
        homeEventInlineAds.postValue(value)
    }


}