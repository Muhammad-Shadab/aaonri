package com.aaonri.app.data.advertise.repository

import com.aaonri.app.data.advertise.api.AdvertiseApi
import com.aaonri.app.data.advertise.api.CancelAdvertiseApi
import com.aaonri.app.data.advertise.model.PostAdvertiseRequest
import com.aaonri.app.data.advertise.model.RenewAdvertiseRequest
import com.aaonri.app.data.advertise.model.UpdateAdvertiseRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class AdvertiseRepository @Inject constructor(
    private val advertiseApi: AdvertiseApi,
    private val cancelAdvertiseApi: CancelAdvertiseApi
) {

    suspend fun getAllAdvertise(userEmail: String) = advertiseApi.getAllAdvertise(userEmail)

    suspend fun getAdvertiseDetailsById(advertiseId: Int) =
        advertiseApi.getAdvertiseDetailsById(advertiseId)

    suspend fun cancelAdvertise(advertiseId: Int) = cancelAdvertiseApi.cancelAdvertise(advertiseId)

    suspend fun getAllActiveAdvertisePage() = advertiseApi.getAllActiveAdvertisePage()

    suspend fun getAdvertisePageLocationById(advertisePageId: Int) =
        advertiseApi.getAdvertisePageLocationById(advertisePageId)

    suspend fun getAdvertiseActiveVas(locationCode: String) =
        advertiseApi.getAdvertiseActiveVas(locationCode)

    suspend fun getActiveTemplateForSpinner() = advertiseApi.getActiveTemplateForSpinner()

    suspend fun postAdvertise(postAdvertiseRequest: PostAdvertiseRequest) =
        advertiseApi.postAdvertisement(postAdvertiseRequest)

    suspend fun uploadAdvertiseImage(advertiseId: Int, file: MultipartBody.Part) =
        advertiseApi.uploadAdvertiseImage(advertiseId, file)

    suspend fun renewAdvertise(renewAdvertiseRequest: RenewAdvertiseRequest) =
        advertiseApi.renewAdvertise(renewAdvertiseRequest)

    suspend fun updateAdvertise(updateAdvertiseRequest: UpdateAdvertiseRequest) =
        advertiseApi.updateAdvertise(updateAdvertiseRequest)

}