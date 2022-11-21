package com.aaonri.app.data.jobs.seeker.model

data class JobAlert(
    val email: String,
    val expectedSalary: String,
    val keyword: String,
    val id: Int,
    val jobAlertName: String,
    val jobProfileId: String,
    val location: String,
    val role: String,
    val workExp: String,
    val workStatus: String
)