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
    val myEvent: MutableLiveData<Resource<AllEventResponse>> = MutableLiveData()
    val recentEventData: MutableLiveData<Resource<RecentEventResponse>> = MutableLiveData()
    var clearAllFilter: MutableLiveData<Boolean> = MutableLiveData()
        private set
    val callEventDetailsApiAfterUpdating: MutableLiveData<Boolean> = MutableLiveData()

    var clickedOnFilter: MutableLiveData<Boolean> = MutableLiveData()

    val callEventApiAfterDelete: MutableLiveData<Boolean> = MutableLiveData()

    var sendDataToEventDetailsScreen: MutableLiveData<Event> = MutableLiveData()
        private set

    var hideFloatingButtonInSecondTab: MutableLiveData<Boolean> = MutableLiveData()

    var eventCityList = mutableListOf<String>()
        private set

    var selectedEventCity: MutableLiveData<String> =
        MutableLiveData()
        private set

    var cityFilter = ""
        private set

    var categoryFilter = ""
        private set

    var zipCodeInFilterScreen = ""
        private set

    var selectedEventLocationLiveData: MutableLiveData<String> = MutableLiveData()
        private set

    var isFilterEnable = false
        private set

    var isAllSelected = false
        private set

    var isPaidSelected = false
        private set

    var isFreeSelected = false
        private set

    var isMyLocationCheckedInFilterScreen = false
        private set

    var clearAllFilterBtn: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var allEventList = mutableListOf<Event>()
        private set

    var searchQueryFilter = ""
        private set

    var keyClassifiedKeyboardListener: MutableLiveData<Boolean> = MutableLiveData()
        private set

    fun getMyEvent(allEventRequest: AllEventRequest) = viewModelScope.launch {
        try {
            myEvent.postValue(Resource.Loading())
            val response = eventRepository.getAllEvent(allEventRequest)
            myEvent.postValue(handleAllEventResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            myEvent.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    fun getRecentEvent(userEmail: String) = viewModelScope.launch {
        try {
            recentEventData.postValue(Resource.Loading())
            val response = eventRepository.getRecentEvent(userEmail)
            recentEventData.postValue(handleRecentEventResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            recentEventData.postValue(e.message?.let { Resource.Error(it) })
        }
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
        try {
            allEventData.postValue(Resource.Loading())
            val response = eventRepository.getAllEvent(allEventRequest)
            allEventData.postValue(handleAllEventResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            allEventData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleAllEventResponse(response: Response<AllEventResponse>): Resource<AllEventResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setHideFloatingBtn(value: Boolean) {
        hideFloatingButtonInSecondTab.postValue(value)
    }

    fun setCallEventDetailsApiAfterUpdating(value: Boolean) {
        callEventDetailsApiAfterUpdating.postValue(value)
    }

    fun setCallEventApiAfterDelete(value: Boolean) {
        callEventApiAfterDelete.postValue(value)
    }

    fun setEventCityList(value: MutableList<String>) {
        eventCityList = value
    }

    fun setSelectedEventCity(
        value: String,
    ) {
        selectedEventCity.postValue(value)
    }

    fun setClearAllFilter(value: Boolean) {
        clearAllFilter.postValue(value)
    }

    fun setClickedOnFilter(value: Boolean) {
        clickedOnFilter.postValue(value)
    }

    fun setCityFilter(value: String) {
        cityFilter = value
    }

    fun setCategoryFilter(value: String) {
        categoryFilter = value
    }

    fun setZipCodeInFilterScreen(value: String) {
        zipCodeInFilterScreen = value
    }

    fun setIsFilterEnable(value: Boolean) {
        isFilterEnable = value
    }

    fun setIsAllSelected(value: Boolean) {
        isAllSelected = value
    }

    fun setIsPaidSelected(value: Boolean) {
        isPaidSelected = value
    }

    fun setIsFreeSelected(value: Boolean) {
        isFreeSelected = value
    }

    fun setSelectedEventLocation(value: String) {
        selectedEventLocationLiveData.postValue(value)
    }

    fun setIsMyLocationChecked(value: Boolean) {
        isMyLocationCheckedInFilterScreen = value
    }

    //This is also used in different case
    fun setClickOnClearAllFilterBtn(value: Boolean) {
        clearAllFilterBtn.postValue(value)
    }

    fun setAllEventList(value: List<Event>) {
        allEventList = value as MutableList<Event>
    }

    fun setSearchQuery(value: String) {
        searchQueryFilter = value
    }

    fun setKeyClassifiedKeyboardListener(value: Boolean) {
        keyClassifiedKeyboardListener.postValue(value)
    }
}