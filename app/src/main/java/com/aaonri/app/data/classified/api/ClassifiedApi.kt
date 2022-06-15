package com.aaonri.app.data.classified.api

import com.aaonri.app.data.classified.model.ClassifiedCategoryResponse
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.GetClassifiedsByUserResponse
import retrofit2.Response
import retrofit2.http.*

interface ClassifiedApi {

    /* @GET("/api/v1/asd/allUserAds/{emailId}")
     suspend fun allUserAdsClassified(@Path("emailId") email: String): Response<AllUserAdsClassifiedResponse>*/


    @Headers("Content-Type:application/json")
    @POST("/api/v1/asd/search")
    suspend fun getClassifiedByUser(
        @Body getClassifiedsByUserRequest: GetClassifiedByUserRequest
    ): Response<GetClassifiedsByUserResponse>

}