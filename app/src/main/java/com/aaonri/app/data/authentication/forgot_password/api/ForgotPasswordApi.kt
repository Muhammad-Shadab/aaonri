package com.aaonri.app.data.authentication.forgot_password.api

import com.aaonri.app.data.authentication.forgot_password.model.ResetPassLinkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ForgotPasswordApi {

    @GET("/api/v1/user/fp/{emailId}")
    suspend fun sendResetPasswordLink(@Path("emailId") userEmail: String): Response<ResetPassLinkResponse>

}