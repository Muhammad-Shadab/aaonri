package com.aaonri.app.data.authentication.forgot_password.repository

import com.aaonri.app.data.authentication.forgot_password.api.ForgotPasswordApi
import javax.inject.Inject

class ForgotPasswordRepository @Inject constructor(private val forgotPasswordApi: ForgotPasswordApi) {

    suspend fun sendResetPasswordLink(userEmail: String) =
        forgotPasswordApi.sendResetPasswordLink(userEmail)


}
