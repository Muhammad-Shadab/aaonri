package com.aaonri.app.data.jobs.recruiter.model

data class AllJobProfileResponseItem(
    val availability: String,
    val contactEmailId: String,
    val coverLetter: String,
    val createdOn: String,
    val emailId: String,
    val experience: String,
    val firstName: String,
    val id: Int,
    val isActive: Boolean,
    val isApplicant: Boolean,
    val lastName: String,
    val location: String,
    val phoneNo: String,
    val resumeName: String,
    val skillSet: String,
    val title: String,
    val visaStatus: String
)