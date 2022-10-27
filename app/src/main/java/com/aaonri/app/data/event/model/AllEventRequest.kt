package com.aaonri.app.data.event.model

data class AllEventRequest(
    val category: String,
    val city: String,
    val from: String,
    val isPaid: Any,
    val keyword: String,
    val maxEntryFee: Int,
    val minEntryFee: Int,
    val myEventsOnly: Boolean,
    val userId: String,
    val zip: String
)