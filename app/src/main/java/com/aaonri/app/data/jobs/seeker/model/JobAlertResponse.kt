package com.aaonri.app.data.jobs.seeker.model

data class JobAlertResponse(
    val jobAlerts: List<JobAlert>,
    val message: String,
    val status: Boolean
)