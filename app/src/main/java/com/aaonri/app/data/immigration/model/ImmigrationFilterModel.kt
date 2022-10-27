package com.aaonri.app.data.immigration.model

data class ImmigrationFilterModel(
    val fifteenDaysSelected: Boolean = false,
    val threeMonthSelected: Boolean = false,
    val oneYearSelected: Boolean = false,
    val activeDiscussion: Boolean = false,
    val atLeastOnDiscussion: Boolean = false
)
