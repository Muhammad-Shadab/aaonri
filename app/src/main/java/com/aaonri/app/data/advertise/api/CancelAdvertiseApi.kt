package com.aaonri.app.data.advertise.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface CancelAdvertiseApi {

    @DELETE("/api/v1/avd/cancelAvd/{advertiseId}")
    suspend fun cancelAdvertise(
        @Path("advertiseId") advertiseId: Int
    ): Response<String>

}