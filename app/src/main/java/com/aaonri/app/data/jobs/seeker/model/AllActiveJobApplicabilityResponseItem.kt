package com.aaonri.app.data.jobs.seeker.model

data class AllActiveJobApplicabilityResponseItem(
    val active: Boolean,
    val applicability: String,
    val id: Int,
    var isSelected: Boolean
)