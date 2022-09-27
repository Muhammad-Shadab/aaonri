package com.aaonri.app.data.home_filter.repository

import com.aaonri.app.data.home_filter.api.HomeFilterApi
import javax.inject.Inject

class HomeFilterRepository @Inject constructor(private val homeFilterApi: HomeFilterApi) {

    suspend fun getAllServicesInterest() = homeFilterApi.getAllServicesInterest()


}