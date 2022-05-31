package com.aaonri.app.data.authentication.register.api

import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegistrationApi {

    @GET("/api/v1/community/activecommunities")
    suspend fun getAllCommunities(): CommunitiesListResponse

    @Headers("Content-Type:application/json")
    @POST("/api/v1/user/authorize")
    suspend fun userLogin(@Body login: Login): Response<LoginResponse>

}