package com.aaonri.app.data.classified.model

data class LikeDislikeClassifiedRequest(
    val emailId: String,
    val favourite: Boolean,
    val itemId: Int,
    val service: String
)