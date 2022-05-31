package com.aaonri.app.data.authentication.login.model

data class UserFlag(
    val createdOn: String,
    val dateAccepted: String,
    val flagName: String,
    val flagStatus: Boolean,
    val flagText: String,
    val id: Int
)