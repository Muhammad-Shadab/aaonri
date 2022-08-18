package com.aaonri.app.data.authentication.register.api

import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerificationResponse
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterationResponse
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponse
import com.aaonri.app.data.classified.model.GetClassifiedSellerResponse
import retrofit2.Response
import retrofit2.http.*

interface RegistrationApi {

    @GET("/api/v1/community/activecommunities")
    suspend fun getAllCommunities(): Response<CommunitiesListResponse>

    @GET("api/v1/interests/all")
    suspend fun getAllServicesInterest(): Response<ServicesResponse>

    @GET("/api/v1/user/findByEmail")
    suspend fun findByEmail(
        @Query("email") userEmail: String
    ): Response<GetClassifiedSellerResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/user/userExists")
    suspend fun isEmailAlreadyRegistered(
        @Body emailVerifyRequest: EmailVerifyRequest
    ): Response<EmailVerificationResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/user/authorize")
    suspend fun userLogin(@Body login: Login): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/user/add")
    suspend fun userRegister(@Body registerRequest: RegisterRequest): Response<RegisterationResponse>

}