package com.aaonri.app.data.classified.model

data class UploadImagesResponse(
    val code: String,
    val community: Community,
    val count: Int,
    val discusstion: Discusstion,
    val errorDetails: ErrorDetails,
    val history: History,
    val job: Job,
    val message: String,
    val status: String,
    val subcategoryCount: SubcategoryCount,
    val totalActiveEvent: Int,
    val totalActiveJobs: Int,
    val totalActiveUser: Int,
    val totalActiveUserAds: Int,
    val user: User,
    val userAds: UserAdsX,
    val userEvent: UserEvent,
    val userInterests: UserInterests
)