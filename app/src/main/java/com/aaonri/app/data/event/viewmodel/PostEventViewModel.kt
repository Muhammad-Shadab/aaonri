package com.aaonri.app.data.event.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.register.model.zip_code.ZipCodeResponse
import com.aaonri.app.data.event.EventConstants
import com.aaonri.app.data.event.model.*
import com.aaonri.app.data.event.repository.EventRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostEventViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    var isNavigateBackBasicDetails = false
        private set

    var navigationForStepper: MutableLiveData<String> = MutableLiveData()
        private set

    val zipCodeData: MutableLiveData<Resource<ZipCodeResponse>> = MutableLiveData()

    val eventDetailsData: MutableLiveData<Resource<EventDetailsResponse>> = MutableLiveData()

    var navigateToEventDetailScreen = false
        private set

    var isUpdateEvent = false
        private set

    var updateEventId = 0
        private set

    var sendDataToEventDetailsScreen: MutableLiveData<Int> = MutableLiveData()
        private set

    var selectedEventCategory: MutableLiveData<EventCategoryResponseItem> = MutableLiveData()
        private set

    var selectedEventTimeZone: MutableLiveData<String> = MutableLiveData()
        private set

    var postEventData: MutableLiveData<Resource<PostEventResponse>> = MutableLiveData()

    var updateEventData: MutableLiveData<Resource<PostEventResponse>> = MutableLiveData()

    var isEventOffline = false
        private set

    var stepViewLastTick: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var isEventFree = false
        private set

    val eventCategoryData: MutableLiveData<Resource<EventCategoryResponse>> = MutableLiveData()

    val deleteEventData: MutableLiveData<Resource<EventDeleteResponse>> = MutableLiveData()

    var eventBasicDetailMap: MutableMap<String, String> = mutableMapOf()
        private set

    var eventAddressDetailMap: MutableMap<String, String> = mutableMapOf()
        private set

    var listOfImagesUri = mutableListOf<Uri>()
        private set

    val uploadPictureData: MutableLiveData<Resource<UploadEventPicResponse>> = MutableLiveData()

    val addInterestedData: MutableLiveData<Resource<EventAddInterestedResponse>> = MutableLiveData()

    val addGoingData: MutableLiveData<Resource<EventAddGoingResponse>> = MutableLiveData()

    val eventuserVisitinginfoData: MutableLiveData<Resource<String>> =
        MutableLiveData()
    val eventuserInterestedinfoData: MutableLiveData<Resource<String>> =
        MutableLiveData()

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

    fun updateEvent(postEventRequest: PostEventRequest) = viewModelScope.launch {
        updateEventData.postValue(Resource.Loading())
        val response = eventRepository.updateEvent(postEventRequest)
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

    fun deleteEvent(eventId: Int) = viewModelScope.launch {
        deleteEventData.postValue(Resource.Loading())
        val response = eventRepository.deleteEvent(eventId)
        deleteEventData.postValue(handleDeleteEventResponse(response))
    }

    private fun handleDeleteEventResponse(response: Response<EventDeleteResponse>): Resource<EventDeleteResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun uploadEventPicture(
        files: MultipartBody.Part,
        eventId: RequestBody,
        delImageIds: RequestBody
    ) =
        viewModelScope.launch {
            uploadPictureData.postValue(Resource.Loading())
            val response = eventRepository.uploadEventPicture(files, eventId, delImageIds)
            uploadPictureData.postValue(handleUploadPictureResponse(response))
        }

    private fun handleUploadPictureResponse(response: Response<UploadEventPicResponse>): Resource<UploadEventPicResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun addStepViewLastTick(value: Boolean) {
        stepViewLastTick.value = value
    }

    fun addNavigationForStepper(value: String) {
        navigationForStepper.value = value
    }

    fun setSendDataToEventDetailsScreen(value: Int) {
        sendDataToEventDetailsScreen.postValue(value)
    }

    fun setNavigateToEventDetailScreen(value: Boolean) {
        navigateToEventDetailScreen = value
    }

    fun setIsUpdateEvent(value: Boolean) {
        isUpdateEvent = value
    }

    fun setUpdateEventId(value: Int) {
        updateEventId = value
    }

    fun getEventDetails(eventId: Int) = viewModelScope.launch {
        eventDetailsData.postValue(Resource.Loading())
        val response = eventRepository.getEventDetails(eventId)
        eventDetailsData.postValue(handleEventDetailsResponse(response))
    }

    private fun handleEventDetailsResponse(response: Response<EventDetailsResponse>): Resource<EventDetailsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setIsNavigateBackToBasicDetails(value: Boolean) {
        isNavigateBackBasicDetails = value
    }

    fun addEventAddInterested(eventAddInterestedRequest: EventAddInterestedRequest) =
        viewModelScope.launch {
            addInterestedData.postValue(Resource.Loading())
            val response = eventRepository.addEventAddInterested(eventAddInterestedRequest)
            addInterestedData.postValue(handleEventAddInterestedResponse(response))
        }

    private fun handleEventAddInterestedResponse(response: Response<EventAddInterestedResponse>): Resource<EventAddInterestedResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun addEventGoing(eventAddGoingRequest: EventAddGoingRequest) = viewModelScope.launch {
        addInterestedData.postValue(Resource.Loading())
        val response = eventRepository.addEventGoing(eventAddGoingRequest)
        addGoingData.postValue(handleaddEventGoingResponse(response))
    }

    private fun handleaddEventGoingResponse(response: Response<EventAddGoingResponse>): Resource<EventAddGoingResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getisUserVisitingEventInfo(email: String, addId: Int) =
        viewModelScope.launch {
            eventuserVisitinginfoData.postValue(Resource.Loading())
            val response = eventRepository.geisUserVisitingEventInfo(email, addId)
            eventuserVisitinginfoData.postValue(handleUserVisitingEventInfoResponse(response))
        }

    private fun handleUserVisitingEventInfoResponse(response: String): Resource<String>? {
        if (response.isNotEmpty()) {
            return Resource.Success(response)
        }
        return Resource.Error("Empty")
    }

    fun getUserisInterested(email: String, services: String, addId: Int) =
        viewModelScope.launch {
            eventuserInterestedinfoData.postValue(Resource.Loading())
            val response = eventRepository.getUserisInterested(email, services, addId)
            eventuserInterestedinfoData.postValue(handleUserIsInterestedInfoResponse(response))
        }

    private fun handleUserIsInterestedInfoResponse(response: String): Resource<String>? {
        if (response.isNotEmpty()) {
            return Resource.Success(response)
        }
        return Resource.Error("Empty")
    }

    fun getLocationByZipCode(postalCode: String, countryCode: String) = viewModelScope.launch {
        zipCodeData.postValue(Resource.Loading())
        val response = eventRepository.getLocationByZipCode(postalCode, countryCode)
        zipCodeData.postValue(handleZipCodeResponse(response))
    }

    private fun handleZipCodeResponse(response: Response<ZipCodeResponse>): Resource<ZipCodeResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}