package com.aaonri.app.data.event.repository

import com.aaonri.app.data.event.api.EventApi
import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.model.PostEventRequest
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventApi: EventApi) {

    suspend fun getRecentEvent(userEmail: String) = eventApi.getRecentEvent(userEmail)

    suspend fun getAllEvent(allEventRequest: AllEventRequest) =
        eventApi.getAllEvent(allEventRequest)

    suspend fun postEvent(postEventRequest: PostEventRequest) = eventApi.postEvent(postEventRequest)

    suspend fun getEventCategory() = eventApi.getEventActiveCategory()

}