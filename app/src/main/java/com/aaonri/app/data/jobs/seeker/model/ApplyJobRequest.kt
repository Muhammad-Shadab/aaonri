package com.aaonri.app.data.jobs.seeker.model

data class ApplyJobRequest(
    val coverLetter: String,
    val email: String,
    val fullName: String,
    val jobId: Int,
    val phoneNo: String,
    val resumeName: String
)