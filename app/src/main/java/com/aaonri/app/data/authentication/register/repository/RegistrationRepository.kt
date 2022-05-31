package com.aaonri.app.data.authentication.register.repository

import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.register.api.CountriesApi
import com.aaonri.app.data.authentication.register.api.RegistrationApi
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegistrationRepository @Inject constructor(
    private val registrationApi: RegistrationApi,
    private val countriesApi: CountriesApi
) {

    fun getCommunitiesList(): Flow<CommunitiesListResponse> = flow {
        emit(registrationApi.getAllCommunities())
    }.flowOn(Dispatchers.IO)

    fun getCountriesList(): Flow<CountriesResponse> = flow {
        emit(countriesApi.getCountriesList())
    }.flowOn(Dispatchers.IO)


    suspend fun loginUser(login: Login) = registrationApi.userLogin(login)

}