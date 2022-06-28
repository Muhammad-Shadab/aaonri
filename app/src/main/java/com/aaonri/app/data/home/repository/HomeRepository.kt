package com.aaonri.app.data.home.repository

import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.home.api.HomeApi
import javax.inject.Inject

class HomeRepository @Inject constructor(private val homeApi: HomeApi) {

    suspend fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedByUserRequest) =
        homeApi.getClassifiedByUser(getClassifiedsByUserRequest)

}