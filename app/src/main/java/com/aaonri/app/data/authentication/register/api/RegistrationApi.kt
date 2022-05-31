package com.aaonri.app.data.authentication.register.api

import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterationResponse
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegistrationApi {

    @GET("/api/v1/community/activecommunities")
    suspend fun getAllCommunities(): CommunitiesListResponse

    @GET("api/v1/interests/all")
    suspend fun getAllServicesInterest(): ServicesResponse

    @Headers("Content-Type:application/json")
    @POST("/api/v1/user/authorize")
    suspend fun userLogin(@Body login: Login): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/user/add")
    suspend fun userRegister(@Body registerRequest: RegisterRequest): Response<RegisterationResponse>

}