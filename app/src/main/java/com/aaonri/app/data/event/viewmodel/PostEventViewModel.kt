package com.aaonri.app.data.event.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.event.model.EventCategoryResponse
import com.aaonri.app.data.event.model.EventCategoryResponseItem
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

    val eventCategoryData: MutableLiveData<Resource<EventCategoryResponse>> = MutableLiveData()

    fun setSelectedEventCategory(value: EventCategoryResponseItem) {
        selectedEventCategory.postValue(value)
    }

    fun setEventTimeZone(value: String) {
        selectedEventTimeZone.postValue(value)
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

}