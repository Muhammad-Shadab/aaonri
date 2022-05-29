package com.aaonri.app.data.authentication.register.api

import com.aaonri.app.data.authentication.register.model.CommunitiesListResponse
import retrofit2.Response
import retrofit2.http.GET

interface RegistrationApi {

    @GET("/api/v1/community/activecommunities")
    suspend fun getAllCommunities(): CommunitiesListResponse

}