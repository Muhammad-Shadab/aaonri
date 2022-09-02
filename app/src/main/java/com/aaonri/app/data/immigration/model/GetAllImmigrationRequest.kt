package com.aaonri.app.data.immigration.model

data class GetAllImmigrationRequest(
    val categoryId: String,
    val createdById: String,
    val keywords: String
)