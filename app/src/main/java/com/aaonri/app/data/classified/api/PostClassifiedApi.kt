package com.aaonri.app.data.classified.api

import com.aaonri.app.data.classified.model.ClassifiedCategoryResponse
import com.aaonri.app.data.classified.model.PostClassifiedRequest
import com.aaonri.app.data.classified.model.UploadImagesRequest
import com.aaonri.app.data.classified.model.UploadImagesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface PostClassifiedApi {

    @GET("/api/v1/category/getCategorySubCategoryMapping")
    suspend fun getClassifiedCategory(): Response<ClassifiedCategoryResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/asd/add")
    suspend fun postClassified(
        @Body postClassifiedRequest: PostClassifiedRequest
    ): Response<PostClassifiedRequest>

    /*@FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST("/api/v1/asd/uploadImages")
    suspend fun uploadImages(
        @Field("files[]") arrayOfImages: List<Any>,
        @Field("adId") addId: Int,
        @Field("delImageIds") delImageIds: String? = null,
    ): Response<UploadImagesResponse>*/

    /*@Headers("Content-Type:application/json")
    @POST("/api/v1/asd/uploadImages")
    suspend fun uploadImages(
        @Body uploadImagesRequest: UploadImagesRequest
    ): Response<UploadImagesResponse>*/


}