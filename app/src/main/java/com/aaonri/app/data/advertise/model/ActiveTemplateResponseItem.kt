package com.aaonri.app.data.advertise.model

data class ActiveTemplateResponseItem(
    val active: Boolean,
    val code: String,
    val createdOn: String,
    val description: String,
    val name: String,
    val templateId: Int
)