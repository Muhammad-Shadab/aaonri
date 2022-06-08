package com.aaonri.app.data.authentication.forgot_password.api

import com.aaonri.app.data.authentication.forgot_password.model.NewPasswordRequest
import com.aaonri.app.data.authentication.forgot_password.model.ResetPassLinkResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ForgotPasswordApi {

    @GET("/api/v1/user/fp/{emailId}")
    suspend fun sendResetPasswordLink(@Path("emailId") userEmail: String): Response<ResetPassLinkResponse>

    @POST("/api/v1/user/password/reset")
    suspend fun newPasswordRequest(
        @Body newPasswordRequest: NewPasswordRequest
    ): Response<ResponseBody>

}