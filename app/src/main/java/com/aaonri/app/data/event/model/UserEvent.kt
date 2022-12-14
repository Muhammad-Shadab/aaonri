package com.aaonri.app.data.event.model

data class UserEvent(
    val acceptedTermsAndConditions: Boolean,
    val address1: String,
    val address2: String,
    val approved: Boolean,
    val category: String,
    val city: String,
    val createdBy: String,
    val createdOn: String,
    val delImages: Any,
    val description: String,
    val endDate: String,
    val endTime: String,
    val eventPlace: Any,
    val favorite: Boolean,
    val fee: Double,
    val id: Int,
    val images: List<Image>,
    val isActive: Boolean,
    val isFree: Any,
    val socialMediaLink: String,
    val startDate: String,
    val startTime: String,
    val state: Any,
    val timeZone: String,
    val title: String,
    val totalFavourite: Int,
    val totalVisiting: Int,
    val zipCode: String
)