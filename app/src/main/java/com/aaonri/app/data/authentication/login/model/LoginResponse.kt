package com.aaonri.app.data.authentication.login.model

data class LoginResponse(
    val changePass: Any,
    val emailId: String,
    val isAdmin: Int,
    val massage: Any,
    val password: String,
    val user: User,
    val userName: String
)