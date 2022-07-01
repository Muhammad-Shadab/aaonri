package com.aaonri.app.data.classified.api

import com.aaonri.app.data.classified.model.*
import retrofit2.Response
import retrofit2.http.*

interface ClassifiedApi {

    /* @GET("/api/v1/asd/allUserAds/{emailId}")
     suspend fun allUserAdsClassified(@Path("emailId") email: String): Response<AllUserAdsClassifiedResponse>*/

    @GET("/api/v1/user/findByEmail")
    suspend fun getClassifiedSellerInfo(
        @Query("email") userEmail: String
    ): Response<GetClassifiedSellerResponse>

    @GET("/api/v1/favourite/findFavouriteByEmailAndService")
    suspend fun getFavoriteClassified(
        @Query("emailId") userEmail: String,
        @Query("service") serviceName: String = "Classified"
    ): Response<FavoriteClassifiedResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/asd/search")
    suspend fun getClassifiedByUser(
        @Body getClassifiedsByUserRequest: GetClassifiedByUserRequest
    ): Response<GetClassifiedsByUserResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/favourite/add")
    suspend fun likedDislikeClassified(
        @Body likeDislikeClassifiedRequest: LikeDislikeClassifiedRequest
    ): Response<LikeDislikeClassifiedResponse>

}