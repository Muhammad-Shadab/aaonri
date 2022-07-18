package com.aaonri.app.data.event.model

data class EventAddGoingResponse(
    val createdOn: String,
    val emailId: String,
    val eventId: Int,
    val id: Int,
    val visiting: Boolean
)