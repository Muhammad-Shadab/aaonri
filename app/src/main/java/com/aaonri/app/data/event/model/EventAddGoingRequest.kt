package com.aaonri.app.data.event.model

data class EventAddGoingRequest(
    val emailId: String,
    val eventId: Int,
    val visiting: Boolean
)