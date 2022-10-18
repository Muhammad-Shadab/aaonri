package com.aaonri.app.data.authentication.register.model.add_user

data class RegistrationResponse(
    val code: String,
    val community: Any,
    val count: Int,
    val country: String,
    val discusstion: Any,
    val errorDetails: List<ErrorDetail>,
    val history: Any,
    val job: Any,
    val message: String,
    val status: String,
    val subcategoryCount: Any,
    val totalActiveEvent: Int,
    val totalActiveJobs: Int,
    val totalActiveUser: Int,
    val totalActiveUserAds: Int,
    val user: UserX,
    val userAds: Any,
    val userEvent: Any,
    val userInterests: Any
)