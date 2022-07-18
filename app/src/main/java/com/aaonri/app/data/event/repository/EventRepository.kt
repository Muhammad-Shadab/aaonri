package com.aaonri.app.data.event.repository

import com.aaonri.app.data.event.api.EventApi
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.model.EventAddGoingRequest
import com.aaonri.app.data.event.model.EventAddInterestedRequest
import com.aaonri.app.data.event.model.PostEventRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventApi: EventApi) {

    suspend fun getRecentEvent(userEmail: String) = eventApi.getRecentEvent(userEmail)

    suspend fun getAllEvent(allEventRequest: AllEventRequest) =
        eventApi.getAllEvent(allEventRequest)

    suspend fun postEvent(postEventRequest: PostEventRequest) = eventApi.postEvent(postEventRequest)

    suspend fun updateEvent(postEventRequest: PostEventRequest) =
        eventApi.updateEvent(postEventRequest)

    suspend fun deleteEvent(eventID: Int) = eventApi.deleteEvent(eventID)

    suspend fun getEventCategory() = eventApi.getEventActiveCategory()

    suspend fun uploadEventPicture(
        files: MultipartBody.Part,
        eventId: RequestBody,
        delImageIds: RequestBody
    ) = eventApi.uploadEventPicture(files, eventId, delImageIds)

    suspend fun getEventDetails(eventID: Int) = eventApi.getEventDetails(eventID)

    suspend fun addEventAddInterested(eventAddInterestedRequest: EventAddInterestedRequest) = eventApi.addEventfav(eventAddInterestedRequest)

    suspend fun addEventGoing(eventAddGoingRequest: EventAddGoingRequest) = eventApi.addEventGoing(eventAddGoingRequest)
}