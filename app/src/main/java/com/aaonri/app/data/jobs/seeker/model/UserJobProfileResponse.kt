package com.aaonri.app.data.jobs.seeker.model

data class UserJobProfileResponse(
    val jobProfile: List<JobProfile>,
    val message: String,
    val status: Boolean
)