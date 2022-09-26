package com.aaonri.app.data.event.model

data class EventAddInterestedRequest(
    val emailId: String,
    val favourite: Boolean,
    val itemId: Int,
    val service: String
)