package com.aaonri.app.data.authentication.forgot_password.repository

import com.aaonri.app.data.authentication.forgot_password.api.ForgotPasswordApi
import com.aaonri.app.data.authentication.forgot_password.model.NewPasswordRequest
import javax.inject.Inject

class ForgotPasswordRepository @Inject constructor(private val forgotPasswordApi: ForgotPasswordApi) {

    suspend fun sendResetPasswordLink(userEmail: String) =
        forgotPasswordApi.sendResetPasswordLink(userEmail)

    suspend fun newPasswordRequest(newPasswordRequest: NewPasswordRequest) =
        forgotPasswordApi.newPasswordRequest(newPasswordRequest)

}
