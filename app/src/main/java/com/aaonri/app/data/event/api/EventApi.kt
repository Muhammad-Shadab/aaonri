package com.aaonri.app.data.event.api

import com.aaonri.app.data.event.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface EventApi {

    @GET("/api/v1/event/recentEvents/{email}")
    suspend fun getRecentEvent(@Path("email") userEmail: String): Result<RecentEventResponse>

    @GET("/api/v1/eventcategory/findAllActiveCategories")
    suspend fun getEventActiveCategory(): Result<EventCategoryResponse>

    @GET("/api/v1/event/view/{eventId}")
    suspend fun getEventDetails(
        @Path("eventId") eventId: Int
    ): Result<EventDetailsResponse>

    @GET("/api/v1/eventvisit/isUserVisitingEvent")
    suspend fun geisUserVisitingEventInfo(
        @Query("eventId") eventId: Int,
        @Query("email") email: String,
    ): Result<String>

    @GET("/api/v1/favourite/getItemFavouriteByEmailAndService")
    suspend fun getUserisInterested(
        @Query("itemId") eventId: Int,
        @Query("service") service: String,
        @Query("email") email: String,
    ): Result<String>

    @POST("/api/v1/event/search")
    suspend fun getAllEvent(
        @Body allEventRequest: AllEventRequest
    ): Result<AllEventResponse>

    @POST("/api/v1/event/createEvent")
    suspend fun postEvent(
        @Body postEventRequest: PostEventRequest
    ): Result<PostEventResponse>

    @Multipart
    @POST("/api/v1/event/uploadImages")
    suspend fun uploadEventPicture(
        @Part files: MultipartBody.Part,
        @Part("eventId") adId: RequestBody,
        @Part("delImageIds") delImageIds: RequestBody
    ): Result<UploadEventPicResponse>

    @POST("/api/v1/favourite/add")
    suspend fun addEventfav(
        @Body addEvnetInterestedRequest: EventAddInterestedRequest
    ): Result<EventAddInterestedResponse>


    @POST("/api/v1/eventvisit/add")
    suspend fun addEventGoing(
        @Body eventAddGoingRequest: EventAddGoingRequest
    ): Result<EventAddGoingResponse>

    @PUT("/api/v1/event/update")
    suspend fun updateEvent(
        @Body postEventRequest: PostEventRequest
    ): Result<PostEventResponse>

    @DELETE("/api/v1/event/delete/{eventId}")
    suspend fun deleteEvent(
        @Path("eventId") eventId: Int
    ): Result<EventDeleteResponse>

}