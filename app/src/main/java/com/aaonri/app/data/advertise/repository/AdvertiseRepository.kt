package com.aaonri.app.data.advertise.repository

import com.aaonri.app.data.advertise.api.AdvertiseApi
import com.aaonri.app.data.advertise.model.PostAdvertiseRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AdvertiseRepository @Inject constructor(
    private val advertiseApi: AdvertiseApi
) {

    suspend fun getAllAdvertise(userEmail: String) = advertiseApi.getAllAdvertise(userEmail)

    suspend fun getAdvertiseDetailsById(advertiseId: Int) =
        advertiseApi.getAdvertiseDetailsById(advertiseId)

    suspend fun postAdvertise(postAdvertiseRequest: PostAdvertiseRequest) =
        advertiseApi.postAdvertisement(postAdvertiseRequest)

    suspend fun uploadAdvertiseImage(advertiseId: RequestBody, file: MultipartBody.Part) =
        advertiseApi.uploadAdvertiseImage(advertiseId, file)

}