package com.aaonri.app.data.jobs.seeker.model

data class CreateAlertRequest(
    val email: String,
    val expectedSalary: String,
    val jobAlertName: String,
    val jobProfileId: Int,
    val keyword: String,
    val location: String,
    val role: String,
    val workExp: String,
    val workStatus: String
)