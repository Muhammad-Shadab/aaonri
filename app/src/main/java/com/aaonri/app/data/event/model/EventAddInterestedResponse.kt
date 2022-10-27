package com.aaonri.app.data.event.model

data class EventAddInterestedResponse(
    val createdOn: String,
    val emailId: String,
    val favourite: Boolean,
    val id: Int,
    val itemId: Int,
    val service: String
)