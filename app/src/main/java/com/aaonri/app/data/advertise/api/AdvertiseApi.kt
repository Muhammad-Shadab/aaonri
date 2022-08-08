package com.aaonri.app.data.advertise.api

import com.aaonri.app.data.advertise.model.PostAdvertiseRequest
import com.aaonri.app.data.advertise.model.PostAdvertiseResponse
import retrofit2.Response
import retrofit2.http.POST

interface AdvertiseApi {

    @POST("/api/v1/avd/add")
    suspend fun postAdvertisement(
        postAdvertiseRequest: PostAdvertiseRequest
    ): Response<PostAdvertiseResponse>

}