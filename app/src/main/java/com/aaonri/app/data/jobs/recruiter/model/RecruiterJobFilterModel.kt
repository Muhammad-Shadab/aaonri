package com.aaonri.app.data.jobs.recruiter.model

data class RecruiterJobFilterModel(
    val anyKeywords: String,
    val allKeywords: String,
    val availability: String,
    val location: String,
    val skillSet: String,
)