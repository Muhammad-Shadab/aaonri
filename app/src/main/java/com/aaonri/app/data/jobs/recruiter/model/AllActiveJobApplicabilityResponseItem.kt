package com.aaonri.app.data.jobs.recruiter.model

data class AllActiveJobApplicabilityResponseItem(
    val active: Boolean,
    val applicability: String,
    val id: Int,
    var isSelected: Boolean
)