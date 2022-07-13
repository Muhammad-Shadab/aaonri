package com.aaonri.app.data.event.api

import com.aaonri.app.data.classified.model.ClassifiedAdDetailsResponse
import com.aaonri.app.data.event.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface EventApi {

    @GET("/api/v1/event/recentEvents/{email}")
    suspend fun getRecentEvent(@Path("email") userEmail: String): Response<RecentEventResponse>

    @GET("/api/v1/eventcategory/findAllActiveCategories")
    suspend fun getEventActiveCategory(): Response<EventCategoryResponse>

    @GET("/api/v1/event/view/{eventId}")
    suspend fun getEventDetails(
        @Path("eventId") eventId: Int
    ): Response<EventDetailsResponse>

    @POST("/api/v1/event/search")
    suspend fun getAllEvent(
        @Body allEventRequest: AllEventRequest
    ): Response<AllEventResponse>

    @POST("/api/v1/event/createEvent")
    suspend fun postEvent(
        @Body postEventRequest: PostEventRequest
    ): Response<PostEventResponse>

    @Multipart
    @POST("/api/v1/event/uploadImages")
    suspend fun uploadEventPicture(
        @Part files: MultipartBody.Part,
        @Part("eventId") adId: RequestBody,
        @Part("delImageIds") delImageIds: RequestBody
    ): Response<UploadEventPicResponse>

    @PUT("/api/v1/event/update")
    suspend fun updateEvent(
        @Body postEventRequest: PostEventRequest
    ): Response<PostEventResponse>

}