package com.aaonri.app.data.authentication.register.model

data class CommunitiesListResponse(
    val code: Any,
    val community: List<Community>,
    val count: Int,
    val discusstion: Any,
    val errorDetails: Any,
    val history: Any,
    val job: Any,
    val message: String,
    val status: String,
    val subcategoryCount: Any,
    val totalActiveEvent: Int,
    val totalActiveJobs: Int,
    val totalActiveUser: Int,
    val totalActiveUserAds: Int,
    val user: Any,
    val userAds: Any,
    val userEvent: Any,
    val userInterests: Any
)