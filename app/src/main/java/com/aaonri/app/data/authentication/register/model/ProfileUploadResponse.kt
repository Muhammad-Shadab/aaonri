package com.aaonri.app.data.authentication.register.model

data class ProfileUploadResponse(
    val fileLocation: String,
    val message: String,
    val status: Boolean
)
