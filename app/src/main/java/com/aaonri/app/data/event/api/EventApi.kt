package com.aaonri.app.data.event.api

import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.model.AllEventResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EventApi {

    @POST("/api/v1/event/search")
    suspend fun getAllClassified(
        @Body allEventRequest: AllEventRequest
    ): Response<AllEventResponse>

}