package com.aaonri.app.data.event.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.model.EventCategoryResponse
import com.aaonri.app.data.event.model.EventCategoryResponseItem
import com.aaonri.app.data.event.model.PostEventRequest
import com.aaonri.app.data.event.model.PostEventResponse
import com.aaonri.app.data.event.repository.EventRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostEventViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    var selectedEventCategory: MutableLiveData<EventCategoryResponseItem> = MutableLiveData()
        private set

    var selectedEventTimeZone: MutableLiveData<String> = MutableLiveData()
        private set

    var postEventData: MutableLiveData<Resource<PostEventResponse>> = MutableLiveData()

    var isEventOffline = false
        private set

    var isEventFree = false
        private set

    val eventCategoryData: MutableLiveData<Resource<EventCategoryResponse>> = MutableLiveData()

    var eventBasicDetailMap: MutableMap<String, String> = mutableMapOf()
        private set

    var eventAddressDetailMap: MutableMap<String, String> = mutableMapOf()
        private set

    var listOfImagesUri = mutableListOf<Uri>()
        private set

    fun setSelectedEventCategory(value: EventCategoryResponseItem) {
        selectedEventCategory.postValue(value)
    }

    fun setEventTimeZone(value: String) {
        selectedEventTimeZone.postValue(value)
    }

    fun setIsEventOffline(value: Boolean) {
        isEventOffline = value
    }

    fun setIsEventFree(value: Boolean) {
        isEventFree = value
    }

    fun getEventCategory() = viewModelScope.launch {
        eventCategoryData.postValue(Resource.Loading())
        val response = eventRepository.getEventCategory()
        eventCategoryData.postValue(handleEventCategoryResponse(response))
    }

    private fun handleEventCategoryResponse(response: Response<EventCategoryResponse>): Resource<EventCategoryResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setEventBasicDetails(
        eventTitle: String,
        eventCategory: String,
        eventStartDate: String,
        eventStartTime: String,
        eventEndDate: String,
        eventEndTime: String,
        eventTimeZone: String,
        eventFee: String,
        eventDesc: String
    ) {
        eventBasicDetailMap[EventConstants.EVENT_TITLE] = eventTitle
        eventBasicDetailMap[EventConstants.EVENT_CATEGORY] = eventCategory
        eventBasicDetailMap[EventConstants.EVENT_START_DATE] = eventStartDate
        eventBasicDetailMap[EventConstants.EVENT_START_TIME] = eventStartTime
        eventBasicDetailMap[EventConstants.EVENT_END_DATE] = eventEndDate
        eventBasicDetailMap[EventConstants.EVENT_END_TIME] = eventEndTime
        eventBasicDetailMap[EventConstants.EVENT_TIMEZONE] = eventTimeZone
        eventBasicDetailMap[EventConstants.EVENT_ASKING_FEE] = eventFee
        eventBasicDetailMap[EventConstants.EVENT_DESC] = eventDesc
    }

    fun setListOfUploadImagesUri(uploadedImagesList: MutableList<Uri>) {
        listOfImagesUri = uploadedImagesList
    }

    fun setEventAddressDetailMap(
        addressLine1: String,
        addressLine2: String,
        cityName: String,
        zipCode: String,
        landmark: String,
        state: String,
        socialMediaLink: String
    ) {
        eventAddressDetailMap[EventConstants.ADDRESS_LINE_1] = addressLine1
        eventAddressDetailMap[EventConstants.ADDRESS_LINE_2] = addressLine2
        eventAddressDetailMap[EventConstants.ADDRESS_CITY] = cityName
        eventAddressDetailMap[EventConstants.ADDRESS_ZIPCODE] = zipCode
        eventAddressDetailMap[EventConstants.ADDRESS_LANDMARK] = landmark
        eventAddressDetailMap[EventConstants.ADDRESS_STATE] = state
        eventAddressDetailMap[EventConstants.ADDRESS_SOCIAL_MEDIA_LINK] = socialMediaLink
    }

    fun postEvent(postEventRequest: PostEventRequest) = viewModelScope.launch {
        postEventData.postValue(Resource.Loading())
        val response = eventRepository.postEvent(postEventRequest)
        postEventData.postValue(handlePostEventResponse(response))
    }

    private fun handlePostEventResponse(response: Response<PostEventResponse>): Resource<PostEventResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}