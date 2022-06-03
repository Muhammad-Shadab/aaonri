package com.aaonri.app.data.authentication.login.model

data class LoginResponse(
    val changePass: Boolean,
    val emailId: String,
    val isAdmin: Int,
    val massage: String,
    val password: String,
    val user: User,
    val userName: String
)