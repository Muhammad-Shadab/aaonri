package com.aaonri.app.data.authentication.register.api

import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import retrofit2.http.GET

interface RegistrationApi {

    @GET("/api/v1/community/activecommunities")
    suspend fun getAllCommunities(): CommunitiesListResponse

}