package com.aaonri.app.data.authentication.register.model.add_user

data class UserFlag(
    val createdOn: String,
    val dateAccepted: Any,
    val flagName: String,
    val flagStatus: Boolean,
    val flagText: String,
    val id: Int
)