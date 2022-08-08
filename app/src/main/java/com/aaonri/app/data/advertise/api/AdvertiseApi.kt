package com.aaonri.app.data.advertise.api

import com.aaonri.app.data.advertise.model.AllAdvertiseResponse
import com.aaonri.app.data.advertise.model.PostAdvertiseRequest
import com.aaonri.app.data.advertise.model.PostAdvertiseResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AdvertiseApi {

    @GET("/api/v1/avd/findMyAvdsByEmail")
    suspend fun getAllAdvertise(
        @Query("emailId") userEmail: String
    ): Response<AllAdvertiseResponse>

    @POST("/api/v1/avd/add")
    suspend fun postAdvertisement(
        postAdvertiseRequest: PostAdvertiseRequest
    ): Response<PostAdvertiseResponse>

    @Multipart
    @POST("/api/v1/common/uploadAvdImage")
    suspend fun uploadAdvertiseImage(
        @Query("avdId") advertiseId: Int,
        @Part file: MultipartBody.Part,
    ): Response<String>


}