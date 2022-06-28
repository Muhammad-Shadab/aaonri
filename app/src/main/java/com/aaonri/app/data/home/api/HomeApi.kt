package com.aaonri.app.data.home.api

import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.GetClassifiedsByUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface HomeApi {

    @GET("/api/v1/event/allEvents")
    suspend fun getAllEvent()

    @Headers("Content-Type:application/json")
    @POST("/api/v1/asd/search")
    suspend fun getClassifiedByUser(
        @Body getClassifiedsByUserRequest: GetClassifiedByUserRequest
    ): Response<GetClassifiedsByUserResponse>

}