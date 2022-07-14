package com.aaonri.app.data.classified.api

import com.aaonri.app.data.classified.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface PostClassifiedApi {

    @GET("/api/v1/category/getCategorySubCategoryMapping")
    suspend fun getClassifiedCategory(): Response<ClassifiedCategoryResponse>

    @Multipart
    @POST("/api/v1/asd/uploadImages")
    suspend fun uploadClassifiedPics(
        @Part files: MultipartBody.Part,
        @Part("adId") adId: RequestBody,
        @Part("delImageIds") delImageIds: RequestBody
    ): Response<ClassifiedUploadPicResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/asd/add")
    suspend fun postClassified(
        @Body postClassifiedRequest: PostClassifiedRequest
    ): Response<PostClassifiedRequest>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/asd/update")
    suspend fun updateClassified(
        @Body postClassifiedRequest: PostClassifiedRequest
    ): Response<PostClassifiedRequest>

    @DELETE("/api/v1/asd/delete/{classifiedId}")
    suspend fun deleteClassified(
        @Path("classifiedId") classifiedId: Int
    ): Response<String>

}