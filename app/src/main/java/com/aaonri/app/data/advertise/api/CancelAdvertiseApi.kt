package com.aaonri.app.data.advertise.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface CancelAdvertiseApi {

    @DELETE("/api/v1/avd/cancelAvd/{advertiseId}")
    suspend fun cancelAdvertise(
        @Path("advertiseId") advertiseId: Int
    ): Result<String>

    @Multipart
    @POST("/api/v1/common/uploadAvdImage")
    suspend fun uploadAdvertiseImage(
        @Query("avdId") adId: Int,
        @Part file: MultipartBody.Part,
    ): Result<String>

}