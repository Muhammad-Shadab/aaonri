package com.aaonri.app.data.home_filter.api

import com.aaonri.app.data.authentication.register.model.services.ServicesResponse
import retrofit2.Response
import retrofit2.http.GET

interface HomeFilterApi {

    @GET("api/v1/interests/all")
    suspend fun getAllServicesInterest(): Response<ServicesResponse>
}