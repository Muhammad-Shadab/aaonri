package com.aaonri.app.data.event.model

data class Image(
    val imageId: Int,
    val imagePath: String,
    val referenceId: Int,
    val userEvent: Any
)