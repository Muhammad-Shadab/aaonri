package com.aaonri.app.data.jobs.recruiter.model

data class SearchAllTalentRequest(
    val allKeyWord: String,
    val anykeyWord: String,
    val availability: String,
    val location: String,
    val skill: String
)