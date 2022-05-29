package com.aaonri.app.data.authentication.register.repository

import com.aaonri.app.data.authentication.register.api.RegistrationApi
import com.aaonri.app.data.authentication.register.model.CommunitiesListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegistrationRepository @Inject constructor(private val registrationApi: RegistrationApi) {

     fun getCommunitiesList(): Flow<CommunitiesListResponse> = flow {
         emit(registrationApi.getAllCommunities())
     }.flowOn(Dispatchers.IO)


}