package com.aaonri.app.data.authentication.forgot_password.model

data class NewPasswordRequest(
    val email: String,
    val password: String
)