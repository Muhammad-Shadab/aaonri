package com.aaonri.app.data.authentication.register.api

import com.aaonri.app.data.authentication.register.model.zip_code.ZipCodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ZipCodeApi {

    @GET("/pincode")
    suspend fun getLocation(
        @Query("postalcode") postalCode: String,
        @Query("countrycode") countryCode: String
    ): Result<ZipCodeResponse>

}