package com.aaonri.app.data.jobs.recruiter.model

data class JobSearchRequest(
    val city: String,
    val company: String,
    val createdByMe: Boolean,
    val experience: String,
    val industry: String,
    val jobType: String,
    val keyWord: String,
    val skill: String,
    val userEmail: String
)