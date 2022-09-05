package com.aaonri.app.data.immigration.model

data class ImmigrationFilterModel(
    val searchQuery: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val activeDiscussion: Boolean? = null,
    val atLeastOnDiscussion: Boolean? = null
)
