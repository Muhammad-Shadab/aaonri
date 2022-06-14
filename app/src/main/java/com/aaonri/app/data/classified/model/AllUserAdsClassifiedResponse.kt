package com.aaonri.app.data.classified.model

data class AllUserAdsClassifiedResponse(
    val code: Any,
    val community: Any,
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
    val userAds: List<UserAd>,
    val userEvent: Any,
    val userInterests: Any
)