package com.aaonri.app.data.event.api

import com.aaonri.app.data.event.model.AllEventRequest
import com.aaonri.app.data.event.model.AllEventResponse
import com.aaonri.app.data.event.model.RecentEventResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApi {

    @GET("/api/v1/event/recentEvents/{email}")
    suspend fun getRecentEvent(@Path("email") userEmail: String): Response<RecentEventResponse>

    @POST("/api/v1/event/search")
    suspend fun getAllEvent(
        @Body allEventRequest: AllEventRequest
    ): Response<AllEventResponse>

}