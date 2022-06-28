package com.aaonri.app.data.classified.model

import java.io.File

data class UploadImagesRequest(
    val files: ArrayList<File>,
    val adId: Int,
    val delImageIds: String? = null
)
