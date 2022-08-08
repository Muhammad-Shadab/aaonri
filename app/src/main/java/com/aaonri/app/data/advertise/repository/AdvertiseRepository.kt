package com.aaonri.app.data.advertise.repository

import com.aaonri.app.data.advertise.api.AdvertiseApi
import com.aaonri.app.data.advertise.model.PostAdvertiseRequest
import javax.inject.Inject

class AdvertiseRepository @Inject constructor(
    private val advertiseApi: AdvertiseApi
) {

    suspend fun postAdvertise(postAdvertiseRequest: PostAdvertiseRequest) =
        advertiseApi.postAdvertisement(postAdvertiseRequest)

}