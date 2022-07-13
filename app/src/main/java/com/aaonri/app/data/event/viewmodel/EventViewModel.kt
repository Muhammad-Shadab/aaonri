package com.aaonri.app.data.event.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.event.model.*
import com.aaonri.app.data.event.repository.EventRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    val allEventData: MutableLiveData<Resource<AllEventResponse>> = MutableLiveData()
    val recentEventData: MutableLiveData<Resource<RecentEventResponse>> = MutableLiveData()


    var sendDataToEventDetailsScreen: MutableLiveData<Event> = MutableLiveData()
        private set

    val hideFloatingButtonInSecondTab: MutableLiveData<Boolean> = MutableLiveData()

    fun getRecentEvent(userEmail: String) = viewModelScope.launch {
        recentEventData.postValue(Resource.Loading())
        val response = eventRepository.getRecentEvent(userEmail)
        recentEventData.postValue(handleRecentEventResponse(response))
    }

    private fun handleRecentEventResponse(response: Response<RecentEventResponse>): Resource<RecentEventResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    fun getAllEvent(allEventRequest: AllEventRequest) = viewModelScope.launch {
        allEventData.postValue(Resource.Loading())
        val response = eventRepository.getAllEvent(allEventRequest)
        allEventData.postValue(handleAllEventResponse(response))
    }

    private fun handleAllEventResponse(response: Response<AllEventResponse>): Resource<AllEventResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setHideFloatingButtonInSecondTab(value: Boolean) {
        hideFloatingButtonInSecondTab.postValue(value)
    }


}