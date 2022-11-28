package com.aaonri.app.data.home.repository

import com.aaonri.app.data.home.api.HomeApi
import com.aaonri.app.data.home.model.SendFcmTokenUserIdRequest
import javax.inject.Inject

class HomeRepository @Inject constructor(private val homeApi: HomeApi) {

    suspend fun getAllInterest() = homeApi.getAllInterest()

    suspend fun getHomeEvents() = homeApi.getAllEvent()

    /*suspend fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedByUserRequest) =
        homeApi.getClassifiedByUser(getClassifiedsByUserRequest)*/

    suspend fun getPopularClassified() = homeApi.getAllPopularClassified()

    suspend fun sendFcmTokenAndUserId(sendFcmTokenUserIdRequest: SendFcmTokenUserIdRequest) = homeApi.sendFcmTokenAndUserId(sendFcmTokenUserIdRequest)
}