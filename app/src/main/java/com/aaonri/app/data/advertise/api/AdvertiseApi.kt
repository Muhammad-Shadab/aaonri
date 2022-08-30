package com.aaonri.app.data.advertise.api

import com.aaonri.app.data.advertise.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AdvertiseApi {

    @GET("/api/v1/avd/findMyAvdsByEmail")
    suspend fun getAllAdvertise(
        @Query("emailId") userEmail: String
    ): Result<AllAdvertiseResponse>

    @GET("/api/v1/avd/findById/{advertiseId}")
    suspend fun getAdvertiseDetailsById(
        @Path("advertiseId") advertiseId: Int
    ): Result<AdvertiseDetailsResponse>

    @GET("/api/v1/avdPage/findAllActive")
    suspend fun getAllActiveAdvertisePage(): Result<AdvertiseActivePageResponse>

    @GET("/api/v1/avdPageLocation/getAllPageLocationByPageId")
    suspend fun getAdvertisePageLocationById(
        @Query("pageId") pageId: Int
    ): Result<AdvertisePageLocationResponse>

    @GET("/api/v1/valueAddedServices/findAllActiveVasByAdvLocationCode")
    suspend fun getAdvertiseActiveVas(
        @Query("locationCode") locationCode: String
    ): Result<AdvertiseActiveVasResponse>

    @GET("/api/v1/template/findAllActive")
    suspend fun getActiveTemplateForSpinner(): Result<ActiveTemplateResponse>

    @POST("/api/v1/avd/add")
    suspend fun postAdvertisement(
        @Body postAdvertiseRequest: PostAdvertiseRequest
    ): Result<PostAdvertiseResponse>

    @POST("/api/v1/avd/renewAvd")
    suspend fun renewAdvertise(
        @Body renewAdvertiseRequest: RenewAdvertiseRequest
    ): Result<String>

    @POST("/api/v1/avd/update")
    suspend fun updateAdvertise(
        @Body updateAdvertiseRequest: UpdateAdvertiseRequest
    ): Result<UpdateAdvertiseResponse>
}