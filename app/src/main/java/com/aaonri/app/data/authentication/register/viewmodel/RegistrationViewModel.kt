package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerificationResponse
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterationResponse
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponse
import com.aaonri.app.data.authentication.register.model.zip_code.ZipCodeResponse
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel
@Inject constructor(
    private val repository: RegistrationRepository,
) : ViewModel() {

    private var mutableCommunities: MutableStateFlow<Resource<CommunitiesListResponse>> =
        MutableStateFlow(Resource.Empty())
    val communities: StateFlow<Resource<CommunitiesListResponse>> = mutableCommunities

    private var mutableServices: MutableStateFlow<Resource<ServicesResponse>> =
        MutableStateFlow(Resource.Empty())
    val service: StateFlow<Resource<ServicesResponse>> = mutableServices

    val emailAlreadyRegisterData: MutableLiveData<Resource<EmailVerificationResponse>> =
        MutableLiveData()

    val loginData: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    val registerData: MutableLiveData<Resource<RegisterationResponse>> = MutableLiveData()


    fun getCommunities() = viewModelScope.launch {
        mutableCommunities.value = Resource.Loading()
        repository.getCommunitiesList().catch { e ->
            mutableCommunities.value = Resource.Error(e.localizedMessage)
        }.collect { response ->
            mutableCommunities.value = Resource.Success(response)
        }
    }

    fun getServices() = viewModelScope.launch {
        mutableServices.value = Resource.Loading()
        repository.getServicesInterest().catch { e ->
            mutableServices.value = Resource.Error(e.localizedMessage)
        }.collect { response ->
            mutableServices.value = Resource.Success(response)
        }
    }

    fun loginUser(login: Login) = viewModelScope.launch {
        loginData.postValue(Resource.Loading())
        val response = repository.loginUser(login)
        loginData.postValue(handleLoginResponse(response))
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): Resource<LoginResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun isEmailAlreadyRegister(emailVerifyRequest: EmailVerifyRequest) = viewModelScope.launch {
        emailAlreadyRegisterData.postValue(Resource.Loading())
        val response = repository.isEmailAlreadyRegistered(emailVerifyRequest)
        emailAlreadyRegisterData.postValue(handleIsEmailVerifyResponse(response))
    }

    private fun handleIsEmailVerifyResponse(response: Response<EmailVerificationResponse>): Resource<EmailVerificationResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun registerUser(registerRequest: RegisterRequest) = viewModelScope.launch {
        registerData.postValue(Resource.Loading())
        val response = repository.registerUser(registerRequest)
        registerData.postValue(handleRegisterResponse(response))
    }

    private fun handleRegisterResponse(response: Response<RegisterationResponse>): Resource<RegisterationResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }



}