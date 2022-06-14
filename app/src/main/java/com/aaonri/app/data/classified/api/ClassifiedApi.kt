package com.aaonri.app.data.classified.api

import com.aaonri.app.data.classified.model.AllUserAdsClassifiedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ClassifiedApi {

    @GET("/api/v1/asd/allUserAds/{emailId}")
    suspend fun allUserAdsClassified(@Path("emailId") email: String): Response<AllUserAdsClassifiedResponse>

}