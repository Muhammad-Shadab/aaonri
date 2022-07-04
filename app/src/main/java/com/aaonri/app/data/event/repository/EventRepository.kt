package com.aaonri.app.data.event.repository

import com.aaonri.app.data.event.api.EventApi
import com.aaonri.app.data.event.model.AllEventRequest
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventApi: EventApi) {

    suspend fun getAllEvent(allEventRequest: AllEventRequest) =
        eventApi.getAllClassified(allEventRequest)

}