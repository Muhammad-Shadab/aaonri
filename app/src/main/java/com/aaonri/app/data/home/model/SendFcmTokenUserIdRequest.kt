package com.aaonri.app.data.home.model

data class SendFcmTokenUserIdRequest(
    val fcmToken: String,
    val deviceType: String,
    val userId: String
)
