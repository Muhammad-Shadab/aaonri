package com.aaonri.app.data.advertise.model

data class AdvertisementPage(
    val active: Boolean,
    val createdOn: String,
    val description: String,
    val imageName: String,
    val pageCode: String,
    val pageId: Int,
    val pageName: String,
    val title: String
)