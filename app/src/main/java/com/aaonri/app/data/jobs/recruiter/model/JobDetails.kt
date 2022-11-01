package com.aaonri.app.data.jobs.recruiter.model

data class JobDetails(
    val applicability: List<Any>,
    val applyCount: Int,
    val billingType: String,
    val city: String,
    val company: String,
    val contactPerson: String,
    val country: String,
    val createdBy: String,
    val createdOn: String,
    val description: String,
    val experienceLevel: String,
    val industry: String,
    val isActive: Boolean,
    val jobId: Int,
    val jobType: String,
    val recruiter: String,
    val salaryRange: String,
    val skillSet: String,
    val state: String,
    val street: String,
    val title: String,
    val viewCount: Int,
    val zipCode: String
)