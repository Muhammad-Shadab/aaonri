package com.aaonri.app.data.immigration.model

data class ImmigrationCenterModelItem(
    val categories: List<Category>,
    val id: Int,
    val image: String,
    val subtitle: String,
    val title: String
)