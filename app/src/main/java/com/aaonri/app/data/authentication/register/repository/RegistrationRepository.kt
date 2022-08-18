package com.aaonri.app.data.authentication.register.repository

import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.register.api.CountriesApi
import com.aaonri.app.data.authentication.register.api.RegistrationApi
import com.aaonri.app.data.authentication.register.api.ZipCodeApi
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegistrationRepository @Inject constructor(
    private val registrationApi: RegistrationApi,
    private val countriesApi: CountriesApi,
    private val zipCodeApi: ZipCodeApi
) {

    suspend fun getCommunitiesList() = registrationApi.getAllCommunities()

    suspend fun getCountries() = countriesApi.getCountriesList()

    suspend fun getServicesInterest() = registrationApi.getAllServicesInterest()

    suspend fun findByEmail(email: String) = registrationApi.findByEmail(email)

    suspend fun isEmailAlreadyRegistered(emailVerifyRequest: EmailVerifyRequest) =
        registrationApi.isEmailAlreadyRegistered(emailVerifyRequest)

    suspend fun loginUser(login: Login) = registrationApi.userLogin(login)

    suspend fun registerUser(registerRequest: RegisterRequest) =
        registrationApi.userRegister(registerRequest)

    suspend fun getLocationByZipCode(postalCode: String, countryCode: String) =
        zipCodeApi.getLocation(postalCode, countryCode)

}