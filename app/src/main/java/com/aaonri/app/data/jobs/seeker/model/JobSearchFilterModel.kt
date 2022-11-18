package com.aaonri.app.data.jobs.seeker.model

data class JobSearchFilterModel(
    val companyName: String,
    val zipCode: String,
    val yearsOfExperience: String,
    val jobType: String,
    val industries: String
)
