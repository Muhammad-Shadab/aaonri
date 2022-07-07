package com.aaonri.app.data.event.model

import com.aaonri.app.data.classified.model.UserAds

data class EventResponse(
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
    val userAds: UserAds,
    val userEvent: List<UserEvent>,
    val userInterests: Any
)