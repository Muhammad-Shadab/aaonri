package com.aaonri.app.data.jobs.seeker.model

data class SaveJobViewRequest(
    val id: Int,
    val isApplied: Boolean,
    val jobId: Int,
    val lastViewedOn: String,
    val noOfTimesViewed: Int,
    val userId: String,
    val viewerName: Any
)