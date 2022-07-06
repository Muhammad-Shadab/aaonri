package com.aaonri.app.data.event.api

import com.aaonri.app.data.event.model.*
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

    @POST("/api/v1/event/createEvent")
    suspend fun postEvent(
        @Body postEventRequest: PostEventRequest
    ): Response<PostEventResponse>

}