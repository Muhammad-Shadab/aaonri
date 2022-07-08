package com.aaonri.app.data.classified.model

data class UserFlag(
    val createdOn: String,
    val dateAccepted: Any,
    val flagName: String,
    val flagStatus: Boolean,
    val flagText: String,
    val id: Int
)