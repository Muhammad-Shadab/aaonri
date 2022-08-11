package com.aaonri.app.data.advertise.api

import com.aaonri.app.data.advertise.model.AdvertiseDetailsResponse
import com.aaonri.app.data.advertise.model.AllAdvertiseResponse
import com.aaonri.app.data.advertise.model.PostAdvertiseRequest
import com.aaonri.app.data.advertise.model.PostAdvertiseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AdvertiseApi {

    @GET("/api/v1/avd/findMyAvdsByEmail")
    suspend fun getAllAdvertise(
        @Query("emailId") userEmail: String
    ): Response<AllAdvertiseResponse>

    @GET("/api/v1/avd/findById/{advertiseId}")
    suspend fun getAdvertiseDetailsById(
        @Path("advertiseId") advertiseId: Int
    ): Response<AdvertiseDetailsResponse>

    @POST("/api/v1/avd/add")
    suspend fun postAdvertisement(
        @Body postAdvertiseRequest: PostAdvertiseRequest
    ): Response<PostAdvertiseResponse>

    @Multipart
    @POST("/api/v1/common/uploadAvdImage")
    suspend fun uploadAdvertiseImage(
        @Part("avdId") adId: RequestBody,
        @Part file: MultipartBody.Part,
    ): Response<String>


}