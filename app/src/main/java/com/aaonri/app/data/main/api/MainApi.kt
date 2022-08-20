package com.aaonri.app.data.main.api

import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponse
import retrofit2.Response
import retrofit2.http.GET

interface MainApi {

    @GET("/api/v1/avd/findAllActive")
    suspend fun getAllActiveAdvertise(): Response<FindAllActiveAdvertiseResponse>
}