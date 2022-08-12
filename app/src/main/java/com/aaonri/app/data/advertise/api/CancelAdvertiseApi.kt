package com.aaonri.app.data.advertise.api

import com.aaonri.app.data.advertise.model.RenewAdvertiseRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface CancelAdvertiseApi {

    @DELETE("/api/v1/avd/cancelAvd/{advertiseId}")
    suspend fun cancelAdvertise(
        @Path("advertiseId") advertiseId: Int
    ): Response<String>

    @POST("/api/v1/avd/renewAvd")
    suspend fun renewAdvertise(
        @Body renewAdvertiseRequest: RenewAdvertiseRequest
    ): Response<String>

}