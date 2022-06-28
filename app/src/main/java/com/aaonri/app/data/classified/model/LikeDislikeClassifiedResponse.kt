package com.aaonri.app.data.classified.model

data class LikeDislikeClassifiedResponse(
    val createdOn: String,
    val emailId: String,
    val favourite: Boolean,
    val id: Int,
    val itemId: Int,
    val service: String
)