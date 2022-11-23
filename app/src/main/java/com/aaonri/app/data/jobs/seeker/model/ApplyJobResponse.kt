package com.aaonri.app.data.jobs.seeker.model

data class ApplyJobResponse(
    val action: Any,
    val coverLetter: String,
    val createdOn: String,
    val email: String,
    val fullName: String,
    val id: Int,
    val jobId: Int,
    val phoneNo: String,
    val resumeName: String,
    val message: String,
    val status: Boolean
)