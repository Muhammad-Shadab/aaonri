package com.aaonri.app.data.main.repository

import com.aaonri.app.data.main.api.MainApi
import javax.inject.Inject

class MainRepository @Inject constructor(private val mainApi: MainApi) {

    suspend fun getAllActiveAdvertise() = mainApi.getAllActiveAdvertise()

}