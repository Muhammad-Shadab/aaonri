package com.aaonri.app.data.home.api

import com.aaonri.app.data.event.model.EventResponse
import com.aaonri.app.data.home.model.InterestResponse
import com.aaonri.app.data.home.model.PoplarClassifiedResponse
import com.aaonri.app.data.home.model.SendFcmTokenUserIdRequest
import com.aaonri.app.data.home.model.SendFcmTokenUserIdResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeApi {

    @GET("/api/v1/interests/all")
    suspend fun getAllInterest(): Response<InterestResponse>

    @GET("/api/v1/home/event/details")
    suspend fun getAllEvent(): Response<EventResponse>

    @GET("/api/v1/classified/findallpopularonaaonri")
    suspend fun getAllPopularClassified(): Response<PoplarClassifiedResponse>

    @POST("/api/v1/user/changeFireBaseToken")
    suspend fun sendFcmTokenAndUserId(
        @Body sendFcmTokenUserIdRequest: SendFcmTokenUserIdRequest
    ): Response<SendFcmTokenUserIdResponse>

    /*@Headers("Content-Type:application/json")
    @POST("/api/v1/asd/search")
    suspend fun getClassifiedByUser(
        @Body getClassifiedsByUserRequest: GetClassifiedByUserRequest
    ): Response<GetClassifiedsByUserResponse>*/

}