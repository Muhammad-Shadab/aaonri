package com.aaonri.app.data.advertise.api

import com.aaonri.app.data.advertise.model.*
import retrofit2.Response
import retrofit2.http.*

interface AdvertiseApi {

    @GET("/api/v1/avd/findAdvertisementsByEmailId")
    suspend fun getAllAdvertise(
        @Query("emailId") userEmail: String
    ): Response<AllAdvertiseResponse>

    /*@GET("/api/v1/avd/findMyAvdsByEmail")
    suspend fun getAllAdvertise(
        @Query("emailId") userEmail: String
    ): Response<AllAdvertiseResponse>*/

    @GET("/api/v1/avd/findById/{advertiseId}")
    suspend fun getAdvertiseDetailsById(
        @Path("advertiseId") advertiseId: Int
    ): Response<AdvertiseDetailsResponse>

    @GET("/api/v1/avdPage/findAllActive")
    suspend fun getAllActiveAdvertisePage(): Response<AdvertiseActivePageResponse>

    @GET("/api/v1/avdPageLocation/getAllPageLocationByPageId")
    suspend fun getAdvertisePageLocationById(
        @Query("pageId") pageId: Int
    ): Response<AdvertisePageLocationResponse>

    @GET("/api/v1/valueAddedServices/findAllActiveVasByAdvLocationCode")
    suspend fun getAdvertiseActiveVas(
        @Query("locationCode") locationCode: String
    ): Response<AdvertiseActiveVasResponse>

    @GET("/api/v1/template/findAllActive")
    suspend fun getActiveTemplateForSpinner(): Response<ActiveTemplateResponse>

    @POST("/api/v1/avd/add")
    suspend fun postAdvertisement(
        @Body postAdvertiseRequest: PostAdvertiseRequest
    ): Response<PostAdvertiseResponse>

    @POST("/api/v1/avd/renewAvd")
    suspend fun renewAdvertise(
        @Body renewAdvertiseRequest: RenewAdvertiseRequest
    ): Response<String>

    @POST("/api/v1/avd/update")
    suspend fun updateAdvertise(
        @Body updateAdvertiseRequest: UpdateAdvertiseRequest
    ): Response<UpdateAdvertiseResponse>
}